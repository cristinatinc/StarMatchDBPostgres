package org.starmatch.src.exceptions;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String message, Exception e) {
        super(message);
    }
}
