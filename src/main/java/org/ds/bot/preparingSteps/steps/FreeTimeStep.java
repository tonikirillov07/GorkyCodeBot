package org.ds.bot.preparingSteps.steps;

import com.pengrad.telegrambot.model.request.ChatAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ds.bot.preparingSteps.UserPlacesData;
import org.ds.bot.preparingSteps.responses.ResponseProcessor;
import org.ds.bot.preparingSteps.responses.freeTime.FreeTimeResponse;
import org.ds.bot.states.States;
import org.ds.service.BotStateService;
import org.ds.service.message.MessageSenderService;
import org.ds.utils.fileReader.FileReader;
import org.ds.utils.fileReader.files.TextFiles;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class FreeTimeStep {
    private static final Log log = LogFactory.getLog(FreeTimeStep.class);
    private final BotStateService botStateService;
    private final MessageSenderService messageSenderService;
    private final ResponseProcessor responseProcessor;

    public FreeTimeStep(BotStateService botStateService, MessageSenderService messageSenderService, ResponseProcessor responseProcessor) {
        this.botStateService = botStateService;
        this.messageSenderService = messageSenderService;
        this.responseProcessor = responseProcessor;
    }

    public boolean tryProcessFreeTime(@NotNull Long chatId,
                                      @NotNull String messageText,
                                      @NotNull UserPlacesData userPlacesData) {
        if (botStateService.getCurrentState() != States.REQUIRES_FREE_TIME)
            return false;

        log.info("Processing free time...");

        messageSenderService.sendChatAction(chatId, ChatAction.typing);
        botStateService.changeCurrentState(States.GENERATING_THOUGHTS);

        responseProcessor.processResponse(
                FileReader.read(TextFiles.FREE_TIME_PROMPT).formatted(messageText),
                FreeTimeResponse.class,
                response -> {
                    FreeTimeResponse freeTimeResponse = (FreeTimeResponse) response;
                    userPlacesData.setFreeTime(freeTimeResponse.getFreeTime());

                    messageSenderService.sendTextMessage(chatId, FileReader.read(TextFiles.FREE_TIME_ACCEPTED_TEXT));
                    botStateService.changeCurrentState(States.REQUIRES_GEOPOSITION);
                },
                description -> {
                    messageSenderService.sendTextMessage(chatId, description);
                    botStateService.changeCurrentState(States.REQUIRES_FREE_TIME);
                }
        );

        log.info("Free time processed!");

        return true;
    }
}
