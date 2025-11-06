package com.github.kosmateus.shinden.user.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.kosmateus.shinden.http.request.FileResource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * Represents a request to update a user's avatar with a file.
 * <p>
 * The {@code AvatarFileUpdateRequest} class is used to encapsulate the information required
 * to update the avatar for a specific user using a file. It contains the user ID and the file resource
 * representing the new avatar image.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@Builder
@ToString
@AllArgsConstructor(onConstructor_ = @JsonCreator)
public class AvatarFileUpdateRequest {

    /**
     * The unique identifier of the user whose avatar is being updated.
     * <p>
     * This field is mandatory and must be provided as a non-null {@link Long} value.
     * </p>
     */
    @NotNull
    private final Long userId;

    /**
     * The file resource representing the new avatar image.
     * <p>
     * This field contains the {@link FileResource} that points to the new avatar file
     * to be associated with the user. The file resource is required and cannot be null.
     * </p>
     */
    @NotNull
    private final FileResource avatar;
}
