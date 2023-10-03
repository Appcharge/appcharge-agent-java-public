package com.appcharge.server.controller.auth.methods.impl;

import com.appcharge.server.controller.auth.methods.FacebookAuth;
import com.appcharge.server.models.auth.AuthResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Service
public class FacebookAuthImpl implements FacebookAuth {

    public AuthResult authenticate(String appId, String token, String appSecret) {
        String url = "https://graph.facebook.com/debug_token?input_token="
                + URLEncoder.encode(token, StandardCharsets.UTF_8) +
                "&access_token=" + URLEncoder.encode(appId + "|" + appSecret, StandardCharsets.UTF_8);

        System.out.println(url);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String responseString = response.body();
                ObjectMapper mapper = new ObjectMapper();
                JsonNode responseObj = mapper.readTree(responseString);
                boolean isValid = responseObj.get("data").get("is_valid").asBoolean();
                String userId = responseObj.get("data").get("user_id").asText();

                return new AuthResult(isValid, userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new AuthResult(false, "0");
    }
}
