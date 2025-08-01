package app.healthy.diet.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.anthropic.AnthropicChatOptions;


@Component
@RequiredArgsConstructor
public class AnthropicClient {

    private final ChatClient chatClient;


    public String complete(String prompt) {
        AnthropicChatOptions options = AnthropicChatOptions.builder()
//                .model("claude-sonnet-4-20250514")
                .model("claude-3-7-sonnet-latest")
                .maxTokens(8_192)
                .build();
        return chatClient.prompt()
                .user(prompt)
                .options(options)
                .call()
                .content();
    }
}
