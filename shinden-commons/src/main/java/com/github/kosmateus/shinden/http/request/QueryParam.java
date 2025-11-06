package com.github.kosmateus.shinden.http.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Interface representing a query parameter in an HTTP request.
 * <p>
 * The {@code QueryParam} interface provides methods to retrieve the name and value
 * of a query parameter. Implementations of this interface can be used to represent
 * key-value pairs that are appended to the URL as part of the query string in an HTTP request.
 * </p>
 *
 * @version 1.0.0
 */
public interface QueryParam {

    /**
     * Returns the name of the query parameter.
     *
     * @return the parameter name as a {@link String}.
     */
    String getQueryParameter();

    /**
     * Returns the value of the query parameter.
     *
     * @return the parameter value as a {@link String}.
     */
    String getQueryValue();

    /**
     * Creates a new instance of {@link QueryParam} with the specified name and value.
     * <p>
     * This method returns an instance of the {@link SimpleQueryParam} class, which is
     * a private implementation of the {@code QueryParam} interface. It is used to
     * create simple key-value pairs that represent query parameters.
     * </p>
     *
     * @param queryParameter the name of the query parameter, must not be {@code null}.
     * @param queryValue     the value of the query parameter, may be {@code null}.
     * @return a new {@link QueryParam} instance containing the specified name and value.
     */
    static QueryParam of(String queryParameter, String queryValue) {
        return new SimpleQueryParam(queryParameter, queryValue);
    }

    /**
     * A simple implementation of the {@link QueryParam} interface.
     * <p>
     * The {@code SimpleQueryParam} class is an immutable representation of a query parameter
     * with a name and an optional value. It is primarily used to provide a straightforward
     * implementation of the {@code QueryParam} interface.
     * </p>
     */
    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    class SimpleQueryParam implements QueryParam {
        /**
         * The name of the query parameter.
         */
        private final String queryParameter;

        /**
         * The value of the query parameter, can be null.
         */
        private final String queryValue;
    }
}
