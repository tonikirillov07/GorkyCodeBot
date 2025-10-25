package org.ds.bot;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.ChatMember;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.ds.bot.commands.CommandData;
import org.ds.bot.commands.CommandsProcessor;
import org.ds.bot.preparingSteps.PreparingSteps;
import org.ds.bot.states.States;
import org.ds.service.BotBlockedService;
import org.ds.service.BotStateService;
import org.ds.service.message.KeyboardButtonsCallbacksService;
import org.ds.service.message.MessageSenderService;
import org.ds.utils.Utils;
import org.ds.utils.fileReader.FileReader;
import org.ds.utils.fileReader.files.TextFiles;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BotUpdates implements UpdatesListener {
    private BotStateService botStateService;
    private CommandsProcessor commandsProcessor;
    private KeyboardButtonsCallbacksService keyboardButtonsCallbacksService;
    private PreparingSteps preparingSteps;
    private MessageSenderService messageSenderService;
    private BotBlockedService botBlockedService;

    public BotUpdates() {}

    @Override
    public int process(@NotNull List<Update> list) {
        list.forEach(this::processUpdate);

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void processUpdate(@NotNull Update update) {
        if (update.callbackQuery() != null)
            keyboardButtonsCallbacksService.processCallbacks(update.callbackQuery());

        if (botBlockedService.tryProcessBlock(update))
            return;

        Message message = update.message();
        if (message == null)
            return;

        Long chatId = message.chat().id();
        String messageText = message.text();
        String username = Utils.getUsername(message.from());
        Long userId = message.from().id();

        if ((messageText != null) && processCommands(chatId, messageText, username, userId))
            return;

        if (preparingSteps.tryPrepare(chatId, message))
            return;

        messageSenderService.sendTextMessage(chatId, FileReader.read(TextFiles.NO_COMMANDS_EXECUTING_TEXT));
    }

    private boolean processCommands(@NotNull Long chatId, @NotNull String messageText, @NotNull String username, @NotNull Long userId) {
        if (botStateService.getCurrentState() == States.EXECUTING_COMMAND)
            return false;

        if (Utils.isMessageCommand(messageText)) {
            commandsProcessor.processCommand(CommandData.of(chatId, messageText, username, userId));
            return true;
        }

        return false;
    }

    @Autowired
    public void setBotStateService(BotStateService botStateService) {
        this.botStateService = botStateService;
    }

    @Autowired
    public void setCommandsProcessor(CommandsProcessor commandsProcessor) {
        this.commandsProcessor = commandsProcessor;
    }

    @Autowired
    public void setKeyboardButtonsCallbacksService(KeyboardButtonsCallbacksService keyboardButtonsCallbacksService) {
        this.keyboardButtonsCallbacksService = keyboardButtonsCallbacksService;
    }

    @Autowired
    public void setPreparingSteps(PreparingSteps preparingSteps) {
        this.preparingSteps = preparingSteps;
    }

    @Autowired
    public void setMessageSenderService(MessageSenderService messageSenderService) {
        this.messageSenderService = messageSenderService;
    }

    @Autowired
    public void setBotBlockedService(BotBlockedService botBlockedService) {
        this.botBlockedService = botBlockedService;
    }
}
