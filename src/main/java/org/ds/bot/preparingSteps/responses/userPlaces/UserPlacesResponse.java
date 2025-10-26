package org.ds.bot.preparingSteps.responses.userPlaces;

import org.ds.bot.inlineKeyboard.KeyboardButton;
import org.ds.bot.preparingSteps.responses.Response;
import org.ds.bot.preparingSteps.userPlaces.UserPlace;
import org.ds.service.message.KeyboardButtonsCallbacksService;
import org.ds.utils.fileReader.FileReader;
import org.ds.utils.fileReader.files.TextFiles;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UserPlacesResponse extends Response {
    private final UserPlace[] places;

    protected UserPlacesResponse(@NotNull Boolean isCorrect, @Nullable String description, UserPlace[] places) {
        super(isCorrect, description);
        this.places = places;
    }

    public KeyboardButton[] getPlacesButtons(@NotNull Long chatId,
                                             @NotNull KeyboardButtonsCallbacksService keyboardButtonsCallbacksService) {
        KeyboardButton[] keyboardButtons = new KeyboardButton[places.length];

        for (int i = 0; i < places.length; i++) {
            UserPlace userPlace = places[i];

            KeyboardButton keyboardButton = KeyboardButton.of(
                    userPlace.name(),
                    "user_place_location_" + i,
                    messageSenderService ->
                            messageSenderService.sendLocation(chatId, userPlace.lat(), userPlace.lon())
            ).addToCallbacksProcessor(keyboardButtonsCallbacksService);
            keyboardButtons[i] = keyboardButton;
        }

        return keyboardButtons;
    }

    public String getResponseAsString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < places.length; i++) {
            UserPlace currentPlace = places[i];

            String text = FileReader.read(TextFiles.USER_PLACES_RESULT_TEXT);
            text = text.formatted(
                    i + 1,
                    currentPlace.name(),
                    currentPlace.description(),
                    currentPlace.distance(),
                    currentPlace.duration()
            );

            stringBuilder.append(text).append("\n");
        }

        return stringBuilder.toString();
    }

    public UserPlace[] getPlaces() {
        return places;
    }
}
