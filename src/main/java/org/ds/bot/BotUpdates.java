package org.ds.bot;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.ds.bot.commands.CommandData;
import org.ds.bot.commands.CommandsProcessor;
import org.ds.bot.preparingSteps.PreparingSteps;
import org.ds.bot.states.States;
import org.ds.service.BotStateService;
import org.ds.service.message.KeyboardButtonsCallbacksService;
import org.ds.utils.Utils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BotUpdates implements UpdatesListener {
    @Autowired
    private CommandsProcessor commandsProcessor;
    @Autowired
    private KeyboardButtonsCallbacksService keyboardButtonsCallbacksService;
    @Autowired
    private BotStateService botStateService;
    @Autowired
    private PreparingSteps preparingSteps;

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

        Long chatId = message.chat().id();
        String messageText = message.text();
        String username = Utils.getUsername(message.from());

        if ((messageText != null) && processCommands(chatId, messageText, username))
            return;

        preparingSteps.prepare(chatId, message);
    }

    private boolean processCommands(@NotNull Long chatId,
                                 @NotNull String messageText,
                                 @NotNull String username) {
        if (botStateService.getCurrentState() == States.EXECUTING_COMMAND)
            return false;

        if (Utils.isMessageCommand(messageText)) {
            commandsProcessor.processCommand(CommandData.of(chatId, messageText, username));
            return true;
        }

        return false;
    }
}
