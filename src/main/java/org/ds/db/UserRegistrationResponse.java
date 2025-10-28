package org.ds.db;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public record UserRegistrationResponse(@NotNull Boolean usingFirstTime,
                                       @NotNull Boolean isGotResult,
                                       @NotNull LocalDateTime lastUsingTime) {
    @Contract("_, _, _ -> new")
    public static @NotNull UserRegistrationResponse of(@NotNull Boolean usingFirstTime,
                                                       @NotNull Boolean isGotResult,
                                                       @NotNull LocalDateTime lastUsingTime) {
        return new UserRegistrationResponse(usingFirstTime, isGotResult, lastUsingTime);
    }
}
