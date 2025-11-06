package com.github.kosmateus.shinden.user.response;

import lombok.Builder;
import lombok.Getter;

/**
 * Represents a user's review of a media title.
 * <p>
 * The {@code Review} class encapsulates the details of a review written by a user about a specific
 * media title. It includes information such as the title and ID of the media, URL details, the content
 * of the review, and vote statistics.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@Builder
public class Review {

    /**
     * The unique identifier for the review.
     */
    private final Long id;

    /**
     * The unique identifier for the media being reviewed.
     */
    private final Long mediaId;

    /**
     * The title of the media being reviewed.
     */
    private final String mediaTitle;

    /**
     * The type of URL associated with the media (e.g., "anime", "manga").
     */
    private final String mediaUrlType;

    /**
     * The URL of the image associated with the media being reviewed.
     */
    private final String mediaImageUrl;

    /**
     * The total number of votes the review has received.
     */
    private final Integer totalVotes;

    /**
     * The number of positive votes the review has received.
     */
    private final Integer positiveVotes;

    /**
     * The number of negative votes the review has received.
     */
    private final Integer negativeVotes;

    /**
     * The content of the review.
     */
    private final String content;
}
