package com.github.kosmateus.shinden.user.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Class representing the settings for estimated read times.
 * <p>
 * The {@code ReadTimeSettings} class encapsulates the user's settings for estimating
 * read times for manga chapters and visual novel chapters. These settings allow users
 * to customize the time expected to read a single chapter of each type.
 * The class uses the {@link Builder} pattern to simplify the creation and configuration of its instances.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@Builder
@ToString
@AllArgsConstructor(onConstructor_ = @JsonCreator)
public class ReadTimeSettings {

    /**
     * The estimated read time for a manga chapter, in minutes.
     * <p>
     * This field represents the user's preferred time estimate for reading one chapter of a manga.
     * It is stored as an {@link Integer} value, representing the time in minutes.
     * </p>
     */
    private final Integer mangaChapterReadTime;

    /**
     * The estimated read time for a visual novel chapter, in minutes.
     * <p>
     * This field represents the user's preferred time estimate for reading one chapter of a visual novel.
     * It is stored as an {@link Integer} value, representing the time in minutes.
     * </p>
     */
    private final Integer visualNovelChapterReadTime;
}
