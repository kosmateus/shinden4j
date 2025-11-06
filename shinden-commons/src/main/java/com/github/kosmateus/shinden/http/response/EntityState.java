package com.github.kosmateus.shinden.http.response;

/**
 * Enum representing various states of an entity in response to an HTTP request.
 * <p>
 * The {@code EntityState} enum provides a set of predefined constants that represent
 * different outcomes or states of an entity following an HTTP request. These states
 * can be used to categorize and handle responses according to the specific situation encountered.
 * </p>
 *
 * @version 1.0.0
 */
public enum EntityState {
    /**
     * Indicates that the request was successful but the response entity is empty.
     */
    EMPTY_OK,

    /**
     * Indicates that the requested entity was not found.
     */
    NOT_FOUND,

    /**
     * Indicates a generic error occurred that does not fit into other specific categories.
     */
    GENERIC_ERROR,

    /**
     * Indicates that the request was unauthorized, typically due to lack of valid authentication credentials.
     */
    UNAUTHORIZED,

    /**
     * Indicates that an internal server error occurred.
     */
    INTERNAL_ERROR,

    /**
     * Indicates that a bad gateway error occurred, typically due to issues with a downstream server.
     */
    BAD_GATEWAY,

    /**
     * Indicates that the request was malformed or invalid.
     */
    BAD_REQUEST
}
