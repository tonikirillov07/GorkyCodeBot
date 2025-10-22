package org.ds.bot.commands;

import org.ds.service.message.MessageSenderService;
import org.ds.utils.fileReader.FileReader;
import org.ds.utils.Utils;
import org.ds.utils.fileReader.Files;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class CommandsProcessor {
    private final MessageSenderService messageSenderService;

    public CommandsProcessor(MessageSenderService messageSenderService) {
        this.messageSenderService = messageSenderService;
    }

    public void processCommand(@NotNull Long chatId, @NotNull String command, String username) {
        if (!Utils.isMessageCommand(command))
            throw new IllegalArgumentException("Message %s isn't command".formatted(command));

        switch (command) {
            case "/start" -> messageSenderService.sendMessage(chatId,
                    FileReader.read(Files.WELCOME_TEXT).formatted(username));
            case "/clear" -> {}
        }
    }
}
