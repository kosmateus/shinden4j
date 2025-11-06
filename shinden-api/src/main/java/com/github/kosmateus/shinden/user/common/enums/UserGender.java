package com.github.kosmateus.shinden.user.common.enums;

import com.github.kosmateus.shinden.http.request.FormParam;
import com.github.kosmateus.shinden.http.request.QueryParam;
import com.github.kosmateus.shinden.i18n.Translatable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing the gender of a user.
 * <p>
 * The {@code UserGender} enum provides gender options for a user, implementing both
 * {@link QueryParam} and {@link Translatable} interfaces. It allows for the gender to be
 * used as a query parameter in requests and also supports translation of the gender label
 * using the {@link Translatable} interface.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@RequiredArgsConstructor
public enum UserGender implements FormParam, Translatable {

    /**
     * Represents the male gender.
     */
    MALE("m", "user.information.edit.gender.male"),

    /**
     * Represents the female gender.
     */
    FEMALE("w", "user.information.edit.gender.female"),

    /**
     * Represents no specified gender.
     */
    NONE("?", "user.information.edit.gender.none");

    private final String formValue;
    private final String translationKey;

    /**
     * Returns the query parameter name for the gender.
     * <p>
     * This method returns the string "gender" as the name of the parameter to be used in HTTP requests.
     * </p>
     *
     * @return the query parameter name, "gender".
     */
    @Override
    public String getFormParameter() {
        return "gender";
    }
}
