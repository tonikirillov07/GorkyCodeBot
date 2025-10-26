package org.ds.exceptions;

import org.jetbrains.annotations.NotNull;

public class GettingResponseException extends RuntimeException {
    public GettingResponseException(@NotNull Exception e) {
        super("Failed to get response: %s".formatted(e.toString()));
    }
}
