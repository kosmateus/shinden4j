package com.github.kosmateus.shinden.user.request;

import com.github.kosmateus.shinden.user.common.ListsSettings;
import com.github.kosmateus.shinden.user.common.UserId;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

/**
 * Represents a request to update a user's list settings.
 * <p>
 * The {@code ListsSettingsRequest} class extends {@link ListsSettings} to include
 * additional information required to update these settings, specifically the user ID.
 * This class allows for updating settings related to both the user's anime and manga lists.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@ToString
@SuperBuilder
public class ListsSettingsRequest extends ListsSettings implements UserId {

    /**
     * The unique identifier of the user whose list settings are being updated.
     * <p>
     * This field is mandatory and must be provided as a non-null {@link Long} value.
     * </p>
     */
    @NotNull
    private final Long userId;
}
