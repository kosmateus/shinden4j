package com.github.kosmateus.shinden.http.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.http.HttpStatus;

/**
 * Represents the reason why an entity is empty in an HTTP response.
 * <p>
 * The {@code EmptyReason} class encapsulates the state of an entity that resulted
 * in an empty response, along with any associated error details. This class provides
 * various static factory methods to create instances based on HTTP status codes
 * and associated errors.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmptyReason {

    private final EntityState state;
    private final ErrorDetails errorDetails;

    /**
     * Constructs an {@code EmptyReason} with no state or error details.
     * <p>
     * This constructor is used to create an instance of {@code EmptyReason} with no specific state
     * or error information.
     * </p>
     */
    public EmptyReason() {
        this.state = null;
        this.errorDetails = null;
    }

    /**
     * Creates an {@code EmptyReason} representing a successful response with no content.
     *
     * @return an {@code EmptyReason} with the state {@link EntityState#EMPTY_OK}.
     */
    public static EmptyReason ok() {
        return new EmptyReason(EntityState.EMPTY_OK, ErrorDetails.empty());
    }

    /**
     * Creates an {@code EmptyReason} based on the provided HTTP status code and error details.
     * <p>
     * This method maps common HTTP status codes to corresponding {@link EntityState} values.
     * </p>
     *
     * @param httpStatus   the HTTP status code.
     * @param errorDetails the details of the error that caused the empty response.
     * @return an {@code EmptyReason} corresponding to the provided HTTP status code.
     */
    public static EmptyReason fromHttpStatus(int httpStatus, ErrorDetails errorDetails) {
        switch (httpStatus) {
            case HttpStatus.SC_NOT_FOUND:
                return notFound(errorDetails);
            case HttpStatus.SC_INTERNAL_SERVER_ERROR:
                return internalError(errorDetails);
            case HttpStatus.SC_UNAUTHORIZED:
                return unauthorized(errorDetails);
            case HttpStatus.SC_BAD_GATEWAY:
                return badGateway(errorDetails);
            case HttpStatus.SC_BAD_REQUEST:
                return badRequest(errorDetails);
            default:
                return errorResponse(errorDetails);
        }
    }

    /**
     * Creates an {@code EmptyReason} representing a "Not Found" error.
     *
     * @param errorDetails the details of the error.
     * @return an {@code EmptyReason} with the state {@link EntityState#NOT_FOUND}.
     */
    public static EmptyReason notFound(ErrorDetails errorDetails) {
        return new EmptyReason(EntityState.NOT_FOUND, errorDetails);
    }

    /**
     * Creates an {@code EmptyReason} representing an internal server error.
     *
     * @param errorDetails the details of the error.
     * @return an {@code EmptyReason} with the state {@link EntityState#INTERNAL_ERROR}.
     */
    public static EmptyReason internalError(ErrorDetails errorDetails) {
        return new EmptyReason(EntityState.INTERNAL_ERROR, errorDetails);
    }

    /**
     * Creates an {@code EmptyReason} representing an unauthorized error.
     *
     * @param errorDetails the details of the error.
     * @return an {@code EmptyReason} with the state {@link EntityState#UNAUTHORIZED}.
     */
    public static EmptyReason unauthorized(ErrorDetails errorDetails) {
        return new EmptyReason(EntityState.UNAUTHORIZED, errorDetails);
    }

    /**
     * Creates an {@code EmptyReason} representing a bad gateway error.
     *
     * @param errorDetails the details of the error.
     * @return an {@code EmptyReason} with the state {@link EntityState#BAD_GATEWAY}.
     */
    public static EmptyReason badGateway(ErrorDetails errorDetails) {
        return new EmptyReason(EntityState.BAD_GATEWAY, errorDetails);
    }

    /**
     * Creates an {@code EmptyReason} representing a bad request error.
     *
     * @param errorDetails the details of the error.
     * @return an {@code EmptyReason} with the state {@link EntityState#BAD_REQUEST}.
     */
    public static EmptyReason badRequest(ErrorDetails errorDetails) {
        return new EmptyReason(EntityState.BAD_REQUEST, errorDetails);
    }

    /**
     * Creates an {@code EmptyReason} representing a generic error.
     *
     * @param errorDetails the details of the error.
     * @return an {@code EmptyReason} with the state {@link EntityState#GENERIC_ERROR}.
     */
    public static EmptyReason errorResponse(ErrorDetails errorDetails) {
        return new EmptyReason(EntityState.GENERIC_ERROR, errorDetails);
    }
}
