package com.github.kosmateus.shinden.user.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents an overview of a user's profile, including personal details, achievements, and media-related statistics.
 * <p>
 * The {@code UserOverview} class provides a comprehensive summary of a user's profile on the platform. It includes
 * personal details such as username, avatar, and rank, as well as statistics related to anime and manga consumption.
 * Additionally, it includes lists of recent updates, favorite media, characters, people, and user comments.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@Builder
@ToString
public class UserOverview {

    /**
     * The unique identifier for the user.
     */
    private final Long id;

    /**
     * The username of the user.
     */
    private final String username;

    /**
     * The URL of the user's avatar image.
     */
    private final String avatarUrl;

    /**
     * The number of achievements earned by the user.
     */
    private final Integer achievements;

    /**
     * The timestamp when the user was last online.
     */
    private final LocalDateTime lastOnline;

    /**
     * The rank of the user within the community.
     */
    private final String rank;

    /**
     * The date when the user joined the platform.
     */
    private final LocalDateTime joinDate;

    /**
     * The user's overall score or reputation within the community.
     */
    private final Integer score;

    /**
     * The preferred language of the user.
     */
    private final String language;

    /**
     * A brief description or "about me" section for the user.
     */
    private final String about;

    /**
     * Statistics related to the user's anime consumption.
     */
    private final MediaStatistics animeStatistics;

    /**
     * Statistics related to the user's manga consumption.
     */
    private final MediaStatistics mangaStatistics;

    /**
     * A list of recent updates to the user's anime list.
     */
    private final List<ListItemOverview> animeListUpdates;

    /**
     * A list of recent updates to the user's manga list.
     */
    private final List<ListItemOverview> mangaListUpdates;

    /**
     * A list of the user's favorite anime titles.
     */
    private final List<FavouriteMediaItem> favouriteAnime;

    /**
     * A list of the user's favorite manga titles.
     */
    private final List<FavouriteMediaItem> favouriteManga;

    /**
     * A list of the user's favorite characters.
     */
    private final List<EntityOverview> favouriteCharacters;

    /**
     * A list of the user's favorite people (e.g., voice actors, creators).
     */
    private final List<EntityOverview> favouritePeople;

    /**
     * A list of comments made by the user.
     */
    private final List<Comment> comments;
}
