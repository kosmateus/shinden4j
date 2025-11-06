package com.github.kosmateus.shinden.response;

/**
 * Enum representing the result of an operation.
 * <p>
 * The {@code Result} enum defines two possible outcomes for an operation:
 * {@link #SUCCESS} and {@link #FAILURE}. It is commonly used to indicate
 * whether an operation was successfully completed or encountered an error.
 * </p>
 *
 * @version 1.0.0
 */
public enum Result {
    /**
     * Indicates that the operation was successful.
     */
    SUCCESS,

    /**
     * Indicates that the operation failed.
     */
    FAILURE
}
