package org.ds.exceptions;

public class InterestsListIsEmpty extends RuntimeException {
    public InterestsListIsEmpty() {
        super("User interests list is empty");
    }
}
