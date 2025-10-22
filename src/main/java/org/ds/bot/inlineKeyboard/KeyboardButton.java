package org.ds.bot.inlineKeyboard;

import org.ds.service.message.KeyboardButtonsCallbacksService;
import org.ds.service.message.MessageSenderService;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public record KeyboardButton(@NotNull String text, @NotNull String callback, Consumer<MessageSenderService> action) {
    @Contract("_, _, _ -> new")
    public static @NotNull KeyboardButton of(@NotNull String text, @NotNull String callback, Consumer<MessageSenderService> action) {
        return new KeyboardButton(text, callback, action);
    }

    public KeyboardButton addToCallbacksProcessor(@NotNull KeyboardButtonsCallbacksService keyboardButtonsCallbacksService) {
        keyboardButtonsCallbacksService.addKeyboardButton(this);

        return this;
    }
}
