package com.github.kosmateus.shinden.user.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.kosmateus.shinden.user.common.enums.PageMainMenu;
import com.github.kosmateus.shinden.user.common.enums.PageTheme;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Class representing the settings for a user's page.
 * <p>
 * The {@code PageSettings} class encapsulates the settings related to the appearance and configuration
 * of a user's page, including the theme and main menu options. The class uses the {@link Builder} pattern
 * to facilitate the creation and configuration of its instances.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@Builder
@ToString
@AllArgsConstructor(onConstructor_ = @JsonCreator)
public class PageSettings {

    /**
     * The theme of the user's page.
     * <p>
     * This field holds the {@link PageTheme} enum value, which determines the visual theme applied
     * to the user's page.
     * </p>
     */
    private final PageTheme pageTheme;

    /**
     * The configuration of the main menu on the user's page.
     * <p>
     * This field holds the {@link PageMainMenu} enum value, which controls the display and layout
     * of the main menu on the user's page.
     * </p>
     */
    private final PageMainMenu pageMainMenu;
}
