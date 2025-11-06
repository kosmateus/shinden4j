package com.github.kosmateus.shinden.login;

import com.github.kosmateus.shinden.auth.PageStructureChangedException;
import com.github.kosmateus.shinden.exception.JsoupParserException;
import com.github.kosmateus.shinden.login.request.LoginRequest;
import com.github.kosmateus.shinden.login.response.LoginDetails;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Interface for handling user login operations.
 * <p>
 * The {@code LoginApi} interface defines the contract for logging in a user by providing
 * a method that accepts a {@link LoginRequest} and returns a {@link LoginDetails}.
 * This interface also specifies potential exceptions that can be thrown if issues arise
 * during the login process.
 * </p>
 *
 * @version 1.0.0
 */
public interface LoginApi {

    /**
     * Logs in a user using the provided login credentials.
     * <p>
     * This method attempts to authenticate a user based on the credentials provided in
     * the {@link LoginRequest}. Upon a successful login, a {@link LoginDetails} is returned
     * with the status and any additional details. If the login page's structure has changed
     * or there is an error parsing the page, corresponding exceptions are thrown.
     * </p>
     *
     * @param loginRequest the {@link LoginRequest} containing the user's login credentials.
     * @return a {@link LoginDetails} indicating the outcome of the login attempt.
     * @throws PageStructureChangedException if the structure of the login page has changed unexpectedly.
     * @throws JsoupParserException          if an error occurs while parsing the web page.
     */
    LoginDetails login(@Valid @NotNull LoginRequest loginRequest);
}
