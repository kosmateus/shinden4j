package com.github.kosmateus.shinden.user.mapper;

import com.github.kosmateus.shinden.user.response.Achievement;
import com.github.kosmateus.shinden.user.response.Achievements;
import com.github.kosmateus.shinden.utils.PatternMatcher;
import com.github.kosmateus.shinden.utils.jsoup.BaseDocumentMapper;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.stream.Collectors;

public class UserAchievementsMapper extends BaseDocumentMapper {

    private static final PatternMatcher LEVEL_MATCHER = PatternMatcher.nullableMatch("(?i)level\\s*(.*)", 1);
    private static final PatternMatcher TYPE_MATCHER = PatternMatcher.nullableMatch(
            "(?i)^(.*?)(?=\\blevel\\b|$)", 1);


    public Achievements map(Document document) {
        return Achievements.builder()
                .lastCheck(mapper.with(document)
                        .selectFirst("section.achv h2 span.timeago")
                        .attr("title")
                        .toLocalDateTime()
                        .orThrowWithCode("last-check")
                )
                .achievements(getAchievements(document))
                .build();
    }

    private List<Achievement> getAchievements(Document document) {
        return document.select("section.achv div.achv-entry")
                .stream()
                .map(item -> Achievement.builder()
                        .title(mapper.with(item)
                                .selectFirst("h3")
                                .ownText()
                                .orThrowWithCode("title")
                        )
                        .progress(mapper.with(item)
                                .selectFirst("span span")
                                .attr("style")
                                .replace("width:", "")
                                .replace("%", "")
                                .toFloat()
                                .orThrowWithCode("progress")
                        )
                        .description(mapper.with(item)
                                .selectFirst("p.desc")
                                .text()
                                .orThrowWithCode("description")
                        )
                        .imageUrl(mapper.with(item)
                                .selectFirst("img")
                                .attr("src")
                                .orThrowWithCode("image-url")
                        )
                        .level(mapper.with(item)
                                .selectFirst("p.achv-level")
                                .ownText()
                                .pattern(LEVEL_MATCHER)
                                .orElse(null)
                        )
                        .type(mapper.with(item)
                                .selectFirst("p.achv-level")
                                .text()
                                .pattern(TYPE_MATCHER)
                                .replace(":", "")
                                .orThrowWithCode("type")
                        )
                        .date(mapper.with(item)
                                .selectFirst("h3 > p.achv-level > span.timeago")
                                .attr("title")
                                .toLocalDate()
                                .orThrowWithCode("date")
                        )
                        .previous(item.hasClass("prev"))
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    protected String getMapperCode() {
        return "user.achievements";
    }

}
