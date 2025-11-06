package com.github.kosmateus.shinden.user.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.kosmateus.shinden.user.common.enums.ChapterLanguage;
import com.github.kosmateus.shinden.user.common.enums.ChapterStatus;
import com.github.kosmateus.shinden.user.common.enums.StatusAutoChange;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * Class representing the settings for a user's manga list.
 * <p>
 * The {@code MangaListSettings} class encapsulates various settings related to the user's manga list,
 * including preferred chapter languages, reading status preferences, and the option for automatic status changes.
 * The class uses the {@link Builder} pattern to facilitate the creation and configuration of its instances.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@Builder
@ToString
@AllArgsConstructor(onConstructor_ = @JsonCreator)
public class MangaListSettings {

    /**
     * The list of preferred chapter languages.
     * <p>
     * This field holds a list of {@link ChapterLanguage} enums, representing the user's preferred languages
     * for manga chapters.
     * </p>
     */
    private final List<ChapterLanguage> chapterLanguages;

    /**
     * The list of manga reading statuses.
     * <p>
     * This field holds a list of {@link ChapterStatus} enums, representing the user's preferred statuses
     * for managing manga in their list.
     * </p>
     */
    private final List<ChapterStatus> mangaReadStatus;

    /**
     * The option for automatic status changes.
     * <p>
     * This field holds a {@link StatusAutoChange} enum, indicating whether the user's manga statuses should
     * update automatically based on their reading progress.
     * </p>
     */
    private final StatusAutoChange statusAutoChange;
}
