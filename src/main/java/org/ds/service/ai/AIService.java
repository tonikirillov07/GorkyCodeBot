package org.ds.service.ai;

import chat.giga.client.GigaChatClient;
import chat.giga.model.ModelName;
import chat.giga.model.completion.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AIService {
    private final GigaChatClient gigaChatClient;

    public AIService(GigaChatClient gigaChatClient) {
        this.gigaChatClient = gigaChatClient;
    }

    public String getResponse(@NotNull String prompt) {
        CompletionResponse completionResponse = gigaChatClient.completions(CompletionRequest.builder()
                .model(ModelName.GIGA_CHAT_MAX)
                .message(ChatMessage.builder()
                        .content(prompt)
                        .role(ChatMessageRole.USER)
                        .build())
                .build());
        List<Choice> choices = completionResponse.choices();

        return choices.getFirst().message().content();
    }
}
