package org.ds.bot.commands;

import org.jetbrains.annotations.NotNull;

public final class Commands {
    public static final String START = "/start";
    public static final String CLEAR = "/clear";

    public static final String[] IGNORE_INTERRUPT_CONFIRMATION_COMMANDS = {START};

    public static boolean isCommandIgnoreInterruptConfirmation(@NotNull String command) {
        for (String ignoreInterruptConfirmationCommand : IGNORE_INTERRUPT_CONFIRMATION_COMMANDS) {
            if (ignoreInterruptConfirmationCommand.equals(command))
                return true;
        }

        return false;
    }
}
