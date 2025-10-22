package org.ds.bot.commands;

import org.ds.bot.inlineKeyboard.Keyboards;
import org.ds.bot.states.States;
import org.ds.service.BotStateService;
import org.ds.service.message.KeyboardButtonsCallbacksService;
import org.ds.service.message.MessageSenderService;
import org.ds.bot.inlineKeyboard.KeyboardButton;
import org.ds.utils.fileReader.FileReader;
import org.ds.utils.Utils;
import org.ds.utils.fileReader.Files;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class CommandsProcessor {
    private final MessageSenderService messageSenderService;
    private final KeyboardButtonsCallbacksService keyboardButtonsCallbacksService;
    private final BotStateService botStateService;

    public CommandsProcessor(MessageSenderService messageSenderService, KeyboardButtonsCallbacksService keyboardButtonsCallbacksService, BotStateService botStateService) {
        this.messageSenderService = messageSenderService;
        this.keyboardButtonsCallbacksService = keyboardButtonsCallbacksService;
        this.botStateService = botStateService;
    }

    public void processCommand(@NotNull CommandData commandData) {
        if (!Utils.isMessageCommand(commandData.command()))
            throw new IllegalArgumentException("Message %s isn't command".formatted(commandData.command()));

        botStateService.changeCurrentState(States.EXECUTING_COMMAND);

        if (botStateService.getCurrentState() != States.NONE) {
            confirmInterrupt(commandData);
            return;
        }

        executeCommand(commandData);
    }

    private void confirmInterrupt(@NotNull CommandData commandData) {
        Consumer<MessageSenderService> onConfirm = _ -> executeCommand(commandData);
        Consumer<MessageSenderService> onCancel = _ -> {
            botStateService.changeCurrentState(States.NONE);
            messageSenderService.sendMessage(commandData.chatId(), FileReader.read(Files.COMMAND_CANCELED_TEXT));
        };

        Keyboards.createConfirmation(
                commandData.chatId(),
                FileReader.read(Files.COMMAND_CONFIRMATION_TEXT),
                messageSenderService,
                keyboardButtonsCallbacksService,
                onConfirm,
                onCancel
        );
    }

    private void executeCommand(@NotNull CommandData commandData) {
        switch (commandData.command()) {
            case "/start" -> messageSenderService.sendMessage(commandData.chatId(),
                    FileReader.read(Files.WELCOME_TEXT).formatted(commandData.username()));
            case "/clear" -> {}
            default ->
                    messageSenderService.sendMessage(commandData.chatId(), FileReader.read(Files.COMMAND_NOT_FOUND_TEXT));
        }

        botStateService.changeCurrentState(States.NONE);
    }
}
