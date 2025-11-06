package com.github.kosmateus.shinden.user.common.enums;

import com.github.kosmateus.shinden.http.request.FormParam;
import com.github.kosmateus.shinden.i18n.Translatable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing the option to enable or disable automatic status changes.
 * <p>
 * The {@code StatusAutoChange} enum provides options for automatically updating the status of a user's items,
 * such as anime or manga, based on their progress. This enum implements both {@link Translatable} and {@link FormParam}
 * interfaces, allowing the selected option to be used as a form parameter in HTTP requests, and supporting the translation
 * of the option labels using the {@link Translatable} interface.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@RequiredArgsConstructor
public enum StatusAutoChange implements Translatable, FormParam {

    /**
     * Represents the option to disable automatic status changes.
     */
    NO("0", "user.settings.edit.status-autochange.no"),

    /**
     * Represents the option to enable automatic status changes.
     */
    YES("1", "user.settings.edit.status-autochange.yes");

    private final String formValue;
    private final String translationKey;

    /**
     * Returns the form parameter name for the status auto-change option.
     * <p>
     * This method returns the string "status_autochange" as the name of the parameter to be used in HTTP requests
     * when setting or changing the option for automatic status changes.
     * </p>
     *
     * @return the form parameter name, "status_autochange".
     */
    @Override
    public String getFormParameter() {
        return "status_autochange";
    }
}
