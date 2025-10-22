package org.ds.bot;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import org.ds.bot.commands.CommandData;
import org.ds.bot.commands.CommandsProcessor;
import org.ds.bot.states.States;
import org.ds.service.BotStateService;
import org.ds.service.ai.AIService;
import org.ds.service.message.KeyboardButtonsCallbacksService;
import org.ds.service.message.MessageSenderService;
import org.ds.utils.Utils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BotUpdates implements UpdatesListener {
    @Autowired
    private MessageSenderService messageSenderService;
    @Autowired
    private AIService aiService;
    @Autowired
    private CommandsProcessor commandsProcessor;
    @Autowired
    private KeyboardButtonsCallbacksService keyboardButtonsCallbacksService;
    @Autowired
    private BotStateService botStateService;


    @Override
    public int process(@NotNull List<Update> list) {
        list.forEach(this::processUpdate);

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void processUpdate(@NotNull Update update) {
        if (update.callbackQuery() != null)
            keyboardButtonsCallbacksService.processCallbacks(update.callbackQuery());

        Message message = update.message();
        if (message == null)
            return;

        if (botStateService.getCurrentState() == States.EXECUTING_COMMAND)
            return;

        Long chatId = message.chat().id();
        String messageText = message.text();
        String username = Utils.getUsername(message.from());

        messageSenderService.sendChatAction(chatId, ChatAction.typing);

        if (Utils.isMessageCommand(messageText))
            commandsProcessor.processCommand(CommandData.of(chatId, messageText, username));
    }
}
