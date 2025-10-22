package org.ds.bot;

import com.pengrad.telegrambot.ExceptionHandler;
import com.pengrad.telegrambot.TelegramException;
import com.pengrad.telegrambot.response.BaseResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class BotExceptionHandler implements ExceptionHandler {
    private static final Log log = LogFactory.getLog(BotExceptionHandler.class);

    @Override
    public void onException(@NotNull TelegramException e) {
        BaseResponse baseResponse = e.response();

        if (baseResponse == null)
            log.error("Exception occurred: %s".formatted(e.getMessage()));
        else
            log.error("Exception occurred. Error code: %d, Description: %s"
                    .formatted(baseResponse.errorCode(), baseResponse.description()));
    }
}
