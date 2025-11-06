package com.github.kosmateus.shinden.anime.response;

import com.github.kosmateus.shinden.common.enums.TitleStatus;
import com.github.kosmateus.shinden.common.enums.TitleType;
import com.github.kosmateus.shinden.common.enums.UrlType;
import com.github.kosmateus.shinden.enums.tag.Genre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Represents the result of an anime search.
 *
 * <p>The {@code AnimeSearchResult} class contains detailed information about an anime title, including
 * its ID, URL type, image URL, title, genres, type, number of episodes, rating, and status.</p>
 *
 * @version 1.0.0
 */
@Getter
@Builder
@AllArgsConstructor
public class AnimeSearchResult {

    /**
     * The unique identifier for the anime.
     */
    private final Long id;

    /**
     * The type of URL associated with the anime.
     */
    private final UrlType urlType;

    /**
     * The URL of the anime's image.
     */
    private final String imageUrl;

    /**
     * The title of the anime.
     */
    private final String title;

    /**
     * The list of genres associated with the anime.
     */
    private final List<Genre> genres;

    /**
     * The type of the anime (e.g., TV, OVA, Movie).
     */
    private final TitleType type;

    /**
     * The number of episodes in the anime.
     */
    private final Integer episodes;

    /**
     * The rating of the anime.
     */
    private final Rating rating;

    /**
     * The current status of the anime (e.g., Currently Airing, Finished).
     */
    private final TitleStatus status;

    /**
     * Represents the rating details for an anime.
     *
     * <p>The {@code Rating} class contains various rating categories such as top, overall, story,
     * graphics, music, and characters.</p>
     */
    @Getter
    @Builder
    @AllArgsConstructor
    public static final class Rating {

        /**
         * The top rating score for the anime.
         */
        private final Float top;

        /**
         * The overall rating score for the anime.
         */
        private final Float overall;

        /**
         * The story rating score for the anime.
         */
        private final Float story;

        /**
         * The graphics rating score for the anime.
         */
        private final Float graphics;

        /**
         * The music rating score for the anime.
         */
        private final Float music;

        /**
         * The characters rating score for the anime.
         */
        private final Float characters;
    }
}
