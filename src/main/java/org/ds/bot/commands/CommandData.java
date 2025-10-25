package org.ds.bot.commands;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record CommandData(@NotNull Long chatId,
                          @NotNull String command,
                          @NotNull String username,
                          @NotNull Long userId) {
    @Contract("_, _, _, _ -> new")
    public static @NotNull CommandData of(@NotNull Long chatId,
                                          @NotNull String command,
                                          @NotNull String username,
                                          @NotNull Long userId) {
        return new CommandData(chatId, command, username, userId);
    }

    @Contract("_, _, _ -> new")
    public static @NotNull CommandData of(@NotNull Long chatId,
                                          @NotNull String username,
                                          @NotNull Long userId) {
        return new CommandData(chatId, "NO_COMMAND", username, userId);
    }

    public boolean isIgnoreInterruptConfirmation() {
        return Commands.isCommandIgnoreInterruptConfirmation(command);
    }
}
