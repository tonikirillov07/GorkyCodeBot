package org.ds.utils.fileReader.files;

public final class TextFiles {
    private static final String TEXTS_DIRECTORY = "/texts";
    private static final String COMMANDS_DIRECTORY = TEXTS_DIRECTORY + "/commands";
    private static final String CLEAR_COMMAND_DIRECTORY = COMMANDS_DIRECTORY + "/clear";
    private static final String START_COMMAND_DIRECTORY = COMMANDS_DIRECTORY + "/start";
    private static final String PROMPTS_DIRECTORY = TEXTS_DIRECTORY + "/prompts";

    public static final String WELCOME_TEXT = START_COMMAND_DIRECTORY + "/welcome.txt";

    public static final String CLEAR_TEXT = CLEAR_COMMAND_DIRECTORY + "/clearConfirmation.txt";
    public static final String CLEAR_DONE = CLEAR_COMMAND_DIRECTORY + "/clearDone.txt";

    public static final String COMMAND_CONFIRMATION_TEXT = COMMANDS_DIRECTORY + "/commandConfirmation.txt";
    public static final String COMMAND_NOT_FOUND_TEXT = COMMANDS_DIRECTORY + "/commandNotFound.txt";
    public static final String COMMAND_CANCELED_TEXT = COMMANDS_DIRECTORY + "/commandExecutionCanceled.txt";
}
