package com.github.kosmateus.shinden.user.response;

import lombok.Builder;
import lombok.Getter;

/**
 * Represents a user's favorite tag and associated statistics.
 * <p>
 * The {@code FavouriteTag} class encapsulates details about a tag that a user has marked as a favorite.
 * This includes the tag's name, ratings, the number of titles associated with the tag, and time spent.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@Builder
public class FavouriteTag {

    /**
     * The unique identifier for the tag.
     */
    private final Long id;

    /**
     * The name of the tag.
     */
    private final String name;

    /**
     * The lowest rating given to any title associated with this tag.
     */
    private final Integer lowestRating;

    /**
     * The highest rating given to any title associated with this tag.
     */
    private final Integer highestRating;

    /**
     * The total number of titles associated with this tag.
     */
    private final Integer titlesCount;

    /**
     * The average rating of titles associated with this tag.
     */
    private final Float averageRating;

    /**
     * The weighted rating of titles associated with this tag, potentially taking into account the user's preferences.
     */
    private final Float weightedRating;

    /**
     * The total time spent on titles associated with this tag, typically in minutes.
     */
    private final Integer spentTime;
}
