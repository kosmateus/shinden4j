package com.github.kosmateus.shinden.user.common;

import com.github.kosmateus.shinden.user.common.enums.ShowOption;
import com.github.kosmateus.shinden.user.common.enums.SliderPosition;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Class representing the settings for adding items to a list.
 * <p>
 * The {@code AddToListSettings} class encapsulates the user-configurable settings
 * related to the "Add to List" feature. It includes options for setting the slider
 * position and controlling the visibility of the "Add to List" button or feature.
 * The class uses the {@link Builder} pattern to facilitate the creation of its instances.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@ToString
@SuperBuilder
public class AddToListSettings {

    /**
     * The position or limit of items in the slider.
     * <p>
     * This field holds the {@link SliderPosition} value, which determines how many items
     * can be displayed in the slider when adding items to the list.
     * </p>
     */
    private final SliderPosition sliderPosition;

    /**
     * The visibility setting for the "Add to List" feature.
     * <p>
     * This field holds the {@link ShowOption} value, which controls whether the "Add to List"
     * option is shown or hidden in the user interface.
     * </p>
     */
    private final ShowOption showAddToList;
}
