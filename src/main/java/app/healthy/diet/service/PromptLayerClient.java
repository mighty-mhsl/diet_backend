package app.healthy.diet.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class PromptLayerClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${promptlayer.api-key}")
    private String apiKey;

    public String getPrompt(String promptId) throws IOException {
        String url = "https://api.promptlayer.com/v1/prompts/" + promptId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        JsonNode json = objectMapper.readTree(response.getBody());
        return json.path("prompt").asText();
    }
}
