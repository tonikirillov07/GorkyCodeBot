package org.ds.exceptions;

import org.jetbrains.annotations.NotNull;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(@NotNull Long userId) {
        super("User with id %d was not found".formatted(userId));
    }
}
