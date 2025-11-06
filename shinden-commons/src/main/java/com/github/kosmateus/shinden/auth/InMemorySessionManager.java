package com.github.kosmateus.shinden.auth;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * In-memory implementation of the {@link SessionManager} interface.
 * <p>
 * The {@code InMemorySessionManager} stores session-related data, such as username, password,
 * authentication token, and cookies, directly in memory. This is suitable for short-lived sessions or
 * scenarios where persistence is not required beyond the application runtime.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@Setter
public class InMemorySessionManager implements SessionManager {

    private String username;
    private String password;
    private String authToken;
    private boolean rememberMe;
    private Map<String, String> cookies;
    private Long userId;

    @Override
    public boolean isSuccessfullyAuthenticated() {
        return StringUtils.isNotBlank(authToken);
    }
}
