package com.github.kosmateus.shinden.user.response;

import com.github.kosmateus.shinden.user.common.AddToListSettings;
import com.github.kosmateus.shinden.user.common.AnimeListSettings;
import com.github.kosmateus.shinden.user.common.MangaListSettings;
import com.github.kosmateus.shinden.user.common.PageSettings;
import com.github.kosmateus.shinden.user.common.ReadTimeSettings;
import lombok.Builder;
import lombok.Getter;

/**
 * Class representing the user settings response.
 * <p>
 * The {@code UserSettings} class encapsulates various settings related to the user's account,
 * such as page appearance, read time estimates, and list management preferences.
 * This class is typically used to transfer user settings data in responses.
 * The class uses the {@link Builder} pattern to facilitate its creation.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@Builder
public class UserSettings {

    /**
     * The settings related to the user's page appearance and configuration.
     * <p>
     * This field holds an instance of {@link PageSettings}, representing the user's preferences
     * for page theme and main menu configuration.
     * </p>
     */
    private final PageSettings pageSettings;

    /**
     * The settings for estimated read times.
     * <p>
     * This field holds an instance of {@link ReadTimeSettings}, representing the user's
     * preferred read time estimates for manga and visual novel chapters.
     * </p>
     */
    private final ReadTimeSettings readTimeSettings;

    /**
     * The settings related to adding items to lists.
     * <p>
     * This field holds an instance of {@link AddToListSettings}, representing the user's
     * preferences for the "Add to List" feature, including slider position and visibility.
     * </p>
     */
    private final AddToListSettings addToListSettings;

    /**
     * The settings for managing the user's anime list.
     * <p>
     * This field holds an instance of {@link AnimeListSettings}, representing the user's
     * preferences for anime list management, including subtitle languages, watch statuses,
     * and options for skipping fillers and automatic status changes.
     * </p>
     */
    private final AnimeListSettings animeListSettings;

    /**
     * The settings for managing the user's manga list.
     * <p>
     * This field holds an instance of {@link MangaListSettings}, representing the user's
     * preferences for manga list management, including chapter languages, read statuses,
     * and options for automatic status changes.
     * </p>
     */
    private final MangaListSettings mangaListSettings;
}
