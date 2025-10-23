package org.ds.bot.preparingSteps;

import com.google.gson.*;
import com.pengrad.telegrambot.model.Location;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ChatAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ds.bot.preparingSteps.responses.Response;
import org.ds.bot.preparingSteps.responses.freeTime.FreeTimeResponse;
import org.ds.bot.preparingSteps.responses.geoposition.GeopositionResponse;
import org.ds.bot.preparingSteps.responses.interests.InterestResponse;
import org.ds.bot.states.States;
import org.ds.service.BotStateService;
import org.ds.service.ai.AIService;
import org.ds.service.message.MessageSenderService;
import org.ds.utils.fileReader.FileReader;
import org.ds.utils.fileReader.files.TextFiles;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@Component
public class PreparingSteps {
    private static final Log log = LogFactory.getLog(PreparingSteps.class);
    private final BotStateService botStateService;
    private final MessageSenderService messageSenderService;
    private final AIService aIService;
    private final UserPlacesData userPlacesData;

    public PreparingSteps(BotStateService botStateService, MessageSenderService messageSenderService, AIService aIService) {
        this.botStateService = botStateService;
        this.messageSenderService = messageSenderService;
        this.aIService = aIService;
        this.userPlacesData = new UserPlacesData();
    }

    public void prepare(@NotNull Long chatId, @NotNull Message message) {
        String messageText = message.text();

        if (processInterests(chatId, messageText))
            return;

        if (processFreeTime(chatId, messageText))
            return;

        if (processGeoposition(chatId, message))
            return;
    }

    private boolean processGeoposition(@NotNull Long chatId, @NotNull Message message) {
        if (botStateService.getCurrentState() != States.REQUIRES_GEOPOSITION)
            return false;

        AtomicReference<String> currentLocation = new AtomicReference<>();
        Location location = message.location();

        if (location == null) {
            processResponse(
                    chatId,
                    FileReader.read(TextFiles.GEOPOSITION_PROMPT).formatted(message.text()),
                    GeopositionResponse.class,
                    response -> {
                        GeopositionResponse geopositionResponse = (GeopositionResponse) response;
                        currentLocation.set(geopositionResponse.getUserLocation());

                        acceptGeoposition(chatId, currentLocation.get());
                    },
                    description -> messageSenderService.sendTextMessage(chatId, description)
            );
        } else {
            currentLocation.set("lat: %f, lon: %f".formatted(location.latitude(), location.longitude()));
            acceptGeoposition(chatId, currentLocation.get());
        }

        return true;
    }

    private void acceptGeoposition(@NotNull Long chatId, @NotNull String currentLocation) {
        botStateService.changeCurrentState(States.NONE);
        userPlacesData.setGeoposition(currentLocation);

        processFinish(chatId);
    }

    private void processFinish(@NotNull Long chatId) {
        messageSenderService.sendTextMessage(chatId, FileReader.read(TextFiles.GEOPOSITION_ACCEPTED_TEXT));
        botStateService.changeCurrentState(States.NONE);

        if (userPlacesData.isCompleted())
            messageSenderService.sendTextMessage(chatId, userPlacesData.toString());
        else
            messageSenderService.sendTextMessage(chatId, "Сбор данных не был завершен корректно");
    }

    private boolean processFreeTime(@NotNull Long chatId, @NotNull String messageText) {
        if (botStateService.getCurrentState() != States.REQUIRES_FREE_TIME)
            return false;

        messageSenderService.sendChatAction(chatId, ChatAction.typing);

        processResponse(
                chatId,
                FileReader.read(TextFiles.FREE_TIME_PROMPT).formatted(messageText),
                FreeTimeResponse.class,
                response -> {
                    FreeTimeResponse freeTimeResponse = (FreeTimeResponse) response;
                    userPlacesData.setFreeTime(freeTimeResponse.getFreeTime());

                    messageSenderService.sendTextMessage(chatId, FileReader.read(TextFiles.FREE_TIME_ACCEPTED_TEXT));
                    botStateService.changeCurrentState(States.REQUIRES_GEOPOSITION);
                },
                description -> messageSenderService.sendTextMessage(chatId, description)
        );

        return true;
    }

    private boolean processInterests(@NotNull Long chatId, @NotNull String messageText) {
        if (botStateService.getCurrentState() != States.REQUIRES_INTERESTS)
            return false;

        messageSenderService.sendChatAction(chatId, ChatAction.typing);

        processResponse(
                chatId,
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

        return true;
    }

    private <T extends Response> void processResponse(@NotNull Long chatId,
                                     @NotNull String prompt,
                                     Class<T> responseClass,
                                     Consumer<Response> onCorrect,
                                     Consumer<String> onIncorrect) {
        String responseMessage = aIService.getResponse(prompt);

        log.info("Response: %s".formatted(responseMessage));

        Gson gson = new Gson();
        Response response = gson.fromJson(responseMessage, responseClass);

        if (response.isCorrect())
            onCorrect.accept(response);
        else
            onIncorrect.accept(response.getDescription());
    }
}
