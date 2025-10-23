package org.ds.service.message;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class MessagesDeleterService {
    private static final Log log = LogFactory.getLog(MessagesDeleterService.class);
    private final TelegramBot telegramBot;

    public MessagesDeleterService(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void deleteMessage(@NotNull Long chatId,
                              @NotNull Integer messageId) {
        DeleteMessage deleteMessage = new DeleteMessage(chatId, messageId);

        BaseResponse baseResponse = telegramBot.execute(deleteMessage);
        if (baseResponse.isOk())
            log.info("Deleted message %d from chat %d".formatted(messageId, chatId));
        else
            log.error("Failed to delete message %d from chat %d. Error Code: %d, Description: %s"
                    .formatted(messageId, chatId, baseResponse.errorCode(), baseResponse.description()));
    }
}
