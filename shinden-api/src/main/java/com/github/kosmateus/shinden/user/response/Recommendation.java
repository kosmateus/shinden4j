package com.github.kosmateus.shinden.user.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Represents a recommendation made by or for a user regarding a media title.
 * <p>
 * The {@code Recommendation} class encapsulates the details of a media recommendation,
 * including the media being recommended, the media for which the recommendation is made,
 * the date of the recommendation, the rating given, and a description of the recommendation.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@Builder
public class Recommendation {

    /**
     * The unique identifier for the recommendation.
     */
    private final Long id;

    /**
     * The unique identifier for the media being recommended.
     */
    private final Long mediaId;

    /**
     * The URL of the image associated with the recommended media.
     */
    private final String mediaImageUrl;

    /**
     * The type of URL associated with the recommended media (e.g., "anime", "manga").
     */
    private final String mediaUrlType;

    /**
     * The title of the recommended media.
     */
    private final String mediaTitle;

    /**
     * The unique identifier for the media for which the recommendation is made.
     */
    private final Long forMediaId;

    /**
     * The type of URL associated with the media for which the recommendation is made.
     */
    private final String forMediaUrlType;

    /**
     * The title of the media for which the recommendation is made.
     */
    private final String forMediaTitle;

    /**
     * The date when the recommendation was made.
     */
    private final LocalDateTime date;

    /**
     * The rating given to the recommendation, typically as an integer score.
     */
    private final Integer rating;

    /**
     * A description or commentary about the recommendation.
     */
    private final String description;
}
