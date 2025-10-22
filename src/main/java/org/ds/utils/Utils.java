package org.ds.utils;

import com.pengrad.telegrambot.model.User;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class Utils {
    public static @NotNull String getUsername(@NotNull User user) {
        return user.firstName() + (user.lastName() == null ? "" : user.lastName());
    }

    @Contract(pure = true)
    public static boolean isMessageCommand(@NotNull String message) {
        return message.startsWith("/");
    }
}
