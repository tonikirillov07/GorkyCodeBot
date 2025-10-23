package org.ds.bot.preparingSteps.steps;

import com.pengrad.telegrambot.model.request.ChatAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ds.bot.preparingSteps.UserPlacesData;
import org.ds.bot.states.States;
import org.ds.service.BotStateService;
import org.ds.service.ai.AIService;
import org.ds.service.message.MessageSenderService;
import org.ds.utils.fileReader.FileReader;
import org.ds.utils.fileReader.files.TextFiles;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class FinishStep {
    private static final Log log = LogFactory.getLog(FinishStep.class);
    private final BotStateService botStateService;
    private final MessageSenderService messageSenderService;
    private final AIService aIService;

    public FinishStep(BotStateService botStateService, MessageSenderService messageSenderService, AIService aIService) {
        this.botStateService = botStateService;
        this.messageSenderService = messageSenderService;
        this.aIService = aIService;
    }

    public void processFinish(@NotNull Long chatId,
                              @NotNull UserPlacesData userPlacesData) {
        log.info("Processing finish...");

        botStateService.changeCurrentState(States.NONE);

        messageSenderService.sendTextMessage(chatId, FileReader.read(TextFiles.GEOPOSITION_ACCEPTED_TEXT));
        messageSenderService.sendChatAction(chatId, ChatAction.typing);

        if (userPlacesData.isCompleted()) {
            String prompt = FileReader.read(TextFiles.USER_PLACES_PROMPT)
                    .formatted(
                            userPlacesData.getInterestsAsString(),
                            userPlacesData.getFreeTime(),
                            userPlacesData.getGeoposition()
                    );

            messageSenderService.sendTextMessage(chatId, aIService.getResponse(prompt));
        } else
            messageSenderService.sendTextMessage(chatId, FileReader.read(TextFiles.DATA_COLLECTION_WAS_NOT_COMPLETED_TEXT)
                    .formatted(userPlacesData.getNotCompletedData()));

        log.info("Finish processed!");
    }
}
