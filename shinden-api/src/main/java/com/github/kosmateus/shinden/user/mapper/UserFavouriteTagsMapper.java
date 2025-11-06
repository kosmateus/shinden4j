package com.github.kosmateus.shinden.user.mapper;

import com.github.kosmateus.shinden.user.response.FavouriteTag;
import com.github.kosmateus.shinden.utils.jsoup.BaseDocumentMapper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;

public class UserFavouriteTagsMapper extends BaseDocumentMapper {

    public List<FavouriteTag> map(Document document) {
        return mapper.with(document)
                .selectFirst("table.fav-tags tbody")
                .select("tr")
                .mapTo(this::mapToTag)
                .orThrowWithCode("table");
    }

    private FavouriteTag mapToTag(Element element) {
        return mapper.with(element)
                .select("td")
                .collect()
                .mapTo(td -> FavouriteTag.builder()
                        .id(mapper.with(element).attr("data-tag-id").toLong().orThrowWithCode("table.row.id"))
                        .name(mapper.with(td.get(1)).text().orThrowWithCode("table.row.name"))
                        .lowestRating(mapper.with(td.get(2)).text().toInteger()
                                .orThrowWithCode("table.row.lowest-rating"))
                        .highestRating(mapper.with(td.get(3)).text().toInteger()
                                .orThrowWithCode("table.row.highest-rating"))
                        .titlesCount(mapper.with(td.get(4)).text().toInteger()
                                .orThrowWithCode("table.row.titles-count"))
                        .averageRating(mapper.with(td.get(5)).attr("data-sort-value").toFloat()
                                .orThrowWithCode("table.row.average-rating"))
                        .weightedRating(mapper.with(td.get(6)).attr("data-sort-value").toFloat()
                                .orThrowWithCode("table.row.weighted-rating"))
                        .spentTime(mapper.with(td.get(7)).attr("data-sort-value").toInteger()
                                .orThrowWithCode("table.row.spent-time"))
                        .build())
                .orThrowWithCode("table.row");
    }

    @Override
    protected String getMapperCode() {
        return "user.favourite.tags";
    }
}
