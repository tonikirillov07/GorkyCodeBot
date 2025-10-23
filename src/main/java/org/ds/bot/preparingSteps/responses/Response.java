package org.ds.bot.preparingSteps.responses;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Response {
    private final Boolean isCorrect;
    private final String description;

    protected Response(@NotNull Boolean isCorrect,
                       @Nullable String description) {
        this.isCorrect = isCorrect;
        this.description = description;
    }

    public Boolean isCorrect() {
        return isCorrect;
    }

    public String getDescription() {
        return description;
    }
}
