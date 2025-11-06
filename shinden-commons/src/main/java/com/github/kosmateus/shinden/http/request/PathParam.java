package com.github.kosmateus.shinden.http.request;

/**
 * Interface representing a path parameter in an HTTP request.
 * <p>
 * The {@code PathParam} interface defines methods for retrieving the name and value of
 * a path parameter, which is a dynamic component of the URL path in an HTTP request.
 * Path parameters are often used to specify resources or filter criteria in RESTful APIs.
 * </p>
 *
 * @version 1.0.0
 */
public interface PathParam {

    /**
     * Returns the name of the path parameter.
     * <p>
     * The name is a key that represents a variable part of the URL path.
     * For example, in a URL like {@code /user/{userId}}, the name of the
     * path parameter would be {@code "userId"}.
     * </p>
     *
     * @return the parameter name as a {@link String}
     */
    String getPathParameter();

    /**
     * Returns the value of the path parameter.
     * <p>
     * The value corresponds to the actual data that replaces the path parameter in
     * the URL path. For example, in a URL like {@code /user/123}, the value of the
     * path parameter {@code "userId"} would be {@code "123"}.
     * </p>
     *
     * @return the parameter value as a {@link String}
     */
    String getPathValue();
}
