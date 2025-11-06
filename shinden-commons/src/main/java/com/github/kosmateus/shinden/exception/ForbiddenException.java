package com.github.kosmateus.shinden.exception;

/**
 * Exception thrown when access to a resource is forbidden.
 * <p>
 * The {@code ForbiddenException} is used to indicate that an attempt to access a
 * resource was denied due to insufficient permissions or other access control restrictions.
 * This exception includes a message that provides additional context about the access denial.
 * </p>
 *
 * @version 1.0.0
 */
public class ForbiddenException extends RuntimeException {

    /**
     * Constructs a new {@code ForbiddenException} with the specified detail message.
     *
     * @param message the detail message, providing more information about the cause of the exception.
     */
    public ForbiddenException(String message) {
        super(message);
    }
}
