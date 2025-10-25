package org.ds.utils.fileReader.files;

public final class TextFiles {
    private static final String TEXTS_DIRECTORY = "/texts";
    private static final String COMMANDS_DIRECTORY = TEXTS_DIRECTORY + "/commands";
    private static final String CLEAR_COMMAND_DIRECTORY = COMMANDS_DIRECTORY + "/clear";
    private static final String START_COMMAND_DIRECTORY = COMMANDS_DIRECTORY + "/start";
    private static final String PROMPTS_DIRECTORY = TEXTS_DIRECTORY + "/prompts";
    private static final String STEPS_DIRECTORY = TEXTS_DIRECTORY + "/steps";
    private static final String INTEREST_STEP_DIRECTORY = STEPS_DIRECTORY + "/interests";
    private static final String FREE_TIME_STEP_DIRECTORY = STEPS_DIRECTORY + "/freeTime";
    private static final String GEOPOSITION_STEP_DIRECTORY = STEPS_DIRECTORY + "/geoposition";

    public static final String DATA_COLLECTION_WAS_NOT_COMPLETED_TEXT = FREE_TIME_STEP_DIRECTORY + "/dataCollectingWasntCompleted.txt";
    public static final String FREE_TIME_ACCEPTED_TEXT = FREE_TIME_STEP_DIRECTORY + "/freeTimeAccepted.txt";
    public static final String INTERESTS_ACCEPTED_TEXT = INTEREST_STEP_DIRECTORY + "/interestsAccepted.txt";
    public static final String GEOPOSITION_ACCEPTED_TEXT = GEOPOSITION_STEP_DIRECTORY + "/geopositionAccepted.txt";

    public static final String INTERESTS_PROMPT = PROMPTS_DIRECTORY + "/interests.txt";
    public static final String FREE_TIME_PROMPT = PROMPTS_DIRECTORY + "/freeTime.txt";
    public static final String GEOPOSITION_PROMPT = PROMPTS_DIRECTORY + "/geoposition.txt";
    public static final String USER_PLACES_PROMPT = PROMPTS_DIRECTORY + "/userPlaces.txt";

    public static final String WELCOME_TEXT = START_COMMAND_DIRECTORY + "/welcome.txt";
    public static final String WELCOME_2_TEXT = START_COMMAND_DIRECTORY + "/welcome_2.txt";
    public static final String WELCOME_3_TEXT = START_COMMAND_DIRECTORY + "/welcome_3.txt";
    public static final String CLEAR_TEXT = CLEAR_COMMAND_DIRECTORY + "/clearConfirmation.txt";
    public static final String CLEAR_DONE = CLEAR_COMMAND_DIRECTORY + "/clearDone.txt";

    public static final String COMMAND_CONFIRMATION_TEXT = COMMANDS_DIRECTORY + "/commandConfirmation.txt";
    public static final String COMMAND_NOT_FOUND_TEXT = COMMANDS_DIRECTORY + "/commandNotFound.txt";
    public static final String COMMAND_CANCELED_TEXT = COMMANDS_DIRECTORY + "/commandExecutionCanceled.txt";
    public static final String NO_COMMANDS_EXECUTING_TEXT = COMMANDS_DIRECTORY + "/noCommandsExecuting.txt";
}
