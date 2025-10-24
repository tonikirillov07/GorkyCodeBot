package org.ds.bot.preparingSteps.steps;

import com.pengrad.telegrambot.model.request.ChatAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ds.bot.preparingSteps.UserPlacesData;
import org.ds.bot.preparingSteps.responses.ResponseProcessor;
import org.ds.bot.preparingSteps.responses.interests.InterestResponse;
import org.ds.bot.states.States;
import org.ds.service.BotStateService;
import org.ds.service.message.MessageSenderService;
import org.ds.utils.fileReader.FileReader;
import org.ds.utils.fileReader.files.TextFiles;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class InterestsStep {
    private static final Log log = LogFactory.getLog(InterestsStep.class);
    private final MessageSenderService messageSenderService;
    private final BotStateService botStateService;
    private final ResponseProcessor responseProcessor;

    public InterestsStep(MessageSenderService messageSenderService, BotStateService botStateService, ResponseProcessor responseProcessor) {
        this.messageSenderService = messageSenderService;
        this.botStateService = botStateService;
        this.responseProcessor = responseProcessor;
    }

    public boolean tryProcessInterests(@NotNull Long chatId,
                                       @NotNull String messageText,
                                       @NotNull UserPlacesData userPlacesData) {
        if (botStateService.getCurrentState() != States.REQUIRES_INTERESTS)
            return false;

        log.info("Processing interests...");

        messageSenderService.sendChatAction(chatId, ChatAction.typing);

        responseProcessor.processResponse(
                FileReader.read(TextFiles.INTERESTS_PROMPT).formatted(messageText),
                InterestResponse.class,
                response -> {
                    InterestResponse interestResponse = (InterestResponse) response;
                    userPlacesData.setInterests(interestResponse.getInterests());

                    messageSenderService.sendTextMessage(chatId, FileReader.read(TextFiles.INTERESTS_ACCEPTED_TEXT));
                    botStateService.changeCurrentState(States.REQUIRES_FREE_TIME);
                },
                description -> messageSenderService.sendTextMessage(chatId, description)
        );

        log.info("Interests processed!");

        return true;
    }
}
