package com.github.kosmateus.shinden.user.mapper;

import com.github.kosmateus.shinden.user.request.ImportMalListRequest;
import com.github.kosmateus.shinden.utils.jsoup.BaseDocumentMapper;
import com.google.common.collect.ImmutableMap;
import org.jsoup.nodes.Document;

import java.util.Map;

public class UserImportMalListMapper extends BaseDocumentMapper {

    public Map<String, String> map(Document document, ImportMalListRequest request) {
        return ImmutableMap.of(
                "file_send", "true",
                "MAX_FILE_SIZE", mapper.with(document)
                        .selectFirst("input[name=MAX_FILE_SIZE]")
                        .attr("value")
                        .orThrowWithCode("max-file-size"),
                "import-type", request.getImportType().getValue(),
                "creator[data]", mapper.with(document)
                        .selectFirst("input[name~=creator\\[data\\]]")
                        .attr("value")
                        .orThrowWithCode("creator-data"),
                "creator[post_page_id]", mapper.with(document)
                        .selectFirst("input[name~=creator\\[post_page_id\\]]")
                        .attr("value")
                        .orThrowWithCode("creator-post-page-id")
        );
    }

    @Override
    protected String getMapperCode() {
        return "user.import-mal-list";
    }
}
