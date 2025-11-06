package com.github.kosmateus.shinden.login;

import com.github.kosmateus.shinden.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration of error codes specific to the login process.
 * <p>
 * The {@code LoginErrorCodes} enum defines error codes that can be encountered during the login
 * process. Each error code is associated with a specific situation, such as missing authentication
 * tokens in the login response.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@RequiredArgsConstructor
enum LoginErrorCodes implements ErrorCode {

    /**
     * Error code indicating that the authentication token was not found in the login response.
     */
    NO_AUTH_TOKEN_IN_LOGIN_RESPONSE("SL_001");

    private final String code;
}
