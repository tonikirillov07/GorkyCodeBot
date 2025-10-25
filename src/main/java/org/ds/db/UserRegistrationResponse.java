package org.ds.db;

import org.ds.db.entity.UserEntity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record UserRegistrationResponse(@NotNull Boolean usingFirstTime) {
    @Contract("_ -> new")
    public static @NotNull UserRegistrationResponse of(@NotNull Boolean usingFirstTime) {
        return new UserRegistrationResponse(usingFirstTime);
    }
}
