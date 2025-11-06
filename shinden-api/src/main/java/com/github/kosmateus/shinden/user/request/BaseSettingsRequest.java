package com.github.kosmateus.shinden.user.request;

import com.github.kosmateus.shinden.user.common.BaseSettings;
import com.github.kosmateus.shinden.user.common.UserId;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

/**
 * Represents a request to update a user's base settings.
 * <p>
 * The {@code BaseSettingsRequest} class extends {@link BaseSettings} to include
 * additional information required to update these settings, specifically the user ID.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@ToString
@SuperBuilder
public class BaseSettingsRequest extends BaseSettings implements UserId {

    /**
     * The unique identifier of the user whose settings are being updated.
     * <p>
     * This field is mandatory and must be provided as a non-null {@link Long} value.
     * </p>
     */
    @NotNull
    private final Long userId;
}
