package org.ds.db;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record UserRegistrationResponse(@NotNull Boolean usingFirstTime,
                                       @NotNull Boolean isGotResult) {
    @Contract("_, _ -> new")
    public static @NotNull UserRegistrationResponse of(@NotNull Boolean usingFirstTime,
                                                       @NotNull Boolean isGotResult) {
        return new UserRegistrationResponse(usingFirstTime, isGotResult);
    }
}
