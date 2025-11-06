package com.github.kosmateus.shinden.user.common.enums;

import com.github.kosmateus.shinden.http.request.FormParam;
import com.github.kosmateus.shinden.i18n.Translatable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing the language options for subtitles.
 * <p>
 * The {@code SubtitlesLanguage} enum provides different language options for subtitles in a user's settings.
 * This enum implements both {@link Translatable} and {@link FormParam} interfaces, allowing the selected
 * subtitle language to be used as a form parameter in HTTP requests, and supporting the translation of the
 * language labels using the {@link Translatable} interface.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@RequiredArgsConstructor
public enum SubtitlesLanguage implements Translatable, FormParam {

    /**
     * Represents no specified subtitle language.
     */
    NONE("", "user.settings.edit.subtitle-lang.none"),

    /**
     * Represents Japanese subtitles.
     */
    JAPANESE("jp", "user.settings.edit.subtitle-lang.jp"),

    /**
     * Represents Polish subtitles.
     */
    POLISH("pl", "user.settings.edit.subtitle-lang.pl"),

    /**
     * Represents English subtitles.
     */
    ENGLISH("en", "user.settings.edit.subtitle-lang.en"),

    /**
     * Represents Chinese subtitles.
     */
    CHINESE("cn", "user.settings.edit.subtitle-lang.cn"),

    /**
     * Represents Korean subtitles.
     */
    KOREAN("kr", "user.settings.edit.subtitle-lang.kr");

    private final String formValue;
    private final String translationKey;

    /**
     * Returns the form parameter name for the subtitles language.
     * <p>
     * This method returns the string "lang[]" as the name of the parameter to be used in HTTP requests
     * when setting or changing the subtitle language in a user's settings.
     * </p>
     *
     * @return the form parameter name, "lang[]".
     */
    @Override
    public String getFormParameter() {
        return "lang[]";
    }
}
