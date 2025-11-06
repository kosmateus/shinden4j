package com.github.kosmateus.shinden.anime;

import com.github.kosmateus.shinden.anime.request.AnimeSearchRequest.SortType;
import com.github.kosmateus.shinden.http.request.HttpRequest;
import com.github.kosmateus.shinden.http.request.HttpRequest.KeyValue;
import com.github.kosmateus.shinden.http.response.ResponseHandler;
import com.github.kosmateus.shinden.http.rest.HttpClient;
import com.github.kosmateus.shinden.request.FixedPageable;
import com.github.kosmateus.shinden.request.Sort;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.github.kosmateus.shinden.constants.ShindenConstants.SHINDEN_URL;

/**
 * Client for handling HTTP requests related to anime search operations.
 *
 * <p>The {@code AnimeHttpClient} class provides methods to send HTTP requests to the server
 * for retrieving anime search results based on the specified search criteria and pagination details.</p>
 *
 * @version 1.0.0
 */
@RequiredArgsConstructor(onConstructor_ = @__(@Inject))
class AnimeHttpClient {
    private final HttpClient httpClient;

    /**
     * Searches for anime titles using the specified query parameters and pagination details.
     *
     * @param params   the list of {@link KeyValue} query parameters representing the search criteria
     * @param pageable the {@link FixedPageable} object containing pagination details, such as page number and sort type
     * @return a {@link ResponseHandler} containing the HTTP response as a string
     */
    ResponseHandler<String> searchAnime(List<KeyValue> params, FixedPageable<SortType> pageable) {
        List<KeyValue> queryParams = new ArrayList<>(params);
        if (pageable != null) {
            queryParams.add(KeyValue.of("page", String.valueOf(pageable.getPageNumber())));
            if (pageable.getSort().isPresent()) {
                pageable.getSort().get().getOrders().forEach(order -> {
                    queryParams.add(KeyValue.of(order.getProperty().getSortParameter(), order.getProperty().getSortValue()));
                    queryParams.add(KeyValue.of("sort_order", order.getDirection() == Sort.Direction.ASC ? "asc" : "desc"));
                });
            }
        }
        return httpClient.get(HttpRequest.builder()
                .target(SHINDEN_URL)
                .path("/series")
                .queryParams(queryParams)
                .build(), String.class);
    }

    ResponseHandler<String> getSummary(Long animeId) {
        return httpClient.get(HttpRequest.builder()
                .target(SHINDEN_URL)
                .path("/titles/" + animeId)
                .build(), String.class);
    }

    ResponseHandler<String> getEpisodes(Long animeId) {
        return httpClient.get(HttpRequest.builder()
                .headers(
                        ImmutableMap.of(
                                "accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
                                "accept-language", "en-US,en;q=0.9,pl-PL;q=0.8,pl;q=0.7",
                                "cache-control", "max-age=0",
                                "user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/132.0.0.0 Safari/537.36"
                        )
                )
                .target(SHINDEN_URL)

                .path("/titles/" + animeId + "/all-episodes")
                .build(), String.class);
    }

    ResponseHandler<String> getCharacters(Long animeId) {
        return httpClient.get(HttpRequest.builder()
                .target(SHINDEN_URL)
                .path("/titles/" + animeId + "/characters")
                .build(), String.class);
    }

    ResponseHandler<String> getRecommendations(Long animeId) {
        return httpClient.get(HttpRequest.builder()
                .target(SHINDEN_URL)
                .path("/titles/" + animeId + "/recommendations")
                .build(), String.class);
    }

    ResponseHandler<String> getReviews(Long animeId) {
        return httpClient.get(HttpRequest.builder()
                .target(SHINDEN_URL)
                .path("/titles/" + animeId + "/reviews")
                .build(), String.class);
    }

    ResponseHandler<String> getStats(Long animeId) {
        return httpClient.get(HttpRequest.builder()
                .target(SHINDEN_URL)
                .path("/titles/" + animeId + "/stats")
                .build(), String.class);
    }
}
