package app.healthy.diet.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class PromptLayerClient {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${promptlayer.api-key}")
    private String apiKey;

    public String getPrompt(String promptId) throws IOException {
        String response = webClient.get()
                .uri("https://api.promptlayer.com/v1/prompts/" + promptId)
                .header("Authorization", "Bearer " + apiKey)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        JsonNode json = objectMapper.readTree(response);
        return json.path("prompt").asText();
    }
}
