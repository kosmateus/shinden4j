package com.github.kosmateus.shinden.http.request;

/**
 * Interface representing a form parameter in an HTTP request.
 * <p>
 * The {@code FormParam} interface provides methods to retrieve the name and value
 * of a form parameter. Implementations of this interface can be used to represent
 * key-value pairs that are sent as part of the body in an HTTP POST request, typically
 * encoded as "application/x-www-form-urlencoded".
 * </p>
 *
 * @version 1.0.0
 */
public interface FormPart {

    /**
     * Returns the name of the form parameter.
     *
     * @return the parameter name as a {@link String}.
     */
    String getParameter();

    /**
     * Returns the value of the form parameter.
     *
     * @return the parameter value as a {@link String}.
     */
    String getValue();
}
