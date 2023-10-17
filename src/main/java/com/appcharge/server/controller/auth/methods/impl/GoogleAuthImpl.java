package com.appcharge.server.controller.auth.methods.impl;

import com.appcharge.server.controller.auth.methods.GoogleAuth;
import com.appcharge.server.models.auth.AuthResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class GoogleAuthImpl implements GoogleAuth {
    public AuthResult authenticate(String appId, String token) {
        String url = "https://oauth2.googleapis.com/tokeninfo?access_token=" + token;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String responseString = response.body();
                ObjectMapper mapper = new ObjectMapper();
                JsonNode data = mapper.readTree(responseString);

                if (data.get("aud").asText().equals(appId)) {
                    return new AuthResult(true, data.get("sub").asText());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new AuthResult(false, null);
    }
}
