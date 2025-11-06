package com.github.kosmateus.shinden.user.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * Represents an item in a user's manga list.
 * <p>
 * The {@code MangaListItem} class encapsulates detailed information about a manga
 * that is part of a user's manga list. This includes fields such as the manga's title,
 * image URL, user and total scores, chapter counts, dates, and status.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@Builder
@ToString
public class MangaListItem {

    /**
     * The unique identifier for the manga.
     */
    private final Long id;

    /**
     * The title of the manga.
     */
    private final String title;

    /**
     * The URL of the manga's image.
     */
    private final String imageUrl;

    /**
     * The score given by the user to the manga.
     */
    private final Integer userScore;

    /**
     * The total score of the manga.
     */
    private final Integer totalScore;

    /**
     * The score for the plot of the manga.
     */
    private final Integer plotScore;

    /**
     * The score for the characters in the manga.
     */
    private final Integer charactersScore;

    /**
     * The score for the graphics of the manga.
     */
    private final Integer graphicsScore;

    /**
     * The total number of chapters in the manga.
     */
    private final Integer totalChapters;

    /**
     * The number of chapters read by the user.
     */
    private final Integer readChapters;

    /**
     * The date when the user started reading the manga.
     */
    private final LocalDate startDate;

    /**
     * The date when the user finished reading the manga.
     */
    private final LocalDate endDate;

    /**
     * The current status of the manga in the user's list (e.g., Reading, Completed).
     */
    private final String status;

    /**
     * A brief description of the manga.
     */
    private final String description;
}
