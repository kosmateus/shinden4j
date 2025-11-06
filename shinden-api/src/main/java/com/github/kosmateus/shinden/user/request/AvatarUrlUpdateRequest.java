package com.github.kosmateus.shinden.user.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * Represents a request to update a user's avatar URL.
 * <p>
 * The {@code AvatarUrlUpdateRequest} class is used to encapsulate the information required
 * to update the avatar URL for a specific user. It contains the user ID and the new avatar URL.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@Builder
@ToString
@AllArgsConstructor(onConstructor_ = @JsonCreator)
public class AvatarUrlUpdateRequest {

    /**
     * The unique identifier of the user whose avatar URL is being updated.
     * <p>
     * This field is mandatory and must be provided as a non-null {@link Long} value.
     * </p>
     */
    @NotNull
    private final Long userId;

    /**
     * The new URL of the user's avatar.
     * <p>
     * This field contains the URL that points to the new avatar image to be associated with the user.
     * It is required and cannot be null.
     * </p>
     */
    @NotNull
    private final String avatarUrl;
}
