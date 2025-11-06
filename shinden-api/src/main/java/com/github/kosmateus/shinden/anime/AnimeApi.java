package com.github.kosmateus.shinden.anime;

import com.github.kosmateus.shinden.anime.request.AnimeSearchRequest;
import com.github.kosmateus.shinden.anime.request.AnimeSearchRequest.SortType;
import com.github.kosmateus.shinden.anime.response.AnimeDetails;
import com.github.kosmateus.shinden.anime.response.AnimeSearchResult;
import com.github.kosmateus.shinden.request.FixedPageable;
import com.github.kosmateus.shinden.response.Page;

/**
 * Interface for the Anime API, providing methods to search for anime titles.
 *
 * <p>The {@code AnimeApi} interface defines methods for searching anime titles based on various
 * criteria specified in the {@link AnimeSearchRequest}. It supports paginated search results
 * using the {@link FixedPageable} parameter, and also provides an overloaded method for non-paginated
 * searches.</p>
 *
 * @version 1.0.0
 */
public interface AnimeApi {

    /**
     * Searches for anime titles based on the specified request and pagination details.
     *
     * @param request  the {@link AnimeSearchRequest} containing the search criteria
     * @param pageable the {@link FixedPageable} object containing pagination details, such as page size and sort type
     * @return a {@link Page} of {@link AnimeSearchResult} containing the search results
     */
    Page<AnimeSearchResult> searchAnime(AnimeSearchRequest request, FixedPageable<SortType> pageable);

    /**
     * Searches for anime titles based on the specified request without pagination.
     *
     * @param request the {@link AnimeSearchRequest} containing the search criteria
     * @return a {@link Page} of {@link AnimeSearchResult} containing the search results
     */
    Page<AnimeSearchResult> searchAnime(AnimeSearchRequest request);

    /**
     * Retrieves detailed information about an anime title.
     *
     * @param animeId the unique identifier of the anime title
     * @return an {@link AnimeDetails} object containing detailed information about the anime title
     */
    AnimeDetails getAnime(Long animeId);
}
