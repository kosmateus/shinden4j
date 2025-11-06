package com.github.kosmateus.shinden.utils;

import com.github.kosmateus.shinden.exception.ForbiddenException;
import com.github.kosmateus.shinden.exception.NotFoundException;
import com.github.kosmateus.shinden.http.response.ResponseHandler;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class for validating the response from a {@link ResponseHandler}.
 * <p>
 * The {@code ResponseHandlerValidator} class provides static methods to validate
 * the result of an HTTP response encapsulated in a {@link ResponseHandler}. It checks
 * for specific response conditions, such as "Not Found" and "Forbidden" statuses,
 * and throws appropriate exceptions if those conditions are met.
 * </p>
 *
 * @version 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseHandlerValidator {

    /**
     * Validates the response contained within a {@link ResponseHandler}.
     * <p>
     * This method checks the response for specific conditions:
     * </p>
     * <ul>
     *   <li>If the response indicates that the resource was not found (HTTP 404),
     *   a {@link NotFoundException} is thrown.</li>
     *   <li>If the response indicates that access is forbidden (HTTP 403),
     *   a {@link ForbiddenException} is thrown.</li>
     * </ul>
     *
     * @param responseHandler the {@link ResponseHandler} to validate. Must not be null.
     * @throws NotFoundException        if the response indicates that the resource was not found.
     * @throws ForbiddenException       if the response indicates that access is forbidden.
     * @throws IllegalArgumentException if the responseHandler is null.
     * @throws IllegalStateException    if the response is not valid.
     */
    public static void validateResponse(ResponseHandler<?> responseHandler) {
        if (responseHandler == null) {
            throw new IllegalArgumentException("ResponseHandler must not be null.");
        }

        if (responseHandler.isNotFound()) {
            throw new NotFoundException(responseHandler.getEmptyReason().getErrorDetails().getMessage());
        }
        if (responseHandler.isForbidden()) {
            throw new ForbiddenException(responseHandler.getEmptyReason().getErrorDetails().getMessage());
        }
        if (responseHandler.hasStatus(800)) {
            throw new IllegalStateException("The response is not valid", responseHandler.getEmptyReason().getErrorDetails().getCause());
        }
    }
}
