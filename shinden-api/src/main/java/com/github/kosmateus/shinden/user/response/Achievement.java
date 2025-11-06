package com.github.kosmateus.shinden.user.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * Represents an achievement earned by a user.
 * <p>
 * The {@code Achievement} class encapsulates the details of a user's achievement, including the title,
 * description, progress, image URL, date of achievement, level, type, and whether it is a previous achievement.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@Builder
public class Achievement {

    /**
     * The title of the achievement.
     */
    private final String title;

    /**
     * A brief description of the achievement.
     */
    private final String description;

    /**
     * The progress made towards completing the achievement, represented as a percentage.
     */
    private final Float progress;

    /**
     * The URL of the image associated with the achievement.
     */
    private final String imageUrl;

    /**
     * The date on which the achievement was earned.
     */
    private final LocalDate date;

    /**
     * The level of the achievement (e.g., "Bronze", "Silver", "Gold").
     */
    private final String level;

    /**
     * The type of achievement (e.g., "Challenge", "Milestone").
     */
    private final String type;

    /**
     * Indicates whether this achievement is a previous one (e.g., from a previous period or level).
     */
    private final Boolean previous;
}
