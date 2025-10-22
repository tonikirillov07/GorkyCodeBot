package org.ds.utils.fileReader;

public final class Files {
    private static final String TEXTS_DIRECTORY = "/texts";
    private static final String COMMANDS_DIRECTORY = TEXTS_DIRECTORY + "/commands";

    public static final String WELCOME_TEXT = TEXTS_DIRECTORY + "/welcome.txt";
    public static final String COMMAND_CONFIRMATION_TEXT = COMMANDS_DIRECTORY + "/commandConfirmation.txt";
    public static final String COMMAND_NOT_FOUND_TEXT = COMMANDS_DIRECTORY + "/commandNotFound.txt";
    public static final String COMMAND_CANCELED_TEXT = COMMANDS_DIRECTORY + "/commandExecutionCanceled.txt";
}
