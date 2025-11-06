package com.github.kosmateus.shinden.anime;

import com.github.kosmateus.shinden.anime.mapper.AnimeDetailsMapper;
import com.github.kosmateus.shinden.anime.mapper.AnimeSearchMapper;
import com.github.kosmateus.shinden.anime.request.AnimeSearchRequest;
import com.github.kosmateus.shinden.anime.request.AnimeSearchRequest.SortType;
import com.github.kosmateus.shinden.anime.response.AnimeDetails;
import com.github.kosmateus.shinden.anime.response.AnimeSearchResult;
import com.github.kosmateus.shinden.http.response.ResponseHandler;
import com.github.kosmateus.shinden.request.FixedPageable;
import com.github.kosmateus.shinden.request.Sort;
import com.github.kosmateus.shinden.response.Page;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.kosmateus.shinden.anime.mapper.AnimeDetailsMapper.CHARACTERS;
import static com.github.kosmateus.shinden.anime.mapper.AnimeDetailsMapper.EPISODES;
import static com.github.kosmateus.shinden.anime.mapper.AnimeDetailsMapper.RECOMMENDATIONS;
import static com.github.kosmateus.shinden.anime.mapper.AnimeDetailsMapper.REVIEWS;
import static com.github.kosmateus.shinden.anime.mapper.AnimeDetailsMapper.STATS;
import static com.github.kosmateus.shinden.anime.mapper.AnimeDetailsMapper.SUMMARY;
import static com.github.kosmateus.shinden.utils.ResponseHandlerValidator.validateResponse;

/**
 * Implementation of the {@link AnimeApi} interface for handling anime-related operations.
 *
 * <p>The {@code AnimeApiImpl} class provides methods to search for anime titles and handles the response
 * from the server, parsing and mapping the response data to the appropriate model objects.</p>
 *
 * @version 1.0.0
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Inject))
public class AnimeApiImpl implements AnimeApi {
    private static final String LAST_PAGE_REGEX = "<li>[^>]*>(\\d+)<[^<]*<\\/li>\\s*<li\\s+class=[\"']pagination-next[\"'][^>]*>(?:[\\s\\S]*?)<\\/li>\\s*<li\\s+class=[\"']pagination-next[\"'][^>]*>(?:[\\s\\S]*?)<\\/li>\\s*<\\/ul>(?![\\s\\S]*?<li>)";
    private static final Pattern LAST_PAGE_PATTERN = Pattern.compile(LAST_PAGE_REGEX);


    private final AnimeHttpClient httpClient;
    private final AnimeSearchMapper searchMapper;
    private final AnimeDetailsMapper detailsMapper;

    /**
     * Searches for anime titles based on the specified request and pagination details.
     *
     * @param request  the {@link AnimeSearchRequest} containing the search criteria
     * @param pageable the {@link FixedPageable} object containing pagination details, such as page size and sort type
     * @return a {@link Page} of {@link AnimeSearchResult} containing the search results
     */
    @Override
    public Page<AnimeSearchResult> searchAnime(AnimeSearchRequest request, FixedPageable<SortType> pageable) {
        ResponseHandler<String> requestedPage = httpClient.searchAnime(request.toQueryParams(), pageable);
        validateResponse(requestedPage);
        String page = requestedPage.getEntity();
        return searchMapper.map(page, getLastPageData(request, pageable, page), pageable);
    }

    /**
     * Searches for anime titles based on the specified request without pagination.
     *
     * @param request the {@link AnimeSearchRequest} containing the search criteria
     * @return a {@link Page} of {@link AnimeSearchResult} containing the search results
     */
    @Override
    public Page<AnimeSearchResult> searchAnime(AnimeSearchRequest request) {
        return searchAnime(request, FixedPageable.of(1));
    }

    @Override
    public AnimeDetails getAnime(Long animeId) {
        ExecutorService executor = Executors.newFixedThreadPool(6);
        Map<String, Future<Document>> futures = new HashMap<>();

        try {
            futures.put(SUMMARY, executor.submit(() -> parseToDocument(httpClient.getSummary(animeId))));
            futures.put(EPISODES, executor.submit(() -> parseToDocument(httpClient.getEpisodes(animeId))));
            futures.put(CHARACTERS, executor.submit(() -> parseToDocument(httpClient.getCharacters(animeId))));
            futures.put(RECOMMENDATIONS, executor.submit(() -> parseToDocument(httpClient.getRecommendations(animeId))));
            futures.put(REVIEWS, executor.submit(() -> parseToDocument(httpClient.getReviews(animeId))));
            futures.put(STATS, executor.submit(() -> parseToDocument(httpClient.getStats(animeId))));

            Map<String, Document> results = new HashMap<>();

            for (Map.Entry<String, Future<Document>> entry : futures.entrySet()) {
                try {
                    Document document = entry.getValue().get();
                    results.put(entry.getKey(), document);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted while waiting for document parsing for key: " + entry.getKey(), e);
                } catch (ExecutionException e) {
                    throw new RuntimeException("Error occurred while parsing document for key: " + entry.getKey(), e);

                }
            }
            return detailsMapper.map(animeId, results);
        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    private Document parseToDocument(ResponseHandler<String> response) {
        validateResponse(response);
        return Jsoup.parse(response.getEntity());
    }


    /**
     * Retrieves the last page data based on the search request and pagination details.
     *
     * @param request  the {@link AnimeSearchRequest} containing the search criteria
     * @param pageable the {@link FixedPageable} object containing pagination details
     * @param page     the current page content as a string
     * @return a {@link Pair} containing the last page number and its content
     */
    private Pair<Integer, String> getLastPageData(AnimeSearchRequest request, FixedPageable<SortType> pageable, String page) {
        Matcher matcher = LAST_PAGE_PATTERN.matcher(page);
        if (matcher.find()) {
            int pageNumber = Integer.parseInt(matcher.group(1));
            if (pageNumber == pageable.getPageNumber()) {
                return Pair.of(pageNumber, page);
            }
            FixedPageable<SortType> lastPagePageable = FixedPageable.of(pageNumber, pageable.getSort().orElse(Sort.unsorted()));
            ResponseHandler<String> lastPage = httpClient.searchAnime(request.toQueryParams(), lastPagePageable);
            validateResponse(lastPage);
            return Pair.of(pageNumber, lastPage.getEntity());
        }
        return null;
    }
}
