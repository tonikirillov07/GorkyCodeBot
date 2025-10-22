package org.ds.exceptions;

import org.jetbrains.annotations.NotNull;

public class FileReadException extends RuntimeException {
    public FileReadException(String fileName, @NotNull Exception e) {
        super("Failed to read file: %s. Error: %s".formatted(fileName, e.toString()));
    }
}
