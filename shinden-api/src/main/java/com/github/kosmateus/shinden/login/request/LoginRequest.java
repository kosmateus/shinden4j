package com.github.kosmateus.shinden.login.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * Represents a request to log in to the system.
 * <p>
 * The {@code LoginRequest} class encapsulates the credentials and options needed to authenticate
 * a user, including the username, password, and an optional "remember me" setting. It also provides
 * a method to convert these details into a form data map suitable for submission in an HTTP request.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@Builder
@ToString
@AllArgsConstructor(onConstructor_ = @JsonCreator)
public class LoginRequest {

    /**
     * The username of the user attempting to log in.
     * <p>
     * This field is mandatory and must be provided as a non-blank {@link String}.
     * </p>
     */
    @NotBlank
    private final String username;

    /**
     * The password of the user attempting to log in.
     * <p>
     * This field is mandatory and must be provided as a non-blank {@link String}.
     * </p>
     */
    @NotBlank
    private final String password;

    /**
     * A flag indicating whether the user should be remembered on the system.
     * <p>
     * If set to {@code true}, the "remember me" functionality is enabled, allowing the user's session
     * to persist beyond the current login. If {@code false}, the session will only last for the current session.
     * </p>
     */
    private final boolean rememberMe;

    /**
     * Converts the login request details into a form data map.
     * <p>
     * This method creates an immutable map containing the username, password, "remember me" setting, and
     * an additional login field. The map is intended for use in submitting the login request as form data
     * in an HTTP request.
     * </p>
     *
     * @return a map containing the form data for the login request
     */
    public Map<String, String> toFormData() {
        return ImmutableMap.of(
                "username", username,
                "password", password,
                "remember", rememberMe ? "on" : "",
                "login", ""
        );
    }
}
