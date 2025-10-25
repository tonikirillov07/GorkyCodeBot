package org.ds.bot.commands;

import org.ds.bot.commands.botCommands.ClearCommand;
import org.ds.bot.commands.botCommands.StartCommand;
import org.ds.bot.inlineKeyboard.Keyboards;
import org.ds.bot.states.States;
import org.ds.service.BotStateService;
import org.ds.service.message.KeyboardButtonsCallbacksService;
import org.ds.service.message.MessageSenderService;
import org.ds.utils.fileReader.FileReader;
import org.ds.utils.Utils;
import org.ds.utils.fileReader.files.PhotoFiles;
import org.ds.utils.fileReader.files.TextFiles;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class CommandsProcessor {
    private final MessageSenderService messageSenderService;
    private final KeyboardButtonsCallbacksService keyboardButtonsCallbacksService;
    private final BotStateService botStateService;

    @Autowired
    private StartCommand startCommand;
    @Autowired
    private ClearCommand clearCommand;

    public CommandsProcessor(MessageSenderService messageSenderService,
                             KeyboardButtonsCallbacksService keyboardButtonsCallbacksService,
                             BotStateService botStateService) {
        this.messageSenderService = messageSenderService;
        this.keyboardButtonsCallbacksService = keyboardButtonsCallbacksService;
        this.botStateService = botStateService;
    }

    public void processCommand(@NotNull CommandData commandData) {
        if (!Utils.isMessageCommand(commandData.command()))
            throw new IllegalArgumentException("Message %s isn't command".formatted(commandData.command()));

        if ((botStateService.getCurrentState() != States.NONE) && !commandData.isIgnoreInterruptConfirmation()) {
            confirmInterrupt(commandData);
            return;
        }

        executeCommand(commandData);
    }

    private void confirmInterrupt(@NotNull CommandData commandData) {
        Consumer<MessageSenderService> onConfirm = _ -> executeCommand(commandData);
        Consumer<MessageSenderService> onCancel = _ -> {
            botStateService.changeCurrentState(States.NONE);
            messageSenderService.sendTextMessage(commandData.chatId(), FileReader.read(TextFiles.COMMAND_CANCELED_TEXT));
        };

        Keyboards.createConfirmation(
                commandData.chatId(),
                FileReader.read(TextFiles.COMMAND_CONFIRMATION_TEXT),
                messageSenderService,
                keyboardButtonsCallbacksService,
                onConfirm,
                onCancel
        );
    }

    private void executeCommand(@NotNull CommandData commandData) {
        switch (commandData.command()) {
            case Commands.START -> startCommand.execute(commandData);
            case Commands.CLEAR -> clearCommand.execute(commandData);
            default -> commandNotFound(commandData.chatId());
        }
    }

    private void commandNotFound(@NotNull Long chatId) {
        messageSenderService.sendTextMessage(chatId, FileReader.read(TextFiles.COMMAND_NOT_FOUND_TEXT));
        botStateService.changeCurrentState(States.NONE);
    }
}
