package vn.com.hugio.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LoggingUtil {

    private static final List<String> SENSITIVE_FIELD = new ArrayList<>(List.of("password", "jwt", "token", "client_secret"));

    public static String maskValue(String input) {
        List<String> element = new ArrayList<>(List.of(input.split("\"")));
        for (int i = 0; i < element.size(); i++) {
            String currentField = element.get(i);
            String colon;
            try {
                colon = element.get(i + 1);
            } catch (IndexOutOfBoundsException e) {
                continue;
            }
            if (isSensitive(currentField)) {
                if (colon.trim().contains(":")) {
                    element.set(i + 2, "***********");
                }
            } else if ((i + 2 < element.size()) && element.get(i + 2).length() > 1000) {
                element.set(
                        i + 2,
                        element.get(i + 2).substring(0, 50) + "..." +
                                element.get(i + 2).substring(element.get(i + 2).length() - 50)
                );
            }
        }
        return String.join("\"", element);
    }

    public static boolean isSensitive(String input) {
        for (String s : SENSITIVE_FIELD) {
            if (input.toLowerCase().contains(s.toLowerCase()) || s.toLowerCase().contains(input.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static String beautifyJson(String input) throws JsonProcessingException {
        JSONObject jsonObject = new JSONObject(input);
        return jsonObject.toString();

        //Map<String, Object> map = OBJECT_MAPPER.readValue(input, new TypeReference<>() {
        //});
        //return "\n" + OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(map);

    }

}
