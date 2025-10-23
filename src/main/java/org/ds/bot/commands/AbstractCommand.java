package org.ds.bot.commands;

import org.ds.service.BotStateService;
import org.ds.service.message.KeyboardButtonsCallbacksService;
import org.ds.service.message.MessageSenderService;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractCommand {
    private final MessageSenderService messageSenderService;
    private final KeyboardButtonsCallbacksService keyboardButtonsCallbacksService;
    private final BotStateService botStateService;

    protected AbstractCommand(MessageSenderService messageSenderService, KeyboardButtonsCallbacksService keyboardButtonsCallbacksService, BotStateService botStateService) {
        this.messageSenderService = messageSenderService;
        this.keyboardButtonsCallbacksService = keyboardButtonsCallbacksService;
        this.botStateService = botStateService;
    }

    public abstract void execute(@NotNull CommandData commandData);

    public MessageSenderService messageSenderService() {
        return messageSenderService;
    }

    public BotStateService botStateService() {
        return botStateService;
    }

    public KeyboardButtonsCallbacksService keyboardButtonsCallbacksService() {
        return keyboardButtonsCallbacksService;
    }
}
