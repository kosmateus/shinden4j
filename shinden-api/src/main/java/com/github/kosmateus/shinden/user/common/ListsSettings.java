package com.github.kosmateus.shinden.user.common;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Represents the settings for a user's anime and manga lists.
 * <p>
 * The {@code ListsSettings} class encapsulates the settings related to both anime and manga lists
 * for a user. It provides separate configurations for each type of list, allowing for customization
 * of how anime and manga lists are managed.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@ToString
@SuperBuilder
public class ListsSettings {

    /**
     * The settings related to the user's anime list.
     * <p>
     * This field contains configurations specific to how the user's anime list is managed,
     * such as display preferences and sorting options.
     * </p>
     */
    private final AnimeListSettings animeListSettings;

    /**
     * The settings related to the user's manga list.
     * <p>
     * This field contains configurations specific to how the user's manga list is managed,
     * such as display preferences and sorting options.
     * </p>
     */
    private final MangaListSettings mangaListSettings;
}
