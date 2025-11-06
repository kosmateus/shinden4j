package com.github.kosmateus.shinden.user.common.enums;

import com.github.kosmateus.shinden.http.request.FormParam;
import com.github.kosmateus.shinden.i18n.Translatable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing the main menu configuration for a user's page.
 * <p>
 * The {@code PageMainMenu} enum provides options for configuring the main menu display on a user's page.
 * This enum implements both {@link Translatable} and {@link FormParam} interfaces, allowing the selected
 * configuration to be used as a form parameter in HTTP requests, and supporting translation of the menu
 * labels using the {@link Translatable} interface.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@RequiredArgsConstructor
public enum PageMainMenu implements Translatable, FormParam {

    /**
     * Represents a configuration where all menu items are displayed.
     */
    ALL("all", "user.settings.edit.page-main-menu.all"),

    /**
     * Represents a configuration optimized for mobile devices.
     */
    MOBILE("mobile", "user.settings.edit.page-main-menu.mobile"),

    /**
     * Represents a configuration optimized specifically for phones.
     */
    PHONE("phone", "user.settings.edit.page-main-menu.phone"),

    /**
     * Represents a configuration where no menu items are displayed.
     */
    NONE("none", "user.settings.edit.page-main-menu.none");

    private final String formValue;
    private final String translationKey;

    /**
     * Returns the form parameter name for the page main menu configuration.
     * <p>
     * This method returns the string "pinned_menu" as the name of the parameter to be used in HTTP requests
     * for configuring the main menu on a user's page.
     * </p>
     *
     * @return the form parameter name, "pinned_menu".
     */
    @Override
    public String getFormParameter() {
        return "pinned_menu";
    }
}
