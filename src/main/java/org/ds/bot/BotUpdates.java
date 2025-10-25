package org.ds.bot;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ds.bot.commands.CommandData;
import org.ds.bot.commands.CommandsProcessor;
import org.ds.bot.preparingSteps.PreparingSteps;
import org.ds.bot.states.States;
import org.ds.service.BotBlockedService;
import org.ds.service.BotStateService;
import org.ds.service.message.KeyboardButtonsCallbacksService;
import org.ds.service.message.MessageSenderService;
import org.ds.service.message.MessagesDeleterService;
import org.ds.utils.Utils;
import org.ds.utils.fileReader.FileReader;
import org.ds.utils.fileReader.files.TextFiles;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BotUpdates implements UpdatesListener {
    private static final Log log = LogFactory.getLog(BotUpdates.class);
    private BotStateService botStateService;
    private CommandsProcessor commandsProcessor;
    private KeyboardButtonsCallbacksService keyboardButtonsCallbacksService;
    private PreparingSteps preparingSteps;
    private MessageSenderService messageSenderService;
    private final BotBlockedService botBlockedService;
    private MessagesDeleterService messagesDeleterService;

    public BotUpdates(BotBlockedService botBlockedService) {
        this.botBlockedService = botBlockedService;
    }

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

        if (botStateService.getCurrentState() == States.GENERATING_THOUGHTS) {
            messagesDeleterService.deleteMessage(chatId, message.messageId());
            return;
        }

        if (messageText != null)
            log.info("User %s sent message: %s".formatted(username, messageText));

        boolean isCommand = (messageText != null) && tryProcessCommands(chatId, messageText, username, userId);
        if (isCommand)
            return;

        if (preparingSteps.tryPrepare(chatId, message))
            return;

        messageSenderService.sendTextMessage(chatId, FileReader.read(TextFiles.NO_COMMANDS_EXECUTING_TEXT));
    }

    private boolean tryProcessCommands(@NotNull Long chatId,
                                       @NotNull String messageText,
                                       @NotNull String username,
                                       @NotNull Long userId) {
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
    public void setMessagesDeleterService(MessagesDeleterService messagesDeleterService) {
        this.messagesDeleterService = messagesDeleterService;
    }
}
