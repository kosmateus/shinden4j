package com.github.kosmateus.shinden.user.common.enums;

import com.github.kosmateus.shinden.http.request.FormParam;
import com.github.kosmateus.shinden.i18n.Translatable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing the status of an anime in a user's list.
 * <p>
 * The {@code AnimeStatus} enum provides different statuses that an anime can have in a user's
 * list, such as "in progress", "completed", "skipped", etc. This enum implements both
 * {@link Translatable} and {@link FormParam} interfaces, allowing the status to be used as a
 * form parameter in requests, and supporting translation of the status labels using the
 * {@link Translatable} interface.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@RequiredArgsConstructor
public enum AnimeStatus implements Translatable, FormParam {

    /**
     * Represents an anime that is currently being watched.
     */
    IN_PROGRESS("in progress", "user.settings.edit.anime-status.in-progress"),

    /**
     * Represents an anime that has been completed.
     */
    COMPLETED("completed", "user.settings.edit.anime-status.completed"),

    /**
     * Represents an anime that has been skipped.
     */
    SKIP("skip", "user.settings.edit.anime-status.skip"),

    /**
     * Represents an anime that is on hold.
     */
    HOLD("hold", "user.settings.edit.anime-status.hold"),

    /**
     * Represents an anime that has been dropped.
     */
    DROPPED("dropped", "user.settings.edit.anime-status.dropped"),

    /**
     * Represents an anime that is planned to be watched.
     */
    PLAN("plan", "user.settings.edit.anime-status.plan");

    private final String value;
    private final String translationKey;

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
    public String getParameter() {
        return "status[]";
    }
}
