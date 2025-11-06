package com.github.kosmateus.shinden.http.request;

/**
 * Interface representing a form parameter in an HTTP request.
 * <p>
 * The {@code FormParam} interface provides methods to retrieve the name and value
 * of a form parameter. Implementations of this interface can be used to represent
 * key-value pairs that are sent as part of the body in an HTTP POST request, typically
 * encoded as "application/x-www-form-urlencoded". This is commonly used in web forms
 * where data is submitted to a server for processing.
 * </p>
 *
 * @version 1.0.0
 */
public interface FormParam {

    /**
     * Returns the name of the form parameter.
     * <p>
     * The name represents the key of the form parameter, which is used by the server
     * to identify the data being sent. This is typically the "name" attribute of an
     * HTML form field.
     * </p>
     *
     * @return the parameter name as a {@link String}
     */
    String getFormParameter();

    /**
     * Returns the value of the form parameter.
     * <p>
     * The value represents the content or data associated with the form parameter.
     * This is the actual data that the client sends to the server, which corresponds
     * to the "value" of an HTML form field.
     * </p>
     *
     * @return the parameter value as a {@link String}
     */
    String getFormValue();
}
