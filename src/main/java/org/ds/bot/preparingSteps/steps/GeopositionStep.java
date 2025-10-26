package org.ds.bot.preparingSteps.steps;

import com.pengrad.telegrambot.model.Location;
import com.pengrad.telegrambot.model.Message;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ds.bot.preparingSteps.userPlaces.UserPlacesData;
import org.ds.bot.preparingSteps.responses.ResponseProcessor;
import org.ds.bot.preparingSteps.responses.geoposition.GeopositionResponse;
import org.ds.bot.states.States;
import org.ds.service.BotStateService;
import org.ds.service.message.MessageSenderService;
import org.ds.utils.fileReader.FileReader;
import org.ds.utils.fileReader.files.TextFiles;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class GeopositionStep {
    private static final Log log = LogFactory.getLog(GeopositionStep.class);
    private final BotStateService botStateService;
    private final MessageSenderService messageSenderService;
    private final ResponseProcessor responseProcessor;

    public GeopositionStep(BotStateService botStateService, MessageSenderService messageSenderService, ResponseProcessor responseProcessor) {
        this.botStateService = botStateService;
        this.messageSenderService = messageSenderService;
        this.responseProcessor = responseProcessor;
    }

    public boolean tryProcessGeoposition(@NotNull Long chatId,
                                         @NotNull Message message,
                                         @NotNull UserPlacesData userPlacesData) {
        log.info("Processing geoposition...");

        if (botStateService.getCurrentState() != States.REQUIRES_GEOPOSITION)
            return false;

        AtomicBoolean returnValue = new AtomicBoolean(true);

        AtomicReference<String> currentLocation = new AtomicReference<>();
        Location location = message.location();

        if (location == null) {
            botStateService.changeCurrentState(States.GENERATING_THOUGHTS);

            responseProcessor.processResponse(
                    FileReader.read(TextFiles.GEOPOSITION_PROMPT).formatted(message.text()),
                    GeopositionResponse.class,
                    response -> {
                        GeopositionResponse geopositionResponse = (GeopositionResponse) response;
                        currentLocation.set(geopositionResponse.getUserLocation());

                        applyGeoposition(currentLocation.get(), userPlacesData);
                        returnValue.set(true);

                        botStateService.changeCurrentState(States.NONE);
                    },
                    description -> {
                        botStateService.changeCurrentState(States.REQUIRES_GEOPOSITION);
                        messageSenderService.sendTextMessage(chatId, description);

                        returnValue.set(false);
                    }
            );
        } else {
            currentLocation.set("lat: %f, lon: %f".formatted(location.latitude(), location.longitude()));

            applyGeoposition(currentLocation.get(), userPlacesData);
            returnValue.set(true);

            botStateService.changeCurrentState(States.NONE);
        }

        return returnValue.get();
    }

    private void applyGeoposition(@NotNull String currentLocation,
                                  @NotNull UserPlacesData userPlacesData) {
        userPlacesData.setGeoposition(currentLocation);

        log.info("Geoposition applied!");
    }
}
