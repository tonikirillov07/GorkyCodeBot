package org.ds.exceptions;

public class JSONProcessingException extends RuntimeException {
    public JSONProcessingException(Exception e) {
        super("Failed to process JSON: %s".formatted(e));
    }
}
