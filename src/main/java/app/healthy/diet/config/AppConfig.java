package app.healthy.diet.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }


    @Bean
    public ChatClient chatClient(AnthropicChatModel anthropicChatModel) {
        return ChatClient.create(anthropicChatModel);
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}
