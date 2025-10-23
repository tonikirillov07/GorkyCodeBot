package org.ds.bot.preparingSteps.responses.interests;

import org.ds.bot.preparingSteps.responses.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InterestResponse extends Response {
    private final String[] interests;

    protected InterestResponse(@NotNull Boolean isCorrect,
                               @Nullable String description,
                               @NotNull String[] interests) {
        super(isCorrect, description);
        this.interests = interests;
    }

    public String[] getInterests() {
        return interests;
    }
}
