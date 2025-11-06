package com.github.kosmateus.shinden.user.mapper;

import com.github.kosmateus.shinden.user.response.Review;
import com.github.kosmateus.shinden.utils.PatternMatcher;
import com.github.kosmateus.shinden.utils.jsoup.BaseDocumentMapper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;

import static com.github.kosmateus.shinden.constants.ShindenConstants.MEDIA_ID_MATCHER;
import static com.github.kosmateus.shinden.constants.ShindenConstants.MEDIA_URL_TYPE_MATCHER;

public class UserReviewsMapper extends BaseDocumentMapper {

    private static final PatternMatcher REVIEW_ID_MATCHER = PatternMatcher.match("/reviews/(\\d+)", 1);

    public List<Review> map(Document document) {
        return mapper.with(document)
                .selectFirst("div.l-container-col2.box-userprofile section")
                .select("article")
                .mapTo(this::mapToReview)
                .orThrowWithCode("list");
    }

    private Review mapToReview(Element element) {
        Integer totalVotes = mapper.with(element)
                .selectFirst("span.review-likes span.binary-rate-count")
                .text()
                .toInteger()
                .orThrowWithCode("total-votes");
        Integer positiveVotes = mapper.with(element)
                .selectFirst("span.review-likes span.binary-rate-up")
                .text()
                .toInteger()
                .orThrowWithCode("positive-votes");
        return Review.builder()
                .id(mapper.with(element)
                        .selectFirst("h3 > a")
                        .attr("href")
                        .pattern(REVIEW_ID_MATCHER)
                        .toLong()
                        .orThrowWithCode("id")
                )
                .mediaImageUrl(mapper.with(element)
                        .selectFirst("img")
                        .attr("src")
                        .orThrowWithCode("media-image-url")
                )
                .mediaUrlType(mapper.with(element)
                        .selectFirst("h3 > a")
                        .attr("href")
                        .pattern(MEDIA_URL_TYPE_MATCHER)
                        .orThrowWithCode("media-url-type")
                )
                .mediaId(mapper.with(element)
                        .selectFirst("h3 > a")
                        .attr("href")
                        .pattern(MEDIA_ID_MATCHER)
                        .toLong()
                        .orThrowWithCode("media-id")
                )
                .mediaTitle(mapper.with(element)
                        .selectFirst("h3 > a")
                        .text()
                        .orThrowWithCode("media-title")
                )
                .totalVotes(totalVotes)
                .positiveVotes(positiveVotes)
                .negativeVotes(totalVotes - positiveVotes)
                .content(mapper.with(element)
                        .selectFirst("p")
                        .text()
                        .keepNewLines()
                        .orThrowWithCode("content")
                )
                .build();
    }

    @Override
    protected String getMapperCode() {
        return "user.reviews";
    }
}
