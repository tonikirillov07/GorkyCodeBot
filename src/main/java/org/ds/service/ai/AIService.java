package org.ds.service.ai;

import chat.giga.model.completion.*;
import com.openai.client.OpenAIClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class AIService {
    private final OpenAIClient openAIClient;

    public AIService(OpenAIClient openAIClient) {
        this.openAIClient = openAIClient;
    }

    /**
     * AI generating response based on prompt
     * @param prompt - prompt for AI
     * @return response from AI
     */
    public String getResponse(@NotNull String prompt) {
        ChatCompletionCreateParams createParams = ChatCompletionCreateParams.builder()
                .addUserMessage(prompt)
                .model("deepseek/deepseek-chat-v3.1:free")
                .build();

        ChatCompletion completion = openAIClient.chat().completions().create(createParams);

        return completion.choices().getFirst().message().content().get();
    }
}
