package org.ds.bot.commands;

import org.ds.bot.states.States;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record CommandData(@NotNull Long chatId,
                          @NotNull String command,
                          @NotNull String username) {
    @Contract("_, _, _ -> new")
    public static @NotNull CommandData of(@NotNull Long chatId,
                                          @NotNull String command,
                                          @NotNull String username) {
        return new CommandData(chatId, command, username);
    }
}
