package org.ds.bot;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.SendChatAction;
import org.ds.service.ai.AIService;
import org.ds.service.message.MessageSenderService;
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

        messageSenderService.sendChatAction(chatId, ChatAction.typing);

    }
}
