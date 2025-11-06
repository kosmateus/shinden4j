package com.github.kosmateus.shinden.anime.request;

/**
 * Interface representing a query parameter for anime search requests.
 *
 * <p>The {@code AnimeSearchQueryParam} interface defines the methods required to get the query parameter's
 * value and the corresponding search parameter name used in HTTP requests for anime search operations.</p>
 *
 * @version 1.0.0
 */
public interface AnimeSearchQueryParam {

    /**
     * Returns the query value to be used in the anime search request.
     *
     * @return the query value as a {@link String}
     */
    String getQueryValue();

    /**
     * Returns the search parameter name to be used in the anime search request.
     *
     * @return the search parameter name as a {@link String}
     */
    String getAnimeSearchQueryParameter();
}
