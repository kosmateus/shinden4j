package com.github.kosmateus.shinden.common.enums;

import com.github.kosmateus.shinden.anime.request.AnimeSearchQueryParam;
import com.github.kosmateus.shinden.i18n.Translatable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration representing different episode number ranges for tagging purposes in the application.
 *
 * <p>Each episode number range is associated with a query value and a translation key for localization
 * purposes, along with predefined query parameters used for API searches.</p>
 *
 * @version 1.0.0
 */
@Getter
@RequiredArgsConstructor
public enum EpisodesNumber implements Translatable, AnimeSearchQueryParam {

    ONLY_1("only_1", "episode.number.only-1"),
    BETWEEN_2_AND_14("2_to_14", "episode.number.between-2-and-14"),
    BETWEEN_15_AND_28("15_to_28", "episode.number.between-15-and-28"),
    BETWEEN_29_AND_100("29_to_100", "episode.number.between-29-and-100"),
    MORE_THAN_100("over_100", "episode.number.more-than-100");


    public static final String ANIME_SEARCH_QUERY_PARAM = "series_number[]";
    private final String queryValue;
    private final String translationKey;
    private final String animeSearchQueryParameter = ANIME_SEARCH_QUERY_PARAM;
}
