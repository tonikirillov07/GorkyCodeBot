package org.ds.bot.commands.botCommands;

import org.ds.bot.commands.AbstractCommand;
import org.ds.bot.commands.CommandData;
import org.ds.bot.states.States;
import org.ds.service.BotStateService;
import org.ds.service.message.KeyboardButtonsCallbacksService;
import org.ds.service.message.MessageSenderService;
import org.ds.utils.fileReader.FileReader;
import org.ds.utils.fileReader.files.PhotoFiles;
import org.ds.utils.fileReader.files.TextFiles;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class StartCommand extends AbstractCommand {
    protected StartCommand(MessageSenderService messageSenderService, KeyboardButtonsCallbacksService keyboardButtonsCallbacksService, BotStateService botStateService) {
        super(messageSenderService, keyboardButtonsCallbacksService, botStateService);
    }

    @Override
    public void execute(@NotNull CommandData commandData) {
        messageSenderService().sendPhotoMessage(commandData.chatId(), PhotoFiles.MAIN_PHOTO,
                FileReader.read(TextFiles.WELCOME_TEXT).formatted(commandData.username()));

        botStateService().changeCurrentState(States.REQUIRES_INTERESTS);
    }
}
