package com.github.kosmateus.shinden.http.response;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

/**
 * Represents the details of an error that occurred during an HTTP request.
 * <p>
 * The {@code ErrorDetails} class contains information about an error, including a code,
 * message, error name, and additional parameters. It provides a structured way to represent
 * error details that can be used for logging, debugging, or returning error responses in an API.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@Builder
@ToString
@EqualsAndHashCode
public final class ErrorDetails {

    private final String code;
    private final String message;
    private final String errorName;
    private final Throwable cause;
    private final Map<String, String> paramMap;

    /**
     * Creates an instance of {@code ErrorDetails} with no details.
     * <p>
     * This method returns an empty {@code ErrorDetails} object, which can be used
     * when there are no specific error details to provide.
     * </p>
     *
     * @return an empty {@code ErrorDetails} instance.
     */
    public static ErrorDetails empty() {
        return builder().build();
    }

    /**
     * Creates an instance of {@code ErrorDetails} for a mapping error.
     * <p>
     * This method returns an {@code ErrorDetails} object with a predefined code and message
     * indicating that there was a failure in mapping the response to the expected class.
     * </p>
     *
     * @return an {@code ErrorDetails} instance representing a mapping error.
     */
    public static ErrorDetails mappingError() {
        return builder().code("500").message("Couldn't map response to given class.").build();
    }
}
