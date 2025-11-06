package com.github.kosmateus.shinden.user.common.enums;

import com.github.kosmateus.shinden.http.request.FormParam;
import com.github.kosmateus.shinden.i18n.Translatable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing the option to skip filler episodes in a user's watchlist.
 * <p>
 * The {@code SkipFillers} enum provides options for enabling or disabling the skipping of filler episodes
 * in a user's watchlist. This enum implements both {@link Translatable} and {@link FormParam} interfaces,
 * allowing the selected option to be used as a form parameter in HTTP requests, and supporting the translation
 * of the option labels using the {@link Translatable} interface.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@RequiredArgsConstructor
public enum SkipFillers implements Translatable, FormParam {

    /**
     * Represents the option to not skip filler episodes.
     */
    NO("0", "user.settings.edit.skip-fillers.no"),

    /**
     * Represents the option to skip filler episodes.
     */
    YES("1", "user.settings.edit.skip-fillers.yes");

    private final String formValue;
    private final String translationKey;

    /**
     * Returns the form parameter name for the skip fillers option.
     * <p>
     * This method returns the string "skip_filers" as the name of the parameter to be used in HTTP requests
     * when setting or changing the option to skip filler episodes in a user's watchlist.
     * </p>
     *
     * @return the form parameter name, "skip_filers".
     */
    @Override
    public String getFormParameter() {
        return "skip_filers";
    }
}
