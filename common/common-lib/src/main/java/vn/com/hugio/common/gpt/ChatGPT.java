package vn.com.hugio.common.gpt;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.com.hugio.common.utils.HttpUtil;

@Service
public class ChatGPT {

    @Value("${chat.api.key:}")
    private String chatApiKey;

    private final HttpUtil httpUtil;
    private final ObjectMapper objectMapper;

    public ChatGPT(HttpUtil httpUtil,
                   ObjectMapper objectMapper) {
        this.httpUtil = httpUtil;
        this.objectMapper = objectMapper;
    }

    public String chatGPT(String text, int... maxToken) throws Exception {
        String url = "https://api.openai.com/v1/completions";
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + this.chatApiKey);

        JSONObject data = new JSONObject();
        data.put("model", "text-davinci-003");
        data.put("prompt", text);
        //data.put("max_tokens", 4000);
        //data.put("max_tokens", 50);
        data.put("max_tokens", maxToken.length > 0 ? maxToken[0] : 800);
        //data.put("max_tokens", text.length());
        data.put("temperature", 1.0);

        con.setDoOutput(true);
        con.getOutputStream().write(data.toString().getBytes());

        String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
                .reduce((a, b) -> a + b).get();

        //System.out.println(new JSONObject(output).getJSONArray("choices").getJSONObject(0).getString("text"));
        return new JSONObject(output).getJSONArray("choices").getJSONObject(0).getString("text");
    }

    public String chatGPT2(String text) throws Exception {
        String chatGptHostUrl = "https://api.openai.com/v1/chat/completions";
        URL url = (new URI(chatGptHostUrl)).toURL();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + this.chatApiKey);

        Turbo16kBaseRequest request = Turbo16kBaseRequest.builder()
                .model("gpt-3.5-turbo-16k")
                .messages(
                        List.of(
                                Messages.builder()
                                        .role("system")
                                        .content("You are a chatbot which can search text and provide a summarised answer.")
                                        .build(),
                                Messages.builder()
                                        .role("user")
                                        .content("How are you?")
                                        .build(),
                                Messages.builder()
                                        .role("assistant")
                                        .content("I am doing well")
                                        .build(),
                                Messages.builder()
                                        .role("user")
                                        .content(text + "\nHãy trả lời không hợp lệ bằng tiếng anh nếu như bạn nhận thấy câu hỏi trên chứa các nội dung không phù hợp")
                                        .build()
                        )
                )
                .build();

        con.setDoOutput(true);
        con.getOutputStream().write(this.objectMapper.writeValueAsString(request).getBytes());

        String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
                .reduce((a, b) -> a + b).get();

        //System.out.println(new JSONObject(output).getJSONArray("choices").getJSONObject(0).getString("text"));
        return new JSONObject(output).getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
    }
}