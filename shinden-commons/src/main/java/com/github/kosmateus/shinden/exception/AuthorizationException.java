package com.github.kosmateus.shinden.exception;

/**
 * Exception thrown when an authorization attempt fails.
 * <p>
 * The {@code AuthorizationException} is used to indicate that a user or process
 * has attempted to perform an action for which they do not have the necessary permissions.
 * This exception carries a message that provides additional context about the authorization failure.
 * </p>
 *
 * @version 1.0.0
 */
public class AuthorizationException extends RuntimeException {

    /**
     * Constructs a new {@code AuthorizationException} with the specified detail message.
     *
     * @param message the detail message, providing more information about the cause of the exception.
     */
    public AuthorizationException(String message) {
        super(message);
    }
}
