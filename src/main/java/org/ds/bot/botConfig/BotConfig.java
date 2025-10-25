package org.ds.bot.botConfig;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import org.ds.bot.BotExceptionHandler;
import org.ds.bot.BotUpdates;
import org.ds.bot.BotInfo;
import org.ds.bot.commands.CommandsProcessor;
import org.ds.bot.preparingSteps.PreparingSteps;
import org.ds.service.BotBlockedService;
import org.ds.service.BotStateService;
import org.ds.service.message.KeyboardButtonsCallbacksService;
import org.ds.service.message.MessageSenderService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class BotConfig {
    @Value("${bot.username}")
    private String username;

    @Value("${bot.token}")
    private String token;

    @Value("${bot.say_hello_message_interval}")
    private Float sayHelloInterval;

    @Bean
    public BotInfo botInfo() {
        return BotInfo.of(username, token, sayHelloInterval);
    }

    @Bean
    public UpdatesListener botUpdates(BotBlockedService botBlockedService) {
        return new BotUpdates(botBlockedService);
    }

    @Bean
    public BotExceptionHandler botExceptionHandler() {
        return new BotExceptionHandler();
    }

    @Bean
    public TelegramBot telegramBot(@NotNull UpdatesListener botUpdates,
                                   @NotNull BotExceptionHandler botExceptionHandler) {
        TelegramBot telegramBot = new TelegramBot(token);
        telegramBot.setUpdatesListener(botUpdates, botExceptionHandler);

        return telegramBot;
    }
}
