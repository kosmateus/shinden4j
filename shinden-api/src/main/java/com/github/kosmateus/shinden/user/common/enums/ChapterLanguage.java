package com.github.kosmateus.shinden.user.common.enums;

import com.github.kosmateus.shinden.http.request.FormParam;
import com.github.kosmateus.shinden.i18n.Translatable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing the language of a manga chapter.
 * <p>
 * The {@code ChapterLanguage} enum provides different language options for manga chapters.
 * This enum implements both {@link Translatable} and {@link FormParam} interfaces, allowing
 * the selected language to be used as a form parameter in HTTP requests, and supporting
 * translation of the language labels using the {@link Translatable} interface.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@RequiredArgsConstructor
public enum ChapterLanguage implements Translatable, FormParam {

    /**
     * Represents no specified language.
     */
    NONE("", "user.settings.edit.subtitle-lang.none"),

    /**
     * Represents Japanese language.
     */
    JAPANESE("jp", "user.settings.edit.subtitle-lang.jp"),

    /**
     * Represents Polish language.
     */
    POLISH("pl", "user.settings.edit.subtitle-lang.pl"),

    /**
     * Represents English language.
     */
    ENGLISH("en", "user.settings.edit.subtitle-lang.en"),

    /**
     * Represents Chinese language.
     */
    CHINESE("cn", "user.settings.edit.subtitle-lang.cn"),

    /**
     * Represents Korean language.
     */
    KOREAN("kr", "user.settings.edit.subtitle-lang.kr");

    private final String formValue;
    private final String translationKey;

    /**
     * Returns the form parameter name for the chapter language.
     * <p>
     * This method returns the string "chap_lang[]" as the name of the parameter to be used in HTTP requests
     * where multiple language options may be selected for manga chapters.
     * </p>
     *
     * @return the form parameter name, "chap_lang[]".
     */
    @Override
    public String getFormParameter() {
        return "chap_lang[]";
    }
}
