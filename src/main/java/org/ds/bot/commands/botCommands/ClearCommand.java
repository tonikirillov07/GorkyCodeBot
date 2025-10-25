package org.ds.bot.commands.botCommands;

import org.ds.bot.commands.AbstractCommand;
import org.ds.bot.commands.CommandData;
import org.ds.bot.inlineKeyboard.KeyboardButton;
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
    private final StartCommand startCommand;

    protected ClearCommand(@NotNull MessageSenderService messageSenderService,
                           @NotNull KeyboardButtonsCallbacksService keyboardButtonsCallbacksService,
                           @NotNull BotStateService botStateService, StartCommand startCommand) {
        super(messageSenderService, keyboardButtonsCallbacksService, botStateService);
        this.startCommand = startCommand;
    }

    @Override
    public void execute(@NotNull CommandData commandData) {
        if (botStateService().getCurrentState() == States.NONE) {
            messageSenderService().sendTextMessage(commandData.chatId(), FileReader.read(TextFiles.NOTHING_TO_CLEAR_TEXT));
            return;
        }

        super.execute(commandData);

        Consumer<MessageSenderService> onConfirm = _ -> confirmClear(commandData);

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

    private void confirmClear(@NotNull CommandData commandData) {
        KeyboardButton keyboardButton = new KeyboardButton("Начать", "start_by_start", _ ->
                startCommand.execute(commandData)
        ).addToCallbacksProcessor(keyboardButtonsCallbacksService());

        messageSenderService().sendButtonsMessage(
                commandData.chatId(),
                FileReader.read(TextFiles.CLEAR_DONE_TEXT),
                new KeyboardButton[] {keyboardButton}
        );

        botStateService().changeCurrentState(States.NONE);
    }
}
