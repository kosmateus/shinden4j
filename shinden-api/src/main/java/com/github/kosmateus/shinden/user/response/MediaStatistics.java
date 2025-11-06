package com.github.kosmateus.shinden.user.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Represents a user's media consumption statistics.
 * <p>
 * The {@code MediaStatistics} class encapsulates various statistics related to a user's media consumption,
 * such as total time spent, mean score, and the status of different media (e.g., in progress, completed, dropped).
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@Builder
@ToString
public class MediaStatistics {

    /**
     * The total time spent on media, typically measured in minutes.
     */
    private final Integer totalTime;

    /**
     * The average score given by the user across all media.
     */
    private final Float meanScore;

    /**
     * The number of media items currently in progress.
     */
    private final Integer inProgress;

    /**
     * The number of media items completed by the user.
     */
    private final Integer completed;

    /**
     * The number of media items skipped by the user.
     */
    private final Integer skip;

    /**
     * The number of media items currently on hold by the user.
     */
    private final Integer hold;

    /**
     * The number of media items dropped by the user.
     */
    private final Integer dropped;

    /**
     * The number of media items planned to be watched or read by the user.
     */
    private final Integer planned;

    /**
     * The total number of media titles tracked by the user.
     */
    private final Integer totalTitles;

    /**
     * The total number of segments (e.g., episodes, chapters) tracked by the user.
     */
    private final Integer totalSegments;

    /**
     * The total number of revisits to media titles by the user.
     */
    private final Integer totalRevisits;
}
