package com.github.kosmateus.shinden.user.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.kosmateus.shinden.user.common.enums.SkipFillers;
import com.github.kosmateus.shinden.user.common.enums.StatusAutoChange;
import com.github.kosmateus.shinden.user.common.enums.SubtitlesLanguage;
import com.github.kosmateus.shinden.user.common.enums.UserTitleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * Class representing the settings for a user's anime list.
 * <p>
 * The {@code AnimeListSettings} class encapsulates various settings related to the user's anime list,
 * including subtitle languages, watch status preferences, and options for skipping fillers and automatic
 * status changes. The class uses the {@link Builder} pattern to simplify the creation and configuration
 * of its instances.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@Builder
@ToString
@AllArgsConstructor(onConstructor_ = @JsonCreator)
public class AnimeListSettings {

    /**
     * The list of preferred subtitle languages.
     * <p>
     * Holds a list of {@link SubtitlesLanguage} enums, representing the user's preferred languages for subtitles
     * when watching anime. This setting allows users to customize their viewing experience by selecting
     * languages that they are comfortable with.
     * </p>
     */
    private final List<SubtitlesLanguage> subtitlesLanguages;

    /**
     * The list of anime watch statuses.
     * <p>
     * Holds a list of {@link UserTitleStatus} enums, representing the user's preferred statuses
     * (such as watching, completed, or dropped) for managing anime in their list. This setting helps in
     * filtering and organizing the anime titles based on their viewing progress or interest.
     * </p>
     */
    private final List<UserTitleStatus> animeWatchStatus;

    /**
     * The option to skip filler episodes.
     * <p>
     * Holds a {@link SkipFillers} enum, indicating whether the user prefers to skip filler episodes
     * in their anime list. Filler episodes are often not part of the main storyline, and this option
     * allows users to bypass them for a more streamlined viewing experience.
     * </p>
     */
    private final SkipFillers skipFillers;

    /**
     * The option for automatic status changes.
     * <p>
     * Holds a {@link StatusAutoChange} enum, indicating whether the user's anime statuses should
     * update automatically based on their progress. For example, an anime may change from "Watching"
     * to "Completed" automatically once all episodes are marked as watched.
     * </p>
     */
    private final StatusAutoChange statusAutoChange;
}
