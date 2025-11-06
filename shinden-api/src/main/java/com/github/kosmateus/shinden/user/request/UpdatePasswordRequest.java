package com.github.kosmateus.shinden.user.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * Represents a request to update a user's password.
 * <p>
 * The {@code UpdatePasswordRequest} class encapsulates the data required to update a user's password,
 * including the user ID, the current password, the new password, and a confirmation of the new password.
 * </p>
 *
 * <p>Instances of this class are immutable and are typically created using the {@link Builder} pattern
 * provided by Lombok's {@link Builder} annotation.</p>
 *
 * @version 1.0.0
 */
@Getter
@Builder
@ToString
@AllArgsConstructor(onConstructor_ = @JsonCreator)
public class UpdatePasswordRequest {

    /**
     * The unique identifier of the user whose password is being updated.
     * <p>
     * This field is required and must be a valid user ID.
     * </p>
     */
    @NotNull
    private final Long userId;

    /**
     * The user's current password.
     * <p>
     * This field is required and must be the user's current valid password.
     * </p>
     */
    @NotNull
    private final String currentPassword;

    /**
     * The user's new password.
     * <p>
     * This field is required and must be the new password that the user wishes to set.
     * It should comply with the system's password policy.
     * </p>
     */
    @NotNull
    private final String newPassword;
}
