package com.github.kosmateus.shinden.user.common.enums;

import com.github.kosmateus.shinden.http.request.DisplayValue;
import com.github.kosmateus.shinden.http.request.FormParam;
import com.github.kosmateus.shinden.http.request.PathParam;
import com.github.kosmateus.shinden.i18n.Translatable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing the status of an anime in a user's list.
 * <p>
 * The {@code UserTitleStatus} enum provides different statuses that an anime can have in a user's
 * list, such as "in progress", "completed", "skipped", etc. This enum implements the {@link Translatable},
 * {@link FormParam}, {@link PathParam}, and {@link DisplayValue} interfaces, allowing the status to be
 * used in HTTP requests as form and path parameters, while also supporting translation and display
 * functionalities.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@RequiredArgsConstructor
public enum UserTitleStatus implements Translatable, FormParam, PathParam, DisplayValue {

    /**
     * Represents an anime that is currently being watched.
     */
    IN_PROGRESS("in progress", "in-progress", "Oglądam", "user.settings.edit.status.in-progress"),

    /**
     * Represents an anime that has been completed.
     */
    COMPLETED("completed", "Obejrzane", "user.settings.edit.status.completed"),

    /**
     * Represents an anime that has been skipped.
     */
    SKIP("skip", "Pomijam", "user.settings.edit.status.skip"),

    /**
     * Represents an anime that is on hold.
     */
    HOLD("hold", "Wstrzymane", "user.settings.edit.status.hold"),

    /**
     * Represents an anime that has been dropped.
     */
    DROPPED("dropped", "Porzucone", "user.settings.edit.status.dropped"),

    /**
     * Represents an anime that is planned to be watched.
     */
    PLAN("plan", "Planuję", "user.settings.edit.status.plan");

    /** The value used for form submissions. */
    private final String formValue;

    /**
     * The value used in path parameters.
     */
    private final String pathValue;

    /**
     * The display value for the status.
     */
    private final String displayValue;

    /** The translation key used for internationalization of the status. */
    private final String translationKey;

    /**
     * Constructor for statuses where form value, path value, and display value are the same.
     *
     * @param value          the value used for form submissions and path parameters.
     * @param displayValue   the display value for the status.
     * @param translationKey the translation key for internationalization.
     */
    UserTitleStatus(String value, String displayValue, String translationKey) {
        this.translationKey = translationKey;
        this.formValue = value;
        this.pathValue = value;
        this.displayValue = displayValue;
    }

    /**
     * Returns the {@code UserTitleStatus} corresponding to the specified value.
     * <p>
     * This method looks up and returns the {@code UserTitleStatus} enum constant that matches the provided
     * value for form submissions, path parameters, or display purposes. If no match is found, it throws an
     * {@link IllegalArgumentException}.
     * </p>
     *
     * @param value the value representing the title status.
     * @return the corresponding {@code UserTitleStatus} enum constant.
     * @throws IllegalArgumentException if the specified value does not match any known status.
     */
    public static UserTitleStatus fromValue(String value) {
        for (UserTitleStatus status : values()) {
            if (status.formValue.equals(value) || status.pathValue.equals(value) || status.displayValue.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + value);
    }

    /**
     * Returns the form parameter name for the anime status.
     * <p>
     * This method returns the string "status[]" as the name of the parameter to be used in HTTP requests
     * where multiple status values may be selected.
     * </p>
     *
     * @return the form parameter name, "status[]".
     */
    @Override
    public String getFormParameter() {
        return "status[]";
    }

    /**
     * Returns the path parameter name for the anime status.
     * <p>
     * This method returns the string "status" as the name of the path parameter to be used in HTTP requests.
     * </p>
     *
     * @return the path parameter name, "status".
     */
    @Override
    public String getPathParameter() {
        return "status";
    }
}
