package com.github.kosmateus.shinden.anime;

import com.github.kosmateus.shinden.anime.mapper.AnimeDetailsMapper;
import com.github.kosmateus.shinden.anime.mapper.AnimeSearchMapper;
import com.github.kosmateus.shinden.anime.mapper.VideoSourceMapper;
import com.github.kosmateus.shinden.anime.request.AnimeSearchRequest;
import com.github.kosmateus.shinden.anime.request.AnimeSearchRequest.SortType;
import com.github.kosmateus.shinden.anime.request.VideoSourceRequest;
import com.github.kosmateus.shinden.anime.response.AnimeDetails;
import com.github.kosmateus.shinden.anime.response.AnimeSearchResult;
import com.github.kosmateus.shinden.anime.response.VideoSource;
import com.github.kosmateus.shinden.auth.SessionManager;
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

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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
    private final VideoSourceMapper videoSourceMapper;
    private final SessionManager sessionManager;

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
            if (sessionManager.isSuccessfullyAuthenticated()) {
                futures.put(STATS, executor.submit(() -> parseToDocument(httpClient.getStats(animeId))));
            }

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

    @Override
    public List<VideoSource> getVideoSources(VideoSourceRequest request) {
        Document episodeVideoSources = parseToDocument(httpClient.getVideoSources(request.getAnimeId(), request.getEpisodeId()));
        List<VideoSource> videoSources = videoSourceMapper.intialMapping(episodeVideoSources);
        videoSources = filterVideoSources(videoSources, request);
        return sortVideoSources(videoSources, request.getSort());
    }

    @Override
    public List<String> videoSourceUrl(Long sourceId) {
        try {
            // Get load player time
            ResponseHandler<String> loadTimeResponse = httpClient.getLoadPlayerTime(sourceId);
            validateResponse(loadTimeResponse);

            // Parse wait time in seconds
            int waitTimeSeconds = Integer.parseInt(loadTimeResponse.getEntity().trim());

            // Wait for the specified time
            if (waitTimeSeconds > 0) {
                TimeUnit.SECONDS.sleep(waitTimeSeconds);
            }

            // Get player HTML
            ResponseHandler<String> playerResponse = httpClient.getPlayer(sourceId);
            validateResponse(playerResponse);

            // Extract iframe src URL
            return videoSourceMapper.extractIframeSrc(parseToDocument(playerResponse));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Interrupted while waiting for load player time for source ID: " + sourceId, e);
            return null;
        } catch (Exception e) {
            log.error("Failed to fetch URL for video source ID: " + sourceId, e);
            return null;
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

    /**
     * Filters video sources based on the request criteria.
     *
     * @param videoSources the list of video sources to filter
     * @param request      the {@link VideoSourceRequest} containing filter criteria
     * @return filtered list of video sources
     */
    private List<VideoSource> filterVideoSources(List<VideoSource> videoSources, VideoSourceRequest request) {
        return videoSources.stream()
                .filter(source -> request.getServices() == null ||
                        request.getServices().isEmpty() ||
                        request.getServices().stream().anyMatch(s -> s.equalsIgnoreCase(source.getService())))
                .filter(source -> request.getQualities() == null ||
                        request.getQualities().isEmpty() ||
                        request.getQualities().contains(source.getQuality()))
                .filter(source -> request.getSubtitlesLanguages() == null ||
                        request.getSubtitlesLanguages().isEmpty() ||
                        request.getSubtitlesLanguages().stream().anyMatch(lang -> lang.equalsIgnoreCase(source.getSubtitlesLanguage())))
                .filter(source -> request.getAudioLanguages() == null ||
                        request.getAudioLanguages().isEmpty() ||
                        request.getAudioLanguages().stream().anyMatch(lang -> lang.equalsIgnoreCase(source.getAudioLanguage())))
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Sorts video sources based on the provided sort criteria.
     *
     * @param videoSources the list of video sources to sort
     * @param sort         the {@link Sort} object containing sorting criteria
     * @return sorted list of video sources
     */
    private List<VideoSource> sortVideoSources(List<VideoSource> videoSources, @Nullable Sort<VideoSourceRequest.SortType> sort) {
        if (sort == null || sort.isUnsorted()) {
            return videoSources;
        }

        Comparator<VideoSource> comparator = null;

        for (Sort.Order<VideoSourceRequest.SortType> order : sort.getOrders()) {
            Comparator<VideoSource> fieldComparator = getComparatorForField(order.getProperty());

            if (order.getDirection() == Sort.Direction.DESC) {
                fieldComparator = fieldComparator.reversed();
            }

            comparator = comparator == null ? fieldComparator : comparator.thenComparing(fieldComparator);
        }

        if (comparator != null) {
            videoSources.sort(comparator);
        }

        return videoSources;
    }

    /**
     * Returns a comparator for the specified sort field.
     *
     * @param sortType the field to sort by
     * @return comparator for the specified field
     */
    private Comparator<VideoSource> getComparatorForField(VideoSourceRequest.SortType sortType) {
        switch (sortType) {
            case SERVICE:
                return Comparator.comparing(VideoSource::getService, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER));
            case QUALITY:
                return Comparator.comparing(VideoSource::getQuality, Comparator.nullsLast(Comparator.naturalOrder()));
            case SUBTITLES_LANGUAGE:
                return Comparator.comparing(VideoSource::getSubtitlesLanguage, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER));
            case AUDIO_LANGUAGE:
                return Comparator.comparing(VideoSource::getAudioLanguage, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER));
            case CREATED_AT:
                return Comparator.comparing(VideoSource::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder()));
            default:
                return Comparator.comparing(VideoSource::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder()));
        }
    }
}
