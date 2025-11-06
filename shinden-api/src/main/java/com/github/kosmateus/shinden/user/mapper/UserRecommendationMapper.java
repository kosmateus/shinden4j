package com.github.kosmateus.shinden.user.mapper;

import com.github.kosmateus.shinden.user.response.Recommendation;
import com.github.kosmateus.shinden.utils.PatternMatcher;
import com.github.kosmateus.shinden.utils.jsoup.BaseDocumentMapper;
import com.github.kosmateus.shinden.utils.jsoup.DocumentMapperEngine.WithElementStep;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.Map;

import static com.github.kosmateus.shinden.constants.ShindenConstants.MEDIA_ID_MATCHER;
import static com.github.kosmateus.shinden.constants.ShindenConstants.MEDIA_URL_TYPE_MATCHER;
import static com.github.kosmateus.shinden.constants.ShindenConstants.PROGRESS_BAR_MATCHER;

public class UserRecommendationMapper extends BaseDocumentMapper {


    private static final String HEADER_SELECTOR = "div.media.clearfix";
    private static final String CONTENT_SELECTOR = "p:not(.text-center):not(:empty)";
    private static final PatternMatcher ID_PATTERN = PatternMatcher.match("recommendation_(\\d+)", 1);

    public List<Recommendation> map(Document document) {
        return mapper.with(document)
                .selectFirst("div.l-container-col2.box-userprofile")
                .select(HEADER_SELECTOR)
                .and()
                .select(CONTENT_SELECTOR)
                .mapTo(this::mapToRecommendation)
                .orThrowWithCode("list");

    }

    private Recommendation mapToRecommendation(Map<String, Element> elements) {
        return Recommendation.builder()
                .id(mapper(elements).attr("id").pattern(ID_PATTERN).toLong().orThrowWithCode("id"))
                .mediaId(mapper(elements).selectFirst("div.title:nth-of-type(1) > a").attr("href").pattern(MEDIA_ID_MATCHER).toLong().orThrowWithCode("media-id"))
                .mediaUrlType(mapper(elements).selectFirst("div.title:nth-of-type(1) > a").attr("href").pattern(MEDIA_URL_TYPE_MATCHER).orThrowWithCode("media-url-type"))
                .mediaTitle(mapper(elements).selectFirst("div.title:nth-of-type(1) > a").text().orThrowWithCode("media-title"))
                .mediaImageUrl(mapper(elements).selectFirst("img").attr("src").orThrowWithCode("media-image-url"))
                .forMediaId(mapper(elements).selectFirst("div.title:nth-of-type(3) > a").attr("href").pattern(MEDIA_ID_MATCHER).toLong().orThrowWithCode("for-media-id"))
                .forMediaUrlType(mapper(elements).selectFirst("div.title:nth-of-type(3) > a").attr("href").pattern(MEDIA_URL_TYPE_MATCHER).orThrowWithCode("for-media-url-type"))
                .forMediaTitle(mapper(elements).selectFirst("div.title:nth-of-type(3) > a").text().orThrowWithCode("for-media-title"))
                .date(mapper(elements).selectFirst("span.add-date").attr("title").toLocalDateTime().orThrowWithCode("date"))
                .rating(mapper(elements).selectFirst("div.progressbar-value").attr("style").pattern(PROGRESS_BAR_MATCHER).toInteger().orThrowWithCode("rating"))
                .description(mapper.with(elements.get(CONTENT_SELECTOR)).text().keepNewLines().orThrowWithCode("description"))
                .build();
    }

    private WithElementStep mapper(Map<String, Element> elements) {
        return mapper.with(elements.get(HEADER_SELECTOR));
    }

    @Override
    protected String getMapperCode() {
        return "user.recommendations";
    }
}
