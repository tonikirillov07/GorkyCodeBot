package org.ds.bot.botConfig;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import org.ds.bot.BotExceptionHandler;
import org.ds.bot.BotUpdates;
import org.ds.bot.BotInfo;
import org.ds.service.message.MessageSenderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class BotConfig {
    @Value("${bot.username}")
    private String username;

    @Value("${bot.token}")
    private String token;

    @Bean
    public BotInfo botInfo() {
        return BotInfo.of(username, token);
    }

    @Bean
    public UpdatesListener botUpdates() {
        return new BotUpdates();
    }

    @Bean
    public BotExceptionHandler botExceptionHandler() {
        return new BotExceptionHandler();
    }

    @Bean
    public TelegramBot telegramBot(UpdatesListener botUpdates, BotExceptionHandler botExceptionHandler) {
        TelegramBot telegramBot = new TelegramBot(token);
        telegramBot.setUpdatesListener(botUpdates, botExceptionHandler);

        return telegramBot;
    }
}
