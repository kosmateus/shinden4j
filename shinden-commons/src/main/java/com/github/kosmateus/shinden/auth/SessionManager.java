package com.github.kosmateus.shinden.auth;

import java.util.Map;

/**
 * Interface representing the management of a user session.
 * <p>
 * The {@code SessionManager} interface provides methods to handle the details of a user's session, such as user ID, username, password, authentication token,
 * and cookies. It also manages session persistence features like the "remember me" option and tracks the authentication state.
 * </p>
 *
 * @version 1.0.0
 */
public interface SessionManager {

    /**
     * Returns the ID of the authenticated user.
     *
     * @return the user's ID as a {@link Long}.
     */
    Long getUserId();

    /**
     * Sets the user ID for the session.
     *
     * @param userId the user's ID as a {@link Long}.
     */
    void setUserId(Long userId);

    /**
     * Returns the username of the authenticated user.
     *
     * @return the username as a {@link String}.
     */
    String getUsername();

    /**
     * Sets the username for the session.
     *
     * @param username the username as a {@link String}.
     */
    void setUsername(String username);

    /**
     * Returns the password of the authenticated user.
     *
     * @return the password as a {@link String}.
     */
    String getPassword();

    /**
     * Sets the password for the session.
     *
     * @param password the password as a {@link String}.
     */
    void setPassword(String password);

    /**
     * Returns the authentication token of the session.
     *
     * @return the authentication token as a {@link String}.
     */
    String getAuthToken();

    /**
     * Sets the authentication token for the session.
     *
     * @param authToken the authentication token as a {@link String}.
     */
    void setAuthToken(String authToken);

    /**
     * Indicates whether the session is persistent across restarts.
     *
     * @return {@code true} if "remember me" is enabled; {@code false} otherwise.
     */
    boolean isRememberMe();

    /**
     * Sets whether the session should be persistent across restarts.
     *
     * @param rememberMe {@code true} to enable "remember me" functionality; {@code false} otherwise.
     */
    void setRememberMe(boolean rememberMe);

    /**
     * Indicates whether the user has been successfully authenticated.
     *
     * @return {@code true} if the user is successfully authenticated; {@code false} otherwise.
     */
    boolean isSuccessfullyAuthenticated();

    /**
     * Returns the cookies associated with the current session.
     *
     * @return a {@link Map} of cookies, where the key is the cookie name and the value is the cookie value.
     */
    Map<String, String> getCookies();

    /**
     * Sets the cookies for the session.
     *
     * @param headers a {@link Map} of cookies, where the key is the cookie name and the value is the cookie value.
     */
    void setCookies(Map<String, String> headers);
}
