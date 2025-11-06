package com.github.kosmateus.shinden.http.response;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jsoup.HttpStatusException;

import java.io.IOException;
import java.util.Optional;

/**
 * Utility class for retrieving HTTP status codes from exceptions.
 * <p>
 * The {@code HttpStatusExceptionRetrieval} class provides a method to extract the HTTP status code
 * from an {@link IOException} if it is an instance of {@link HttpStatusException}. This utility
 * helps in determining the specific HTTP error that caused the exception.
 * </p>
 *
 * @version 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpStatusExceptionRetrieval {

    /**
     * Retrieves the HTTP status code from the provided {@link IOException}.
     * <p>
     * If the exception is an instance of {@link HttpStatusException}, this method extracts and returns
     * the associated HTTP status code as an {@link Optional}. If the exception does not contain an HTTP status code,
     * an empty {@link Optional} is returned.
     * </p>
     *
     * @param e the {@link IOException} from which to extract the HTTP status code.
     * @return an {@link Optional} containing the {@link HttpStatus} if present, or an empty {@link Optional} if not.
     */
    public static Optional<HttpStatus> getHttpStatus(IOException e) {
        if (e instanceof HttpStatusException) {
            return Optional.of(HttpStatus.valueOf(((HttpStatusException) e).getStatusCode()));
        }
        return Optional.empty();
    }
}
