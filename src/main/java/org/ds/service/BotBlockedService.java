package org.ds.service;

import com.pengrad.telegrambot.model.ChatMember;
import com.pengrad.telegrambot.model.ChatMemberUpdated;
import com.pengrad.telegrambot.model.Update;
import org.ds.bot.states.States;
import org.ds.service.db.DBService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class BotBlockedService {
    private final DBService dBService;
    private final BotStateService botStateService;

    public BotBlockedService(DBService dBService, BotStateService botStateService) {
        this.dBService = dBService;
        this.botStateService = botStateService;
    }

    public boolean tryProcessBlock(@NotNull Update update) {
        ChatMemberUpdated chatMember = update.myChatMember();

        if ((chatMember != null) && (chatMember.newChatMember().status() == ChatMember.Status.kicked))
            deleteUser(chatMember.from().id());
        else
            return false;

        return true;
    }

    private void deleteUser(@NotNull Long userId) {
        botStateService.changeCurrentState(States.NONE);
        dBService.deleteUserByUserId(userId);
    }
}
