package com.github.kosmateus.shinden.user.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a collection of achievements earned by a user.
 * <p>
 * The {@code Achievements} class encapsulates a list of {@link Achievement} objects representing the
 * achievements earned by a user. It also includes the timestamp of the last check when the achievements
 * were retrieved or updated.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@Builder
public class Achievements {

    /**
     * The timestamp of the last check when the achievements were retrieved or updated.
     */
    private final LocalDateTime lastCheck;

    /**
     * A list of {@link Achievement} objects representing the user's achievements.
     */
    private final List<Achievement> achievements;
}
