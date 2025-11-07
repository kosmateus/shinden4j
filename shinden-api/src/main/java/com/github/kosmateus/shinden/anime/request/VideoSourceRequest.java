package com.github.kosmateus.shinden.anime.request;

import com.github.kosmateus.shinden.anime.response.VideoSource.VideoQuality;
import com.github.kosmateus.shinden.http.request.SortParam;
import com.github.kosmateus.shinden.request.Sort;
import com.github.kosmateus.shinden.request.Sort.Order;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

import static com.github.kosmateus.shinden.request.Sort.Direction.ASC;
import static com.github.kosmateus.shinden.request.Sort.Direction.DESC;

/**
 * Represents a request for retrieving video sources for a specific anime episode.
 * <p>
 * The {@code VideoSourceRequest} class defines parameters that control how
 * video sources should be fetched, filtered, sorted and limited.
 *
 * @version 1.0.0
 */
@Getter
@Builder
public class VideoSourceRequest {

    /**
     * The unique identifier of the anime.
     * This field is required.
     */
    @NotNull
    private final Long animeId;

    /**
     * The unique identifier of the episode.
     * This field is required.
     */
    @NotNull
    private final Long episodeId;

    /**
     * Filter by specific service names (e.g., "CDA", "Dailymotion").
     */
    @Nullable
    private final Set<String> services;

    /**
     * Filter by video qualities.
     */
    @Nullable
    private final Set<VideoQuality> qualities;

    /**
     * Filter by subtitles languages (e.g., "PL", "EN").
     */
    @Nullable
    private final Set<String> subtitlesLanguages;

    /**
     * Filter by audio languages (e.g., "JP", "EN", "PL").
     */
    @Nullable
    private final Set<String> audioLanguages;

    /**
     * Sorting options for the results.
     * Use {@link Sort#by(Order[])} with {@link SortType} enum.
     * Example: Sort.by(SortType.QUALITY.desc(), SortType.CREATED_AT.asc())
     */
    @Nullable
    private final Sort<SortType> sort;

    /**
     * Available fields for sorting video sources.
     */
    @Getter
    @RequiredArgsConstructor
    public enum SortType implements SortParam<SortType> {
        /**
         * Sort by service name.
         */
        SERVICE("service"),

        /**
         * Sort by video quality.
         */
        QUALITY("quality"),

        /**
         * Sort by subtitles language.
         */
        SUBTITLES_LANGUAGE("subtitles"),

        /**
         * Sort by audio language.
         */
        AUDIO_LANGUAGE("audio"),

        /**
         * Sort by creation date.
         */
        CREATED_AT("created_at");

        private final String value;

        @Override
        public String getSortValue() {
            return value;
        }

        @Override
        public String getSortParameter() {
            return "sort_by";
        }

        @Override
        public Order<SortType> desc() {
            return new Order<>(this, DESC);
        }

        @Override
        public Order<SortType> asc() {
            return new Order<>(this, ASC);
        }
    }

    /**
     * Custom builder class with helper methods for fluent API.
     */
    public static class VideoSourceRequestBuilder {

        /**
         * Adds a service to the filter.
         */
        public VideoSourceRequestBuilder addService(String service) {
            if (this.services == null) {
                this.services = new HashSet<>();
            }
            this.services.add(service);
            return this;
        }

        /**
         * Adds a quality to the filter.
         */
        public VideoSourceRequestBuilder addQuality(VideoQuality quality) {
            if (this.qualities == null) {
                this.qualities = new HashSet<>();
            }
            this.qualities.add(quality);
            return this;
        }

        /**
         * Adds a subtitles language to the filter.
         */
        public VideoSourceRequestBuilder addSubtitlesLanguage(String language) {
            if (this.subtitlesLanguages == null) {
                this.subtitlesLanguages = new HashSet<>();
            }
            this.subtitlesLanguages.add(language);
            return this;
        }

        /**
         * Adds an audio language to the filter.
         */
        public VideoSourceRequestBuilder addAudioLanguage(String language) {
            if (this.audioLanguages == null) {
                this.audioLanguages = new HashSet<>();
            }
            this.audioLanguages.add(language);
            return this;
        }
    }

    /**
     * Checks if any filters are applied.
     */
    public boolean hasFilters() {
        return (services != null && !services.isEmpty()) ||
                (qualities != null && !qualities.isEmpty()) ||
                (subtitlesLanguages != null && !subtitlesLanguages.isEmpty()) ||
                (audioLanguages != null && !audioLanguages.isEmpty());
    }

    /**
     * Checks if sorting is specified.
     */
    public boolean hasSorting() {
        return sort != null && sort.isSorted();
    }
}
