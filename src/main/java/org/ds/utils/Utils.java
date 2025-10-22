package org.ds.utils;

import com.pengrad.telegrambot.model.User;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class Utils {
    /**
     * @param user user info
     * @return username in <b>Firstname Surname</b>-format
     */
    public static @NotNull String getUsername(@NotNull User user) {
        return user.firstName() + (user.lastName() == null ? "" : " " + user.lastName());
    }

    /**
     * Checks is message command
     * @param message message
     * @return <b>true</b> - if message has format like <i>/command</i>.
     * Does not contain spaces and _-symbol. Else returns <b>false</b>
     */
    @Contract(pure = true)
    public static boolean isMessageCommand(@NotNull String message) {
        return message.startsWith("/") && !message.contains(" ") && !message.contains("_");
    }
}
