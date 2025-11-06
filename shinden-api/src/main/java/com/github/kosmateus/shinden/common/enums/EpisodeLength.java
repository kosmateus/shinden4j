package com.github.kosmateus.shinden.common.enums;

import com.github.kosmateus.shinden.anime.request.AnimeSearchQueryParam;
import com.github.kosmateus.shinden.i18n.Translatable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration representing different episode lengths for tagging purposes in the application.
 *
 * <p>Each episode length is associated with a query value and a translation key for localization
 * purposes, along with predefined query parameters used for API searches.</p>
 *
 * @version 1.0.0
 */
@Getter
@RequiredArgsConstructor
public enum EpisodeLength implements Translatable, AnimeSearchQueryParam {

    LESS_THAN_7("less_7", "episode.length.less-than-7-minutes"),
    BETWEEN_7_AND_18("7_to_18", "episode.length.between-7-and-18-minutes"),
    BETWEEN_19_AND_27("19_to_27", "episode.length.between-19-and-27-minutes"),
    BETWEEN_28_AND_48("28_to_48", "episode.length.between-28-and-48-minutes"),
    MORE_THAN_48("over_48", "episode.length.more-than-48-minutes");

    public static final String ANIME_SEARCH_QUERY_PARAM = "series_length[]";
    private final String queryValue;
    private final String translationKey;
    private final String animeSearchQueryParameter = ANIME_SEARCH_QUERY_PARAM;
}
