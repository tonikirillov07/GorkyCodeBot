package org.ds.bot.preparingSteps.steps;

import com.pengrad.telegrambot.model.request.ChatAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ds.bot.preparingSteps.responses.ResponseProcessor;
import org.ds.bot.preparingSteps.responses.userPlaces.UserPlacesResponse;
import org.ds.bot.preparingSteps.userPlaces.UserPlace;
import org.ds.bot.preparingSteps.userPlaces.UserPlacesData;
import org.ds.bot.states.States;
import org.ds.db.entity.UserEntity;
import org.ds.service.BotStateService;
import org.ds.service.ai.AIService;
import org.ds.service.db.DBService;
import org.ds.service.message.KeyboardButtonsCallbacksService;
import org.ds.service.message.MessageSenderService;
import org.ds.utils.datasets.DatasetsLinks;
import org.ds.utils.fileReader.FileReader;
import org.ds.utils.fileReader.files.TextFiles;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class FinishStep {
    private static final Log log = LogFactory.getLog(FinishStep.class);
    private final BotStateService botStateService;
    private final MessageSenderService messageSenderService;
    private final AIService aIService;
    private final DBService dBService;
    private final ResponseProcessor responseProcessor;
    private final KeyboardButtonsCallbacksService keyboardButtonsCallbacksService;

    public FinishStep(@NotNull BotStateService botStateService,
                      @NotNull MessageSenderService messageSenderService,
                      @NotNull AIService aIService,
                      @NotNull DBService dBService, ResponseProcessor responseProcessor, KeyboardButtonsCallbacksService keyboardButtonsCallbacksService) {
        this.botStateService = botStateService;
        this.messageSenderService = messageSenderService;
        this.aIService = aIService;
        this.dBService = dBService;
        this.responseProcessor = responseProcessor;
        this.keyboardButtonsCallbacksService = keyboardButtonsCallbacksService;
    }

    public void processFinish(@NotNull Long userId,
                              @NotNull Long chatId,
                              @NotNull UserPlacesData userPlacesData) {
        log.info("Processing finish...");

        botStateService.changeCurrentState(States.NONE);

        messageSenderService.sendTextMessage(chatId, FileReader.read(TextFiles.GEOPOSITION_ACCEPTED_TEXT));
        messageSenderService.sendChatAction(chatId, ChatAction.typing);

        if (userPlacesData.isCompleted()) {
            botStateService.changeCurrentState(States.GENERATING_THOUGHTS);

            String prompt = FileReader.read(TextFiles.USER_PLACES_PROMPT)
                    .formatted(
                            DatasetsLinks.DATASET_1,
                            userPlacesData.getInterestsAsString(),
                            userPlacesData.getFreeTime(),
                            userPlacesData.getGeoposition()
                    );

            updateUser(userId);
            botStateService.changeCurrentState(States.NONE);

            responseProcessor.processResponse(
                    prompt,
                    UserPlacesResponse.class,
                    response -> {
                        UserPlacesResponse userPlacesResponse = (UserPlacesResponse) response;

                        messageSenderService.sendButtonsMessage(
                                chatId,
                                userPlacesResponse.getResponseAsString(),
                                userPlacesResponse.getPlacesButtons(chatId, keyboardButtonsCallbacksService));
                    },
                    description -> messageSenderService.sendTextMessage(chatId, description));
        } else
            messageSenderService.sendTextMessage(chatId, FileReader.read(TextFiles.DATA_COLLECTION_WAS_NOT_COMPLETED_TEXT)
                    .formatted(userPlacesData.getNotCompletedData()));

        log.info("Finish processed!");
    }

    private void updateUser(@NotNull Long userId) {
        UserEntity user = dBService.getUserByUserId(userId);
        if (user == null) {
            log.error("User with id %d was not found".formatted(userId));
            return;
        }

        user.setGotResult(true);

        dBService.updateUser(user);
    }
}
