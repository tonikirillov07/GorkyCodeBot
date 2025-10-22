package org.ds.bot;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record BotInfo(@NotNull String username, @NotNull String token) {
    @Contract("_, _ -> new")
    public static @NotNull BotInfo of(@NotNull String username, @NotNull String token) {
        return new BotInfo(username, token);
    }
}
