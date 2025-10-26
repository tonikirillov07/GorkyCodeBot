package org.ds.bot.preparingSteps;

import com.pengrad.telegrambot.model.Message;
import org.ds.bot.preparingSteps.steps.FinishStep;
import org.ds.bot.preparingSteps.steps.FreeTimeStep;
import org.ds.bot.preparingSteps.steps.GeopositionStep;
import org.ds.bot.preparingSteps.steps.InterestsStep;
import org.ds.bot.preparingSteps.userPlaces.UserPlacesData;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class PreparingSteps {
    private final UserPlacesData userPlacesData;
    private final GeopositionStep geopositionStep;
    private final FinishStep finishStep;
    private final FreeTimeStep freeTimeStep;
    private final InterestsStep interestsStep;

    public PreparingSteps(@NotNull GeopositionStep geopositionStep,
                          @NotNull FinishStep finishStep,
                          @NotNull FreeTimeStep freeTimeStep, InterestsStep interestsStep) {
        this.geopositionStep = geopositionStep;
        this.finishStep = finishStep;
        this.freeTimeStep = freeTimeStep;
        this.interestsStep = interestsStep;
        this.userPlacesData = new UserPlacesData();
    }

    public boolean tryPrepare(@NotNull Long chatId, @NotNull Message message) {
        String messageText = message.text();

        if (interestsStep.tryProcessInterests(chatId, messageText, userPlacesData))
            return true;

        if (freeTimeStep.tryProcessFreeTime(chatId, messageText, userPlacesData))
            return true;

        if (geopositionStep.tryProcessGeoposition(chatId, message, userPlacesData)) {
            finishStep.processFinish(chatId, message.from().id(), userPlacesData);

            return true;
        }

        return false;
    }
}
