package org.ds.maps;

import org.ds.bot.preparingSteps.responses.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CoordinatesResponse extends Response {
    private final float lat, lon;

    public CoordinatesResponse(@NotNull Boolean isCorrect, @Nullable String description) {
        super(isCorrect, description);
        this.lat = 0f;
        this.lon = 0f;
    }

    public CoordinatesResponse(@NotNull Boolean isCorrect, @Nullable String description, float lat, float lon) {
        super(isCorrect, description);
        this.lat = lat;
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }

    public float getLon() {
        return lon;
    }

    @Override
    public String toString() {
        return "CoordinatesResponse{" +
                "lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
