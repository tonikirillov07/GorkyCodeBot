package org.ds.bot;

import org.ds.bot.states.States;
import org.ds.service.BotStateService;
import org.ds.service.message.MessageSenderService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class PreparingSteps {
    private final BotStateService botStateService;
    private final MessageSenderService messageSenderService;

    public PreparingSteps(BotStateService botStateService, MessageSenderService messageSenderService) {
        this.botStateService = botStateService;
        this.messageSenderService = messageSenderService;
    }

    public void prepare(@NotNull Long chatId, @NotNull String messageText) {
        if (processInterests(chatId, messageText))
            return;

        if (processFreeTime(chatId, messageText))
            return;

        if (processGeoposition(chatId, messageText))
            return;
    }

    private boolean processGeoposition(@NotNull Long chatId, @NotNull String messageText) {
        if (botStateService.getCurrentState() != States.REQUIRES_GEOPOSITION)
            return false;

        return true;
    }

    private boolean processFreeTime(@NotNull Long chatId, @NotNull String messageText) {
        if (botStateService.getCurrentState() != States.REQUIRES_FREE_TIME)
            return false;

        messageSenderService.sendTextMessage(chatId, "Отлично, и последнее, укажите ваше текущее местоположение");

        botStateService.changeCurrentState(States.REQUIRES_GEOPOSITION);

        return true;
    }

    private boolean processInterests(@NotNull Long chatId, @NotNull String messageText) {
        if (botStateService.getCurrentState() != States.REQUIRES_INTERESTS)
            return false;

        messageSenderService.sendTextMessage(chatId, "Хорошо, я учел ваши интересы");
        messageSenderService.sendTextMessage(chatId, "Теперь скажите, сколько у вас есть свободного времени?");

        botStateService.changeCurrentState(States.REQUIRES_FREE_TIME);

        return true;
    }
}
