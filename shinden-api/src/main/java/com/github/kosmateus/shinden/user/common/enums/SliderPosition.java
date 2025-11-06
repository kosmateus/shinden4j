package com.github.kosmateus.shinden.user.common.enums;

import com.github.kosmateus.shinden.http.request.FormParam;
import com.github.kosmateus.shinden.i18n.Translatable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing the position or limit of items in a slider.
 * <p>
 * The {@code SliderPosition} enum provides different options for setting the position or limit of items
 * displayed in a slider. This enum implements both {@link Translatable} and {@link FormParam} interfaces,
 * allowing the selected position to be used as a form parameter in HTTP requests, and supporting the translation
 * of the position labels using the {@link Translatable} interface.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@RequiredArgsConstructor
public enum SliderPosition implements Translatable, FormParam {

    /**
     * Represents no limit on the number of items in the slider.
     */
    NO_LIMIT("1", "user.settings.edit.slider-position.no-limit"),

    /**
     * Represents a limit of three items in the slider.
     */
    THREE_ITEMS("200", "user.settings.edit.slider-position.three-items"),

    /**
     * Represents a limit of six items in the slider.
     */
    SIX_ITEMS("100", "user.settings.edit.slider-position.six-items"),

    /**
     * Represents a limit of twelve items in the slider.
     */
    TWELVE_ITEMS("50", "user.settings.edit.slider-position.twelve-items");

    private final String formValue;
    private final String translationKey;

    /**
     * Returns the form parameter name for the slider position.
     * <p>
     * This method returns the string "steps" as the name of the parameter to be used in HTTP requests
     * when setting or changing the position or limit of items in a slider.
     * </p>
     *
     * @return the form parameter name, "steps".
     */
    @Override
    public String getFormParameter() {
        return "steps";
    }
}
