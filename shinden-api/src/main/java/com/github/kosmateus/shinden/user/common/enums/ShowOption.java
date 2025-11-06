package com.github.kosmateus.shinden.user.common.enums;

import com.github.kosmateus.shinden.http.request.FormParam;
import com.github.kosmateus.shinden.i18n.Translatable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing an option to show or hide certain elements in a user's settings.
 * <p>
 * The {@code ShowOption} enum provides two options, "yes" or "no", to indicate whether
 * specific elements should be shown or hidden in a user's settings. This enum implements
 * both {@link Translatable} and {@link FormParam} interfaces, allowing the selected option
 * to be used as a form parameter in HTTP requests, and supporting the translation of the
 * option labels using the {@link Translatable} interface.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@RequiredArgsConstructor
public enum ShowOption implements Translatable, FormParam {

    /**
     * Represents the option to show the element.
     */
    YES("yes", "user.settings.edit.show.yes"),

    /**
     * Represents the option to hide the element.
     */
    NO("no", "user.settings.edit.show.no");

    private final String formValue;
    private final String translationKey;

    /**
     * Returns the form parameter name for the show option.
     * <p>
     * This method returns the string "show" as the name of the parameter to be used in HTTP requests
     * when setting or changing the visibility of specific elements in a user's settings.
     * </p>
     *
     * @return the form parameter name, "show".
     */
    @Override
    public String getFormParameter() {
        return "show";
    }
}
