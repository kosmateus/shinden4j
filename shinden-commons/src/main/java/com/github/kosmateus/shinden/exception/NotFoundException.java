package com.github.kosmateus.shinden.exception;

/**
 * Exception thrown when a requested resource is not found.
 * <p>
 * The {@code NotFoundException} is used to signal that a specific resource or entity
 * could not be located. This exception typically carries a message that provides
 * additional context about the missing resource.
 * </p>
 *
 * @version 1.0.0
 */
public class NotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code NotFoundException} with the specified detail message.
     *
     * @param message the detail message, providing more information about the cause of the exception.
     */
    public NotFoundException(String message) {
        super(message);
    }
}
