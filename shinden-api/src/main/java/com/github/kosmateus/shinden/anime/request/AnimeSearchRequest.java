package com.github.kosmateus.shinden.anime.request;

import com.github.kosmateus.shinden.common.enums.EpisodeLength;
import com.github.kosmateus.shinden.common.enums.EpisodesNumber;
import com.github.kosmateus.shinden.common.enums.TitleStatus;
import com.github.kosmateus.shinden.common.enums.TitleType;
import com.github.kosmateus.shinden.enums.Tag;
import com.github.kosmateus.shinden.http.request.HttpRequest.KeyValue;
import com.github.kosmateus.shinden.http.request.SortParam;
import com.github.kosmateus.shinden.request.Sort.Order;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.kosmateus.shinden.request.Sort.Direction.ASC;
import static com.github.kosmateus.shinden.request.Sort.Direction.DESC;

/**
 * Class representing a request for searching anime titles.
 *
 * <p>The {@code AnimeSearchRequest} class encapsulates various parameters and filters used for querying anime titles.
 * It includes methods for converting the request into query parameters suitable for use in HTTP requests.</p>
 *
 * @version 1.0.0
 */
@Getter
@Builder
public class AnimeSearchRequest {

    public static final AnimeSearchRequest EMPTY = AnimeSearchRequest.builder().build();

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * The starting letter for filtering anime titles.
     * <p>
     * This parameter limits the search results to anime titles that begin with the specified letter.
     * For example, setting this to 'A' will only include anime titles that start with 'A'.
     * </p>
     */
    private final Letter letter;

    /**
     * The search query to look for anime titles.
     * <p>
     * This field is used to perform a textual search based on the provided keyword or phrase.
     * The search can be matched against the anime title, description, or other searchable fields.
     * </p>
     */
    private final String search;

    /**
     * The type of search to be performed (e.g., contains, equals).
     * <p>
     * Specifies how the search query should be interpreted: whether the search should look for
     * exact matches, partial matches, or other types of textual matching. This helps refine the search criteria.
     * </p>
     */
    private final SearchType searchType;

    /**
     * The set of tags to be excluded from the search results.
     * <p>
     * These tags represent certain attributes or categories that should not appear in the search results.
     * For example, excluding the "Horror" tag will prevent anime with this genre from being listed.
     * </p>
     */
    private final Set<Tag> excludedTags;

    /**
     * The set of tags to be included in the search results.
     * <p>
     * These tags define specific genres, themes, or attributes that the search results must match.
     * Only anime that include all or some of these tags will be returned, depending on the tag search type.
     * </p>
     */
    private final Set<Tag> includedTags;

    /**
     * The type of tag search to be performed (e.g., all tags, at least one tag).
     * <p>
     * Determines whether the search should include anime that match all specified tags or at least one of them.
     * This option allows for more flexible or strict filtering of search results.
     * </p>
     */
    private final TagSearchType tagsSearchType;

    /**
     * The start date for filtering anime by release date.
     * <p>
     * Only anime released on or after this date will be included in the search results.
     * This is useful for narrowing down anime based on when they started airing.
     * </p>
     */
    private final LocalDate startDate;

    /**
     * The end date for filtering anime by release date.
     * <p>
     * Only anime released on or before this date will be included in the search results.
     * This allows users to filter out newer anime beyond a certain date.
     * </p>
     */
    private final LocalDate endDate;

    /**
     * The precision level of the date filter (e.g., year, month, day).
     * <p>
     * Specifies the granularity of the date filtering. For example, setting the precision to "year"
     * will filter anime based only on the year of release, ignoring the month and day.
     * </p>
     */
    private final DatePrecision datePrecision;

    /**
     * The set of title types to filter anime (e.g., TV, OVA, Movie).
     * <p>
     * Defines the types of anime to include in the search results. This could be used to filter
     * out specific types of content, such as movies or TV series.
     * </p>
     */
    private final Set<TitleType> titleTypes;

    /**
     * The set of title statuses to filter anime (e.g., Currently Airing, Finished).
     * <p>
     * Filters anime based on their release status. This is useful for searching for anime that are
     * currently airing, have finished airing, or are scheduled to air in the future.
     * </p>
     */
    private final Set<TitleStatus> titleStatuses;

    /**
     * The set of episodes number ranges to filter anime.
     * <p>
     * Specifies the range or ranges of episodes that the search should consider. This can help
     * in finding shorter series or those with a specific episode count.
     * </p>
     */
    private final Set<EpisodesNumber> episodesNumbers;

    /**
     * The set of episode lengths to filter anime.
     * <p>
     * Defines the acceptable lengths of episodes (in minutes) for the search results.
     * For example, this can be used to filter only short-form or long-form anime.
     * </p>
     */
    private final Set<EpisodeLength> episodeLengths;

    /**
     * The minimum number of episodes for filtering anime.
     * <p>
     * Filters anime that have at least this number of episodes. Useful for excluding short series
     * or for searching for more extended series.
     * </p>
     */
    private final Integer episodesNumberFrom;

    /**
     * The maximum number of episodes for filtering anime.
     * <p>
     * Filters anime that do not exceed this number of episodes. Useful for finding shorter series
     * or for setting a cap on the length of the series.
     * </p>
     */
    private final Integer episodesNumberTo;

    /**
     * Whether to include only anime titles that have at least one episode available online.
     * <p>
     * If set to {@code true}, only anime that have at least one episode available for streaming or download
     * online will be included in the search results.
     * </p>
     */
    private final boolean atLeastOneEpisodeOnline;

    /**
     * Whether to exclude anime titles that are already on the user's list.
     * <p>
     * If set to {@code true}, the search results will exclude any anime that the user has already
     * added to their list, helping to discover new content.
     * </p>
     */
    private final boolean withoutTitlesOnMyList;

    /**
     * Whether to exclude completed anime titles that are already on the user's list.
     * <p>
     * If set to {@code true}, the search will exclude any anime that the user has completed watching,
     * allowing them to find new or ongoing series.
     * </p>
     */
    private final boolean withoutCompletedTitlesOnMyList;

    public static AnimeSearchRequestBuilder search(String search) {
        return AnimeSearchRequest.builder().search(search);
    }

    /**
     * Converts the {@code AnimeSearchRequest} into a list of query parameters for HTTP requests.
     *
     * @return a list of {@link KeyValue} representing the query parameters
     */
    public List<KeyValue> toQueryParams() {
        List<KeyValue> queryParams = new ArrayList<>(Arrays.asList(
                Objects.nonNull(letter) ? KeyValue.of(letter.getQueryParameter(), letter.getQueryValue()) : null,
                Objects.nonNull(search) ? KeyValue.of("search", search) : null,
                Objects.nonNull(searchType) ? KeyValue.of(searchType.getQueryParameter(), searchType.getQueryValue()) : null,
                getGenresQueryParam(),
                getTagsSearchTypeQueryParam(),
                Objects.nonNull(startDate) ? KeyValue.of("year_from", startDate.format(DATE_FORMATTER)) : null,
                Objects.nonNull(endDate) ? KeyValue.of("year_to", endDate.format(DATE_FORMATTER)) : null,
                getDatePrecisionQueryParam()));
        queryParams.addAll(getTitleTypesQueryParam());
        queryParams.addAll(getTitleStatusesQueryParam());
        queryParams.addAll(getEpisodesNumbersQueryParam());
        queryParams.addAll(getEpisodeLengthsQueryParam());
        queryParams.add(atLeastOneEpisodeOnline ? KeyValue.of("one_online", "true") : null);
        queryParams.add(withoutTitlesOnMyList ? KeyValue.of("not_on_list", "true") : null);
        queryParams.add(withoutCompletedTitlesOnMyList ? KeyValue.of("not_saw", "true") : null);
        return queryParams.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    private List<KeyValue> getTitleTypesQueryParam() {
        if (titleTypes == null || titleTypes.isEmpty()) {
            return Stream.of(TitleType.values())
                    .map(value -> KeyValue.of(TitleType.ANIME_SEARCH_QUERY_PARAM, value.getQueryValue()))
                    .collect(Collectors.toList());
        }
        return titleTypes.stream()
                .map(value -> KeyValue.of(value.getAnimeSearchQueryParameter(), value.getQueryValue()))
                .collect(Collectors.toList());
    }

    private List<KeyValue> getTitleStatusesQueryParam() {
        if (titleStatuses == null || titleStatuses.isEmpty()) {
            return Stream.of(TitleStatus.values())
                    .map(value -> KeyValue.of(TitleStatus.ANIME_SEARCH_QUERY_PARAM, value.getQueryValue()))
                    .collect(Collectors.toList());
        }
        return titleStatuses.stream()
                .map(value -> KeyValue.of(value.getAnimeSearchQueryParameter(), value.getQueryValue()))
                .collect(Collectors.toList());
    }

    private List<KeyValue> getEpisodesNumbersQueryParam() {
        if (episodesNumberFrom != null && episodesNumberTo != null) {
            return Collections.singletonList(KeyValue.of(EpisodesNumber.ANIME_SEARCH_QUERY_PARAM, episodesNumberFrom + "_to_" + episodesNumberTo));
        } else if (episodesNumberFrom != null) {
            return Collections.singletonList(KeyValue.of(EpisodesNumber.ANIME_SEARCH_QUERY_PARAM, "over_" + episodesNumberFrom));
        } else if (episodesNumberTo != null) {
            return Collections.singletonList(KeyValue.of(EpisodesNumber.ANIME_SEARCH_QUERY_PARAM, "less_" + episodesNumberTo));
        }

        if (episodesNumbers == null || episodesNumbers.isEmpty()) {
            return Stream.of(EpisodesNumber.values())
                    .map(value -> KeyValue.of(EpisodesNumber.ANIME_SEARCH_QUERY_PARAM, value.getQueryValue()))
                    .collect(Collectors.toList());
        }
        return episodesNumbers.stream()
                .map(value -> KeyValue.of(value.getAnimeSearchQueryParameter(), value.getQueryValue()))
                .collect(Collectors.toList());
    }

    private List<KeyValue> getEpisodeLengthsQueryParam() {
        if (episodeLengths == null || episodeLengths.isEmpty()) {
            return Stream.of(EpisodeLength.values())
                    .map(value -> KeyValue.of(EpisodeLength.ANIME_SEARCH_QUERY_PARAM, value.getQueryValue()))
                    .collect(Collectors.toList());
        }
        return episodeLengths.stream()
                .map(value -> KeyValue.of(value.getAnimeSearchQueryParameter(), value.getQueryValue()))
                .collect(Collectors.toList());
    }

    private KeyValue getDatePrecisionQueryParam() {
        if (datePrecision != null) {
            return KeyValue.of(datePrecision.getQueryParameter(), datePrecision.getQueryValue());
        } else if (startDate != null || endDate != null) {
            return KeyValue.of(DatePrecision.QUERY_PARAMETER, DatePrecision.DAY.getQueryValue());
        }
        return null;
    }

    private KeyValue getGenresQueryParam() {
        if ((includedTags == null || includedTags.isEmpty()) && (excludedTags == null || excludedTags.isEmpty())) {
            return null;
        } else if (includedTags != null && !includedTags.isEmpty() && excludedTags == null) {
            return KeyValue.of("genres", includedTags.stream().map(tag -> "i" + tag.getQueryValue()).collect(Collectors.joining(";")));
        } else if (includedTags == null) {
            return KeyValue.of("genres", excludedTags.stream().map(tag -> "e" + tag.getQueryValue()).collect(Collectors.joining(";")));
        } else {
            return KeyValue.of("genres", includedTags.stream().map(tag -> "i" + tag.getQueryValue()).collect(Collectors.joining(";")) + ";" + excludedTags.stream().map(tag -> "e" + tag.getQueryValue()).collect(Collectors.joining(";")));
        }
    }

    private KeyValue getTagsSearchTypeQueryParam() {
        if (tagsSearchType != null) {
            return KeyValue.of(tagsSearchType.getQueryParameter(), tagsSearchType.getQueryValue());
        } else if (includedTags != null && !includedTags.isEmpty() || excludedTags != null && !excludedTags.isEmpty()) {
            return KeyValue.of("genres-type", "all");
        }
        return null;
    }

    @Getter
    @RequiredArgsConstructor
    public enum DatePrecision implements AnimeSearchQueryParam {
        YEAR("1"),
        MONTH("2"),
        DAY("3");
        static final String QUERY_PARAMETER = "start_date_precision";

        private final String queryValue;
        private final String queryParameter = QUERY_PARAMETER;
        private final String animeSearchQueryParameter = queryParameter;
    }

    @Getter
    @RequiredArgsConstructor
    public enum TagSearchType implements AnimeSearchQueryParam {
        ALL("all"),
        AT_LEAST_ONE("one");

        static final String QUERY_PARAMETER = "genres-type";
        private final String queryValue;
        private final String queryParameter = QUERY_PARAMETER;
        private final String animeSearchQueryParameter = queryParameter;
    }


    @Getter
    @RequiredArgsConstructor
    public enum SearchType implements AnimeSearchQueryParam {
        CONTAINS("contains"),
        EQUALS("equals");

        private final String queryValue;
        private final String queryParameter = "type";
        private final String animeSearchQueryParameter = queryParameter;
    }


    @Getter
    @RequiredArgsConstructor
    public enum Letter implements AnimeSearchQueryParam {
        A("A"),
        B("B"),
        C("C"),
        D("D"),
        E("E"),
        F("F"),
        G("G"),
        H("H"),
        I("I"),
        J("J"),
        K("K"),
        L("L"),
        M("M"),
        N("N"),
        O("O"),
        P("P"),
        Q("Q"),
        R("R"),
        S("S"),
        T("T"),
        U("U"),
        V("V"),
        W("W"),
        X("X"),
        Y("Y"),
        Z("Z"),
        HASH("#");

        private final String queryValue;
        private final String queryParameter = "letter";
        private final String animeSearchQueryParameter = queryParameter;
    }

    @Getter
    @RequiredArgsConstructor
    public enum SortType implements SortParam<SortType> {
        TITLE("desc"),
        TYPE("type"),
        EPISODES("multimedia"),
        STATUS("status"),
        TOP_RATED("ranking-rate");

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

    public static class AnimeSearchRequestBuilder {

        public AnimeSearchRequestBuilder addExcludedTag(Tag tag) {
            if (this.excludedTags == null) {
                this.excludedTags = new HashSet<>();
            }
            this.excludedTags.add(tag);
            return this;
        }

        public AnimeSearchRequestBuilder addIncludedTag(Tag tag) {
            if (this.includedTags == null) {
                this.includedTags = new HashSet<>();
            }
            this.includedTags.add(tag);
            return this;
        }

        public AnimeSearchRequestBuilder addTitleType(TitleType titleType) {
            if (this.titleTypes == null) {
                this.titleTypes = new HashSet<>();
            }
            this.titleTypes.add(titleType);
            return this;
        }

        public AnimeSearchRequestBuilder addTitleStatus(TitleStatus titleStatus) {
            if (this.titleStatuses == null) {
                this.titleStatuses = new HashSet<>();
            }
            this.titleStatuses.add(titleStatus);
            return this;
        }

        public AnimeSearchRequestBuilder addEpisodesNumber(EpisodesNumber episodesNumber) {
            if (this.episodesNumbers == null) {
                this.episodesNumbers = new HashSet<>();
            }
            this.episodesNumbers.add(episodesNumber);
            this.episodesNumberFrom = null;
            this.episodesNumberTo = null;
            return this;
        }

        public AnimeSearchRequestBuilder addEpisodeLength(EpisodeLength episodeLength) {
            if (this.episodeLengths == null) {
                this.episodeLengths = new HashSet<>();
            }
            this.episodeLengths.add(episodeLength);
            return this;
        }

        public AnimeSearchRequestBuilder episodesNumberFrom(Integer episodesNumberFrom) {
            if (episodesNumberFrom != null) {
                this.episodesNumbers.clear();
            }
            this.episodesNumberFrom = episodesNumberFrom;
            return this;
        }

        public AnimeSearchRequestBuilder episodesNumberTo(Integer episodesNumberTo) {
            if (episodesNumberTo != null) {
                this.episodesNumbers.clear();
            }
            this.episodesNumberTo = episodesNumberTo;
            return this;
        }
    }
}
