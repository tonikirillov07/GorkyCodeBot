package org.ds.bot.preparingSteps.responses.geoposition;

import org.ds.bot.preparingSteps.responses.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GeopositionResponse extends Response {
    private final String userLocation;

    protected GeopositionResponse(@NotNull Boolean isCorrect, @Nullable String description, String userLocation) {
        super(isCorrect, description);
        this.userLocation = userLocation;
    }

    public String getUserLocation() {
        return userLocation;
    }
}
