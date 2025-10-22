package org.ds.service.message.config;

import com.pengrad.telegrambot.TelegramBot;
import org.ds.service.message.MessageSenderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageConfig {
    @Bean
    public MessageSenderService messageSenderService(TelegramBot telegramBot) {
        return new MessageSenderService(telegramBot);
    }
}
