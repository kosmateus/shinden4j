package com.github.kosmateus.shinden.user.mapper;

import com.github.kosmateus.shinden.user.common.enums.UserGender;
import com.github.kosmateus.shinden.user.request.UserInformationRequest;
import com.github.kosmateus.shinden.user.response.UserInformation;
import com.github.kosmateus.shinden.utils.jsoup.BaseDocumentMapper;
import com.google.common.collect.ImmutableMap;
import org.jsoup.nodes.Document;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.github.kosmateus.shinden.utils.FormParamUtils.createTypeMapping;
import static com.github.kosmateus.shinden.utils.FormParamUtils.merge;

public class UserInformationMapper extends BaseDocumentMapper {

    public Map<String, String> map(Document document, UserInformationRequest request) {
        UserInformation currentUserInformation = map(document);
        boolean acceptNullFields = request.isAcceptNullFields();
        LocalDate birthDate = request.getBirthDate();
        return ImmutableMap.of(
                "csrf", mapper.with(document)
                        .selectFirst("form.creator-form > input")
                        .attr("value")
                        .orThrowWithCode("csrf"),
                "portal_lang", mapper.with(document)
                        .selectFirst("html")
                        .attr("lang")
                        .orThrowWithCode("lang"),
                "signature", merge(currentUserInformation.getSignature(), request.getSignature(), acceptNullFields),
                "about_me", merge(currentUserInformation.getAboutMe(), request.getAboutMe(), acceptNullFields),
                "gender", merge(currentUserInformation.getGender(), request.getGender(), acceptNullFields).getFormValue(),
                "birthdate_day", merge(currentUserInformation.getBirthDay(), day(birthDate), acceptNullFields).toString(),
                "birthdate_month", merge(currentUserInformation.getBirthMonth(), month(birthDate), acceptNullFields).toString(),
                "birthdate_year", merge(currentUserInformation.getBirthYear(), year(birthDate), acceptNullFields).toString(),
                "email", merge(currentUserInformation.getEmail(), request.getEmail(), acceptNullFields)
        );
    }

    public UserInformation map(Document document) {
        return UserInformation.builder()
                .signature(mapper.with(document)
                        .selectFirst("textarea#signature")
                        .text()
                        .orThrowWithCode("signature")
                )
                .aboutMe(mapper.with(document)
                        .selectFirst("textarea[name=about_me]")
                        .text()
                        .orThrowWithCode("about-me")
                )
                .gender(mapper.with(document)
                        .selectFirst("select#gender > option[selected=selected]")
                        .attr("value")
                        .mapTo(UserGender.class)
                        .orThrowWithCode("gender")
                )
                .birthDay(mapper.with(document)
                        .selectFirst("select#birthdate[name=birthdate_day] > option[selected=selected]")
                        .attr("value")
                        .toInteger()
                        .orThrowWithCode("birth-day")
                )
                .birthMonth(mapper.with(document)
                        .selectFirst("select#birthdate[name=birthdate_month] > option[selected=selected]")
                        .attr("value")
                        .toInteger()
                        .orThrowWithCode("birth-month")
                )
                .birthYear(mapper.with(document)
                        .selectFirst("select#birthdate[name=birthdate_year] > option[selected=selected]")
                        .attr("value")
                        .toInteger()
                        .orThrowWithCode("birth-year")
                )
                .email(mapper.with(document)
                        .selectFirst("input[name=email]")
                        .attr("value")
                        .orThrowWithCode("email")
                )
                .build();
    }

    @Override
    protected String getMapperCode() {
        return "user.information.edit";
    }

    @Override
    protected Map<Class<?>, Function<String, ?>> typeMappers() {
        return ImmutableMap.ofEntries(createTypeMapping(UserGender.class, UserGender.values()));
    }

    private Integer day(LocalDate localDate) {
        return Optional.ofNullable(localDate).map(LocalDate::getDayOfMonth).orElse(null);
    }

    private Integer month(LocalDate localDate) {
        return Optional.ofNullable(localDate).map(LocalDate::getMonthValue).orElse(null);
    }

    private Integer year(LocalDate localDate) {
        return Optional.ofNullable(localDate).map(LocalDate::getYear).orElse(null);
    }
}
