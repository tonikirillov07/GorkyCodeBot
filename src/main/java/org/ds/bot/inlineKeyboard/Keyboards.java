package org.ds.bot.inlineKeyboard;

import org.ds.service.message.KeyboardButtonsCallbacksService;
import org.ds.service.message.MessageSenderService;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class Keyboards {
    public static void createConfirmation(@NotNull Long chatId,
                                          @NotNull String message,
                                          @NotNull MessageSenderService messageSenderService,
                                          @NotNull KeyboardButtonsCallbacksService keyboardButtonsCallbacksService,
                                          @NotNull Consumer<MessageSenderService> onConfirm,
                                          @NotNull Consumer<MessageSenderService> onCancel) {
        KeyboardButton confirmButton = KeyboardButton.of("Да", "confirm_action", onConfirm);
        KeyboardButton cancelButton = KeyboardButton.of("Нет", "cancel_action", onCancel);

        KeyboardButtonGroup keyboardButtonGroup = KeyboardButtonGroup.of(confirmButton, cancelButton).addToCallbacksProcessor(keyboardButtonsCallbacksService);

        messageSenderService.sendButtonsMessage(chatId, message, keyboardButtonGroup.keyboardButtons());
    }
}
