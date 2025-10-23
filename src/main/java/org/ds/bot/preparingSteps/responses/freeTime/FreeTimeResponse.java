package org.ds.bot.preparingSteps.responses.freeTime;

import org.ds.bot.preparingSteps.responses.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FreeTimeResponse extends Response {
    private final String freeTime;

    public FreeTimeResponse(@NotNull Boolean isCorrect,
                            @Nullable String description,
                            @Nullable String freeTime) {
        super(isCorrect, description);
        this.freeTime = freeTime;
    }

    public String getFreeTime() {
        return freeTime;
    }
}
