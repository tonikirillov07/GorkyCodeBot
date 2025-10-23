package org.ds.bot.commands.botCommands;

import org.ds.bot.commands.AbstractCommand;
import org.ds.bot.commands.CommandData;
import org.ds.bot.inlineKeyboard.Keyboards;
import org.ds.bot.states.States;
import org.ds.service.BotStateService;
import org.ds.service.message.KeyboardButtonsCallbacksService;
import org.ds.service.message.MessageSenderService;
import org.ds.utils.fileReader.FileReader;
import org.ds.utils.fileReader.files.TextFiles;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class ClearCommand extends AbstractCommand {
    protected ClearCommand(MessageSenderService messageSenderService, KeyboardButtonsCallbacksService keyboardButtonsCallbacksService, BotStateService botStateService) {
        super(messageSenderService, keyboardButtonsCallbacksService, botStateService);
    }

    @Override
    public void execute(@NotNull CommandData commandData) {
        Consumer<MessageSenderService> onConfirm = messageSenderService -> {
            messageSenderService.sendTextMessage(commandData.chatId(), FileReader.read(TextFiles.CLEAR_DONE));
            botStateService().changeCurrentState(States.NONE);
        };

        Consumer<MessageSenderService> onCancel = messageSenderService -> {
            messageSenderService.sendTextMessage(commandData.chatId(), FileReader.read(TextFiles.COMMAND_CANCELED_TEXT));
            botStateService().changeCurrentState(States.NONE);
        };

        Keyboards.createConfirmation(
                commandData.chatId(),
                FileReader.read(TextFiles.CLEAR_TEXT),
                messageSenderService(),
                keyboardButtonsCallbacksService(),
                onConfirm,
                onCancel
        );
    }
}
