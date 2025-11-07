package com.github.kosmateus.shinden.anime;

import com.github.kosmateus.shinden.anime.request.AnimeSearchRequest;
import com.github.kosmateus.shinden.anime.request.AnimeSearchRequest.SortType;
import com.github.kosmateus.shinden.anime.request.VideoSourceRequest;
import com.github.kosmateus.shinden.anime.response.AnimeDetails;
import com.github.kosmateus.shinden.anime.response.AnimeSearchResult;
import com.github.kosmateus.shinden.anime.response.VideoSource;
import com.github.kosmateus.shinden.request.FixedPageable;
import com.github.kosmateus.shinden.response.Page;

import javax.validation.constraints.NotNull;
import java.util.List;

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
    AnimeDetails getAnime(@NotNull Long animeId);


    /**
     * Retrieves video sources based on the specified request.
     *
     * @param request the {@link VideoSourceRequest} containing the criteria for fetching video sources
     * @return a list of {@link VideoSource} objects matching the request criteria
     */
    List<VideoSource> getVideoSources(@NotNull VideoSourceRequest request);

    /**
     * Retrieves the video source URLs for the specified source ID.
     *
     * <p><strong>Note:</strong> This method may take longer to respond due to server-side requirements.
     * The server enforces a mandatory waiting period before providing access to the video source.
     * The method automatically handles this delay by waiting for the required time.</p>
     *
     * <p>The process involves:</p>
     * <ul>
     *   <li>Fetching the required waiting time from the server</li>
     *   <li>Waiting for the specified duration (plus a 2-second buffer)</li>
     *   <li>Retrieving the player HTML containing the video source URLs</li>
     *   <li>Extracting iframe source URLs from the player</li>
     * </ul>
     *
     * @param sourceId the unique identifier of the video source
     * @return a list of video source URLs extracted from the player iframe, or {@code null} if:
     * <ul>
     *   <li>the thread was interrupted during the waiting period</li>
     *   <li>an error occurred while fetching the video source</li>
     * </ul>
     */
    List<String> videoSourceUrl(@NotNull Long sourceId);

}
