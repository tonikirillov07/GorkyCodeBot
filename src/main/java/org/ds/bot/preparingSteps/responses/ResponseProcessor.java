package org.ds.bot.preparingSteps.responses;

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ds.service.ai.AIService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class ResponseProcessor {
    private static final Log log = LogFactory.getLog(ResponseProcessor.class);
    private final AIService aIService;

    public ResponseProcessor(AIService aIService) {
        this.aIService = aIService;
    }

    public <T extends Response> void processResponse(
            @NotNull String prompt,
            Class<T> responseClass,
            Consumer<Response> onCorrect,
            Consumer<String> onIncorrect) {

        String responseMessage = aIService.getResponse(prompt);

        if (responseMessage.toCharArray()[0] != '{') {
            responseMessage = responseMessage.replaceAll("`", "");
            responseMessage = responseMessage.replaceAll("json", "");
        }

        log.info("Response: %s".formatted(responseMessage));

        Gson gson = new Gson();
        Response response = gson.fromJson(responseMessage, responseClass);

        if (response.isCorrect())
            onCorrect.accept(response);
        else
            onIncorrect.accept(response.getDescription());
    }
}
