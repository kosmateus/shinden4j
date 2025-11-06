package com.github.kosmateus.shinden.exception;

/**
 * Interface representing an error code used for detailed error handling.
 * <p>
 * The {@code ErrorCode} interface provides a contract for classes to implement a method
 * that returns a unique code representing a specific error condition. This can be used
 * for identifying and handling errors in a more granular and structured manner.
 * </p>
 *
 * @version 1.0.0
 */
public interface ErrorCode {

    /**
     * Returns the unique code representing the error.
     *
     * @return the error code as a {@link String}.
     */
    String getCode();
}
