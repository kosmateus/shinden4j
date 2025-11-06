package com.github.kosmateus.shinden.user.common.enums;

import com.github.kosmateus.shinden.http.request.FormParam;
import com.github.kosmateus.shinden.i18n.Translatable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing the theme of a user's page.
 * <p>
 * The {@code PageTheme} enum provides different themes that can be applied to a user's page.
 * Each theme has a unique identifier and a corresponding translation key. This enum implements
 * both {@link Translatable} and {@link FormParam} interfaces, allowing the selected theme to be
 * used as a form parameter in HTTP requests and supporting the translation of theme labels using
 * the {@link Translatable} interface.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@RequiredArgsConstructor
public enum PageTheme implements Translatable, FormParam {

    /**
     * Represents the default theme.
     */
    DEFAULT("0", "user.settings.edit.page-theme.default"),

    /**
     * Represents the simple blue theme.
     */
    SIMPLE_BLUE("1", "user.settings.edit.page-theme.simple-blue"),

    /**
     * Represents the Bakemono theme.
     */
    BAKEMONO("2", "user.settings.edit.page-theme.bakemono"),

    /**
     * Represents the Halloween theme.
     */
    HELLOWEEN("3", "user.settings.edit.page-theme.helloween"),

    /**
     * Represents the Higurashi theme.
     */
    HIGURASHI("4", "user.settings.edit.page-theme.higurashi"),

    /**
     * Represents the Christmas theme with snow.
     */
    CHRISTMAS("5", "user.settings.edit.page-theme.christmas"),

    /**
     * Represents the Christmas theme without snow.
     */
    CHRISTMAS_NO_SNOW("6", "user.settings.edit.page-theme.christmas-no-snow");

    private final String formValue;
    private final String translationKey;

    /**
     * Returns the form parameter name for the page theme.
     * <p>
     * This method returns the string "skin_id" as the name of the parameter to be used in HTTP requests
     * when setting or changing the theme of a user's page.
     * </p>
     *
     * @return the form parameter name, "skin_id".
     */
    @Override
    public String getFormParameter() {
        return "skin_id";
    }
}
