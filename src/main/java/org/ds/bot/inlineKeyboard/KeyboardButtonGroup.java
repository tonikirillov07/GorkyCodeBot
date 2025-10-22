package org.ds.bot.inlineKeyboard;

import org.ds.service.message.KeyboardButtonsCallbacksService;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record KeyboardButtonGroup (KeyboardButton... keyboardButtons) {
    @Contract("_ -> new")
    public static @NotNull KeyboardButtonGroup of(KeyboardButton... keyboardButtons) {
        return new KeyboardButtonGroup(keyboardButtons);
    }

    @Contract("_ -> this")
    public KeyboardButtonGroup addToCallbacksProcessor(@NotNull KeyboardButtonsCallbacksService keyboardButtonsCallbacksService) {
        keyboardButtonsCallbacksService.addKeyboardButtonGroup(this);

        return this;
    }

    public Optional<KeyboardButton> findButtonByCallback(String callback) {
        for (KeyboardButton keyboardButton : keyboardButtons) {
            if (keyboardButton.callback().equals(callback))
                return Optional.of(keyboardButton);
        }

        return Optional.empty();
    }
}
