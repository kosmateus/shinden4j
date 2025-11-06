package com.github.kosmateus.shinden.login.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Data Transfer Object (DTO) for handling the response to a login request.
 * <p>
 * The {@code LoginDetails} class encapsulates the result of a login operation, including
 * the status of the login attempt and any additional details. It provides a method to check
 * if the login was successful.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@Builder
@ToString
@RequiredArgsConstructor
public class LoginDetails {

    /**
     * The status of the login attempt.
     * <p>
     * This field holds an instance of the {@link Status} enum, indicating the result of the login operation.
     * </p>
     */
    private final Status status;

    /**
     * Additional details about the login attempt.
     * <p>
     * This field can hold any additional information related to the login operation, such as error messages
     * or user information, depending on the status of the login attempt.
     * </p>
     */
    private final Object details;

    /**
     * Checks if the login attempt was successful.
     * <p>
     * This method returns {@code true} if the status of the login attempt is {@link Status#SUCCESS}.
     * </p>
     *
     * @return {@code true} if the login was successful, {@code false} otherwise.
     */
    public boolean isSuccessful() {
        return status == Status.SUCCESS;
    }

    /**
     * Enum representing the possible statuses of a login attempt.
     */
    public enum Status {

        /**
         * Indicates that the login was successful.
         */
        SUCCESS,

        /**
         * Indicates that the login failed.
         */
        FAILURE,

        /**
         * Indicates that the provided credentials were invalid.
         */
        INVALID_CREDENTIALS
    }
}
