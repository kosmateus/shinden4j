package com.github.kosmateus.shinden.user.mapper;

import com.github.kosmateus.shinden.user.request.AvatarUrlUpdateRequest;
import com.github.kosmateus.shinden.user.request.UpdatePasswordRequest;
import com.github.kosmateus.shinden.utils.jsoup.BaseDocumentMapper;
import com.google.common.collect.ImmutableMap;
import org.jsoup.nodes.Document;

import java.util.Map;

public class UserAccountMapper extends BaseDocumentMapper {


    public Map<String, String> mapToUpdateAvatar(Document document) {
        return ImmutableMap.of(
                "csrf", mapper.with(document)
                        .selectFirst("form#TabLocal input[name=csrf]")
                        .attr("value")
                        .orThrowWithCode("file.csrf"),
                "MAX_FILE_SIZE", mapper.with(document)
                        .selectFirst("form#TabLocal input[name=MAX_FILE_SIZE]")
                        .attr("value")
                        .orThrowWithCode("file.max-file-size")

        );
    }

    public Map<String, String> mapToUpdateAvatar(Document document, AvatarUrlUpdateRequest request) {
        return ImmutableMap.of(
                "csrf", mapper.with(document)
                        .selectFirst("form#TabUrl input[name=csrf]")
                        .attr("value")
                        .orThrowWithCode("url.csrf"),
                "avatar-url", request.getAvatarUrl()
        );
    }

    public Map<String, String> mapToDeleteAvatar(Document entity) {
        return ImmutableMap.of(
                "csrf", mapper.with(entity)
                        .selectFirst("form#TabDelete input[name=csrf]")
                        .attr("value")
                        .orThrowWithCode("delete.csrf"),
                "delete", "delete"
        );
    }

    public Map<String, String> mapToUpdatePassword(Document entity, UpdatePasswordRequest request) {
        return ImmutableMap.of(
                "csrf", mapper.with(entity)
                        .selectFirst("form input[name=csrf]")
                        .attr("value")
                        .orThrowWithCode("password.csrf"),
                "passwd_old", request.getCurrentPassword(),
                "passwd", request.getNewPassword(),
                "passwd_new2", request.getNewPassword()
        );
    }

    @Override
    protected String getMapperCode() {
        return "user.account.avatar.edit";
    }
}
