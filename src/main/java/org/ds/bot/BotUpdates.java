package org.ds.bot;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import org.ds.bot.commands.CommandsProcessor;
import org.ds.service.ai.AIService;
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

    @Override
    public int process(@NotNull List<Update> list) {
        list.forEach(this::processUpdate);

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void processUpdate(@NotNull Update update) {
        Message message = update.message();
        if (message == null)
            return;

        Long chatId = message.chat().id();
        String messageText = message.text();
        String username = Utils.getUsername(message.from());

        messageSenderService.sendChatAction(chatId, ChatAction.typing);

        if (Utils.isMessageCommand(messageText))
            commandsProcessor.processCommand(chatId, messageText, username);
    }
}
