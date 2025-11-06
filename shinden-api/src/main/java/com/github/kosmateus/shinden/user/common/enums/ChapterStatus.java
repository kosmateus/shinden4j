package com.github.kosmateus.shinden.user.common.enums;

import com.github.kosmateus.shinden.http.request.FormParam;
import com.github.kosmateus.shinden.i18n.Translatable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing the status of a manga chapter in a user's list.
 * <p>
 * The {@code ChapterStatus} enum provides various status options that a manga chapter can have in a user's list,
 * such as "in progress", "completed", "skipped", etc. This enum implements both {@link Translatable} and
 * {@link FormParam} interfaces, allowing the selected status to be used as a form parameter in HTTP requests,
 * and supporting translation of the status labels using the {@link Translatable} interface.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@RequiredArgsConstructor
public enum ChapterStatus implements Translatable, FormParam {

    /**
     * Represents a manga chapter that is currently being read.
     */
    IN_PROGRESS("in progress", "user.settings.edit.chapter-status.in-progress"),

    /**
     * Represents a manga chapter that has been completed.
     */
    COMPLETED("completed", "user.settings.edit.chapter-status.completed"),

    /**
     * Represents a manga chapter that has been skipped.
     */
    SKIP("skip", "user.settings.edit.chapter-status.skip"),

    /**
     * Represents a manga chapter that is on hold.
     */
    HOLD("hold", "user.settings.edit.chapter-status.hold"),

    /**
     * Represents a manga chapter that has been dropped.
     */
    DROPPED("dropped", "user.settings.edit.chapter-status.dropped"),

    /**
     * Represents a manga chapter that is planned to be read.
     */
    PLAN("plan", "user.settings.edit.chapter-status.plan");

    private final String formValue;
    private final String translationKey;

    /**
     * Returns the form parameter name for the chapter status.
     * <p>
     * This method returns the string "chap_status[]" as the name of the parameter to be used in HTTP requests
     * where multiple status options may be selected for manga chapters.
     * </p>
     *
     * @return the form parameter name, "chap_status[]".
     */
    @Override
    public String getFormParameter() {
        return "chap_status[]";
    }
}
