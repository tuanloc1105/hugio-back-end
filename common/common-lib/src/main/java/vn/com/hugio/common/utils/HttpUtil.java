package vn.com.hugio.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import vn.com.hugio.common.log.LOG;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@Component
public class HttpUtil {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final RestTemplate noSslRestTemplate;

    @Autowired
    public HttpUtil(ObjectMapper objectMapper,
                    @Qualifier("restTemplate") RestTemplate restTemplate,
                    @Qualifier("noSslRestTemplate") RestTemplate noSslRestTemplate) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
        this.noSslRestTemplate = noSslRestTemplate;
    }

    public <T> ResponseEntity<T> callApi(Object data, String url, HttpMethod method, Map<String, String> requestHeader, ParameterizedTypeReference<T> respModel, boolean sslCheck, boolean... isLog) throws JsonProcessingException {
        if (isLog.length > 0 && isLog[0]) {
            LOG.info("STARTING FETCHING API TO ENDPOINT %s WITH VALUE \n %s", url, LoggingUtil.maskValue(this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data)));
        }
        HttpHeaders headers = new HttpHeaders();
        if (requestHeader != null && !requestHeader.isEmpty()) {
            for (Map.Entry<String, String> entry : requestHeader.entrySet()) {
                headers.set(entry.getKey(), entry.getValue());
            }
        }
        HttpEntity<Object> requestEntity = new HttpEntity<>(data, headers);
        try {
            ResponseEntity<T> response;
            if (sslCheck) {
                response = this.restTemplate.exchange(url, method, requestEntity, respModel);
            } else {
                response = this.noSslRestTemplate.exchange(url, method, requestEntity, respModel);
            }
            if (isLog.length >= 2 && isLog[1]) {
                LOG.info("[API RESPONSE] \n %s", LoggingUtil.maskValue(this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response.getBody())));
            }
            return response;
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw e;
        } finally {
            if (isLog.length > 0 && isLog[0]) {
                LOG.info("FETCHING API COMPLETED");
            }
        }
    }

    public HttpURLConnection generateConnection(String host) throws Exception {
        URL url = (new URI(host)).toURL();
        return (HttpURLConnection) url.openConnection();
    }

    public <T> T sendRequest(Object data, String url, HttpMethod method, Map<String, String> requestHeader, TypeReference<T> respModel) throws Exception {
        HttpURLConnection http = this.generateConnection(url);
        http.setRequestMethod(method.name());
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "application/json");
        if (requestHeader != null && !requestHeader.isEmpty()) {
            for (Map.Entry<String, String> entry : requestHeader.entrySet()) {
                http.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        if (data != null) {
            String dataJson = this.objectMapper.writeValueAsString(data);
            byte[] out = dataJson.getBytes(StandardCharsets.UTF_8);
            OutputStream stream = http.getOutputStream();
            stream.write(out);
            stream.close();
        }
        LOG.info("[REQUEST STATUS] {}", http.getResponseCode());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(http.getResponseCode() == 200 ? http.getInputStream() : http.getErrorStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String output;
        while ((output = bufferedReader.readLine()) != null) {
            stringBuilder.append(output);
        }
        if (respModel.getType().getTypeName().equals(String.class.getName())) {
            throw new RuntimeException("Input casting type can not be String.class");
        }
        return this.objectMapper.readValue(stringBuilder.toString(), respModel);
    }

    public <T> T callApiHttpPost(Object request, String url, Map<String, String> requestHeader, TypeReference<T> respModel) {
        try {
            HttpPost post = new HttpPost(url);
            if (requestHeader != null && !requestHeader.isEmpty()) {
                for (Map.Entry<String, String> entry : requestHeader.entrySet()) {
                    post.setHeader(entry.getKey(), entry.getValue());
                }
            } else {
                post.setHeader("Content-type", "application/json;charset=UTF-8");
            }
            String req = this.objectMapper.writeValueAsString(request);
            StringEntity postingString = new StringEntity(req, ContentType.parse("UTF-8"));
            post.setEntity(postingString);
            CloseableHttpClient httpClient = null;
            try {
                final String[] res = new String[1];
                httpClient = HttpClientBuilder.create().build();
                HttpResponse response = httpClient.execute(post, (HttpClientResponseHandler<HttpResponse>) httpClientResponse -> {
                    InputStream in = httpClientResponse.getEntity().getContent(); //Get the data in the entity
                    res[0] = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
                    return httpClientResponse;
                });
                if (response != null) {
                    if (respModel.getType().getTypeName().equals(String.class.getName())) {
                        throw new RuntimeException("Input casting type can not be String.class");
                    }
                    return this.objectMapper.readValue(res[0], respModel);
                }
                return null;
            } catch (Exception e) {
                LOG.exception(e);
                throw e;
            } finally {
                if (Optional.ofNullable(httpClient).isPresent()) {
                    httpClient.close();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
