package com.github.kosmateus.shinden.anime.mapper;

import com.github.kosmateus.shinden.anime.request.AnimeSearchRequest.SortType;
import com.github.kosmateus.shinden.anime.response.AnimeSearchResult;
import com.github.kosmateus.shinden.anime.response.AnimeSearchResult.Rating;
import com.github.kosmateus.shinden.common.enums.TitleStatus;
import com.github.kosmateus.shinden.common.enums.TitleType;
import com.github.kosmateus.shinden.common.enums.UrlType;
import com.github.kosmateus.shinden.enums.tag.Genre;
import com.github.kosmateus.shinden.request.FixedPageable;
import com.github.kosmateus.shinden.response.Page;
import com.github.kosmateus.shinden.response.PageImpl;
import com.github.kosmateus.shinden.utils.jsoup.BaseDocumentMapper;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.github.kosmateus.shinden.constants.ShindenConstants.MEDIA_ID_MATCHER;
import static com.github.kosmateus.shinden.constants.ShindenConstants.MEDIA_URL_TYPE_MATCHER;

public class AnimeSearchMapper extends BaseDocumentMapper {

    public static final String LI_RATING_COL = "li.ratings-col";
    public static final String LI_RATE_TOP = "li.rate-top";
    public static final String ANIME_SEARCH_RESULT_ROW = "section.anime-list > section > article > ul.div-row";

    public Page<AnimeSearchResult> map(String requestedPage, Pair<Integer, String> lastPageData, FixedPageable<SortType> pageable) {
        List<AnimeSearchResult> animeSearchResults = mapper.with(Jsoup.parse(requestedPage))
                .select(ANIME_SEARCH_RESULT_ROW)
                .mapTo(this::mapAnime)
                .orElse(Collections.emptyList());
        if (lastPageData != null) {
            Integer lastPage = lastPageData.getLeft();
            Integer items = countItemsOnLastPage(lastPageData.getRight());

            return new PageImpl<>(animeSearchResults, pageable, (long) (lastPage - 1) * pageable.getPageSize() + items);
        }
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        if (pageNumber == 1) {
            return new PageImpl<>(animeSearchResults, pageable, animeSearchResults.size());
        }
        if (animeSearchResults.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
        return new PageImpl<>(animeSearchResults, pageable, (long) (pageNumber - 1) * pageSize + animeSearchResults.size());
    }

    @Override
    protected String getMapperCode() {
        return "anime.search";
    }

    @Override
    protected Map<Class<?>, Function<String, ?>> typeMappers() {
        return ImmutableMap.of(
                UrlType.class, UrlType::fromValue,
                Genre.class, Genre::fromValue,
                TitleType.class, TitleType::fromValue,
                TitleStatus.class, TitleStatus::fromValue
        );
    }

    private AnimeSearchResult mapAnime(Element document) {
        return AnimeSearchResult.builder()
                .id(mapper.with(document).selectFirst("li.desc-col > h3 > a").attr("href").pattern(MEDIA_ID_MATCHER).toLong().orThrowWithCode("id"))
                .urlType(mapper.with(document).selectFirst("li.desc-col > h3 > a").attr("href").pattern(MEDIA_URL_TYPE_MATCHER).mapTo(UrlType.class).orThrowWithCode("url-type"))
                .imageUrl(mapper.with(document).selectFirst("li.cover-col > a").attr("href").orThrowWithCode("image-url"))
                .title(mapper.with(document).selectFirst("li.desc-col > h3 > a").text().orThrowWithCode("title"))
                .genres(mapper.with(document).select("li.desc-col > ul > li > a").mapTo(this::map).orElse(Collections.emptyList()))
                .type(mapper.with(document).selectFirst("li.title-kind-col").text().mapTo(TitleType.class).orThrowWithCode("type"))
                .episodes(mapper.with(document).selectFirst("li.episodes-col").text().toInteger().orThrowWithCode("episodes"))
                .rating(mapToRating(document))
                .status(mapper.with(document).selectFirst("li.title-status-col").text().mapTo(TitleStatus.class).orThrowWithCode("status"))
                .build();
    }

    private Rating mapToRating(Element document) {
        return Rating.builder()
                .top(mapper.with(document).selectFirst(LI_RATE_TOP).text().replace("Brak", "").replace(",", ".").toFloat().orElse(null))
                .overall(mapper.with(document).selectFirst(LI_RATING_COL + " div.rating.rating-total span").ownText().replace(",", ".").toFloat().orElse(null))
                .story(mapper.with(document).selectFirst(LI_RATING_COL + " div.rating.rating-story span").ownText().replace(",", ".").toFloat().orElse(null))
                .graphics(mapper.with(document).selectFirst(LI_RATING_COL + " div.rating.rating-graphics span").ownText().replace(",", ".").toFloat().orElse(null))
                .music(mapper.with(document).selectFirst(LI_RATING_COL + " div.rating.rating-music span").ownText().replace(",", ".").toFloat().orElse(null))
                .characters(mapper.with(document).selectFirst(LI_RATING_COL + " div.rating.rating-titlecahracters span").ownText().replace(",", ".").toFloat().orElse(null))
                .build();
    }

    private Genre map(Element element) {
        return mapper.with(element).attr("href").pattern(MEDIA_ID_MATCHER).mapTo(Genre.class).orThrowWithCode("genre");
    }

    private Integer countItemsOnLastPage(String lastPage) {
        return Jsoup.parse(lastPage).select(ANIME_SEARCH_RESULT_ROW).size();
    }
}
