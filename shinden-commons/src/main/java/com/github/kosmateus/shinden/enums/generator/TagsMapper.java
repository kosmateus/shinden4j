package com.github.kosmateus.shinden.enums.generator;

import com.github.kosmateus.shinden.utils.jsoup.BaseDocumentMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;

import java.util.List;

import static com.github.kosmateus.shinden.constants.ShindenConstants.GENERIC_ID_KEY_MATCHER;
import static com.github.kosmateus.shinden.constants.ShindenConstants.GENERIC_ID_MATCHER;

class TagsMapper extends BaseDocumentMapper {

    public List<TagData> mapTags(Document document, String tagUrl) {
        return mapper.with(document)
                .selectFirst("ul.tags")
                .select("li")
                .mapTo(element -> TagData.of(
                        mapper.with(element).selectFirst("a").attr("href").pattern(GENERIC_ID_MATCHER.apply(tagUrl)).toInteger().orThrowWithCode("id"),
                        mapper.with(element).selectFirst("a").attr("href").pattern(GENERIC_ID_KEY_MATCHER.apply(tagUrl)).orThrowWithCode("key"),
                        mapper.with(element).selectFirst("a").text().orThrowWithCode("translation"),
                        mapper.with(element).selectFirst("a").attr("href").pattern(GENERIC_ID_KEY_MATCHER.apply(tagUrl)).orThrowWithCode("translationKey")
                ))
                .orThrowWithCode("tags");
    }

    @Override
    protected String getMapperCode() {
        return "tags";
    }


    @Getter
    @RequiredArgsConstructor(staticName = "of")
    public static class TagData {
        private final Integer id;
        private final String key;
        private final String translation;
        private final String translationKey;
    }
}



