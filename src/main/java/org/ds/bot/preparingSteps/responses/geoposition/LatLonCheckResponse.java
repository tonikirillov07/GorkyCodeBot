package org.ds.bot.preparingSteps.responses.geoposition;

import org.ds.bot.preparingSteps.responses.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LatLonCheckResponse extends Response {
    protected LatLonCheckResponse(@NotNull Boolean isCorrect, @Nullable String description) {
        super(isCorrect, description);
    }
}
