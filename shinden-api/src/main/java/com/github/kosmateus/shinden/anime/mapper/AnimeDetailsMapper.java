package com.github.kosmateus.shinden.anime.mapper;

import com.github.kosmateus.shinden.anime.response.AnimeDetails;
import com.github.kosmateus.shinden.anime.response.AnimeDetails.AgeType;
import com.github.kosmateus.shinden.anime.response.AnimeDetails.ConnectedTitle;
import com.github.kosmateus.shinden.anime.response.AnimeDetails.CreatedBy;
import com.github.kosmateus.shinden.anime.response.AnimeDetails.DemographicVotes;
import com.github.kosmateus.shinden.anime.response.AnimeDetails.Episode;
import com.github.kosmateus.shinden.anime.response.AnimeDetails.GeneralStatistics;
import com.github.kosmateus.shinden.anime.response.AnimeDetails.Rate;
import com.github.kosmateus.shinden.anime.response.AnimeDetails.RateType;
import com.github.kosmateus.shinden.anime.response.AnimeDetails.Review;
import com.github.kosmateus.shinden.anime.response.AnimeDetails.RoleType;
import com.github.kosmateus.shinden.anime.response.AnimeDetails.Staff;
import com.github.kosmateus.shinden.anime.response.AnimeDetails.Statistics;
import com.github.kosmateus.shinden.anime.response.AnimeDetails.TitleRecommendation;
import com.github.kosmateus.shinden.anime.response.AnimeDetails.VoiceActor;
import com.github.kosmateus.shinden.common.enums.MPAA;
import com.github.kosmateus.shinden.common.enums.TitleConnectionType;
import com.github.kosmateus.shinden.common.enums.TitleStatus;
import com.github.kosmateus.shinden.common.enums.TitleType;
import com.github.kosmateus.shinden.common.enums.UrlType;
import com.github.kosmateus.shinden.constants.ShindenConstants;
import com.github.kosmateus.shinden.enums.tag.CharacterType;
import com.github.kosmateus.shinden.enums.tag.Genre;
import com.github.kosmateus.shinden.enums.tag.Other;
import com.github.kosmateus.shinden.enums.tag.PlaceAndTime;
import com.github.kosmateus.shinden.enums.tag.SourceMaterial;
import com.github.kosmateus.shinden.enums.tag.Studio;
import com.github.kosmateus.shinden.enums.tag.TargetGroup;
import com.github.kosmateus.shinden.utils.PatternMatcher;
import com.github.kosmateus.shinden.utils.jsoup.BaseDocumentMapper;
import com.google.common.collect.ImmutableMap;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnimeDetailsMapper extends BaseDocumentMapper {
    public static final String SUMMARY = "SUMMARY";
    public static final String EPISODES = "EPISODES";
    public static final String CHARACTERS = "CHARACTERS";
    public static final String RECOMMENDATIONS = "RECOMMENDATIONS";
    public static final String REVIEWS = "REVIEWS";
    public static final String STATS = "STATS";
    public static final String FEMALE_VOTES_ROW = "div.col3.push5.progressbar-demographic-w";
    public static final String UNKNOWN_VOTES_ROW = "div.col3.push9.progressbar-from-right.progressbar-demographic-unknown";
    public static final String DEMOGRAPHIC_VOTES = "article.page-content.page-anime-stats > section.box";
    private static final String MALE_VOTES_ROW = "div.col3.push0.progressbar-from-right";
    private static final String AGE_ROW = "div.col2.push3:nth-child(n+2)";
    private static final String REVIEW_ITEM_SELECTOR = "ul.media-list > li.media-item";
    private static final String REVIEW_ID_ATTR = "id";
    private static final String REVIEW_ID_REGEX = "review_(\\d+)";
    private static final String RECOMMENDATION_ITEM_SELECTOR = "ul.media-list > li.media-item";
    private static final String RECOMMENDATION_ID_REGEX = "recommendation_(\\d+)";
    private static final String USER_ID_REGEX = "/user/(\\d+)-";
    private static final String DATE_TIME_REGEX = "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})";
    private static final DateTimeFormatter DATE_FORMATER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter DATE_FORMATTER_DEFAULT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final Function<String, LocalDate> LOCAL_DATE_MAPPER = date -> {
        if (date == null || date.isEmpty()) {
            return null;
        }
        if (date.contains(".")) {
            return LocalDate.parse(date, DATE_FORMATER);
        } else {
            return LocalDate.parse(date, DATE_FORMATTER_DEFAULT);
        }
    };

    public AnimeDetails map(Long animeId, Map<String, Document> results) {
        return AnimeDetails.builder()
                .id(animeId)
                .tags(parseTags(results.get(SUMMARY)))
                .title(parseTitle(results.get(SUMMARY)))
                .alternativeTitles(parseAlternativeTitles(results.get(SUMMARY)))
                .description(parseDescription(results.get(SUMMARY)))
                .image(parseImage(results.get(SUMMARY)))
                .rating(parseRating(results.get(SUMMARY)))
                .userRating(parseUserRating(results.get(SUMMARY)))
                .information(parseInformation(results.get(SUMMARY)))
                .generalStatistics(parseGeneralStatistics(results.get(SUMMARY)))
                .pageCreators(parsePageCreators(results.get(SUMMARY)))
                .connectedTitles(parseConnectedTitles(results.get(SUMMARY)))
                .forumTopics(parseForumTopics(results.get(SUMMARY)))
                .episodes(parseEpisodes(results.get(EPISODES)))
                .characters(mapToCharacters(results.get(CHARACTERS)))
                .staff(mapToStaff(results.get(CHARACTERS)))
                .recommendations(mapToRecommendations(results.get(RECOMMENDATIONS)))
                .reviews(mapToReviews(results.get(REVIEWS)))
                .statistics(mapToStatistics(results.get(STATS)))
                .build();
    }

    @Override
    protected String getMapperCode() {
        return "anime.details";
    }

    @Override
    protected Map<Class<?>, Function<String, ?>> typeMappers() {
        return ImmutableMap.of(
                AgeType.class, AgeType::fromValue,
                RateType.class, RateType::fromValue,
                LocalDate.class, LOCAL_DATE_MAPPER,
                TitleConnectionType.class, type -> {
                    for (TitleConnectionType t : TitleConnectionType.values()) {
                        if (t.getTranslation().equalsIgnoreCase(type)) {
                            return t;
                        }
                    }
                    throw new IllegalArgumentException("Unknown title connection type: " + type);
                },
                UrlType.class, UrlType::fromValue
        );
    }

    private String parseTitle(Document doc) {
        return mapper.with(doc)
                .selectFirst("h1.page-title span.title")
                .text()
                .orElse(null);
    }

    private List<String> parseAlternativeTitles(Document doc) {
        String alt = mapper.with(doc)
                .selectFirst("div.title-other")
                .ownText()
                .orElse("")
                .trim();
        if (alt.isEmpty()) return Collections.emptyList();
        return Stream.of(alt.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    private String parseDescription(Document doc) {
        return mapper.with(doc)
                .selectFirst("#description p")
                .text()
                .orElse(null);
    }

    private String parseImage(Document doc) {
        return mapper.with(doc)
                .selectFirst("section.title-cover img.info-aside-img")
                .attr("src")
                .orElse(null);
    }

    private AnimeDetails.Rating parseRating(Document doc) {
        Float overall = mapper.with(doc)
                .selectFirst("h3.info-aside-rating-data span.info-aside-rating-user")
                .ownText()
                .replace(",", ".")
                .toFloat()
                .orElse(null);
        Integer votes = mapper.with(doc)
                .selectFirst("h3.info-aside-rating-data + span.h6")
                .text()
                .replace("\\D+", "")
                .toInteger()
                .orElse(null);
        List<Element> ratingRows = mapper.with(doc)
                .select("ul.info-aside-overall-rating li")
                .collect()
                .mapTo(e -> e)
                .orElse(Collections.emptyList());
        Float story = null, graphics = null, music = null, characters = null;
        for (Element e : ratingRows) {
            String text = e.ownText().replace(",", ".").trim();
            if (text.isEmpty()) continue;
            float val = Float.parseFloat(text);
            String label = mapper.with(e.selectFirst("span")).text().orElse("").toLowerCase();
            if (label.contains("fabuła") || label.contains("story")) story = val;
            if (label.contains("grafika") || label.contains("graphics")) graphics = val;
            if (label.contains("muzyka") || label.contains("music")) music = val;
            if (label.contains("postacie") || label.contains("characters")) characters = val;
        }
        return AnimeDetails.Rating.builder()
                .overall(overall)
                .votes(votes)
                .story(story)
                .graphics(graphics)
                .music(music)
                .characters(characters)
                .build();
    }

    private AnimeDetails.UserRating parseUserRating(Document doc) {
        List<Integer> ratings = mapper.with(doc)
                .select("section.title-rates table x-star-rating")
                .mapTo(e -> Integer.parseInt(e.attr("value")))
                .orElse(Collections.emptyList());

        if (ratings.isEmpty()) return null;

        return AnimeDetails.UserRating.builder()
                .story(ratings.get(0))
                .graphics(ratings.get(1))
                .music(ratings.get(2))
                .characters(ratings.get(3))
                .overall(ratings.get(4))
                .build();

    }

    private AnimeDetails.Tags parseTags(Document doc) {
        List<Element> tagRows = mapper.with(doc)
                .select("table.data-view-table-full tbody.info-top-table-highlight tr")
                .collect()
                .mapTo(e -> e)
                .orElse(Collections.emptyList());
        return AnimeDetails.Tags.builder()
                .genres(tagRows.stream()
                        .filter(e -> mapper.with(e).selectFirst("td").text().orElse("").equals("Gatunki:"))
                        .map(e -> mapper.with(e).select("td ul.tags li a").mapTo(el -> el.attr("data-id")).orElse(Collections.emptyList()))
                        .flatMap(List::stream)
                        .map(Genre::fromValueOrNull)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .targetGroups(tagRows.stream()
                        .filter(e1 -> mapper.with(e1).selectFirst("td").text().orElse("").equals("Grupy docelowe:"))
                        .map(e1 -> mapper.with(e1).select("td ul.tags li a").mapTo(el1 -> el1.attr("data-id")).orElse(Collections.emptyList()))
                        .flatMap(List::stream)
                        .map(TargetGroup::fromValueOrNull)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .otherTags(tagRows.stream()
                        .filter(e2 -> mapper.with(e2).selectFirst("td").text().orElse("").equals("Pozostałe tagi:"))
                        .map(e2 -> mapper.with(e2).select("td ul.tags li a").mapTo(el2 -> el2.attr("data-id")).orElse(Collections.emptyList()))
                        .flatMap(List::stream)
                        .map(Other::fromValueOrNull)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .placeAndTimeTags(tagRows.stream()
                        .filter(e3 -> mapper.with(e3).selectFirst("td").text().orElse("").equals("Miejsce i czas:"))
                        .map(e3 -> mapper.with(e3).select("td ul.tags li a").mapTo(el3 -> el3.attr("data-id")).orElse(Collections.emptyList()))
                        .flatMap(List::stream)
                        .map(PlaceAndTime::fromValueOrNull)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .characterTypes(tagRows.stream()
                        .filter(e4 -> mapper.with(e4).selectFirst("td").text().orElse("").equals("Rodzaje postaci:"))
                        .map(e4 -> mapper.with(e4).select("td ul.tags li a").mapTo(el4 -> el4.attr("data-id")).orElse(Collections.emptyList()))
                        .flatMap(List::stream)
                        .map(CharacterType::fromValueOrNull)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .sourceMaterials(tagRows.stream()
                        .filter(e5 -> mapper.with(e5).selectFirst("td").text().orElse("").equals("Pierwowzór:"))
                        .map(e5 -> mapper.with(e5).select("td ul.tags li a").mapTo(el5 -> el5.attr("data-id")).orElse(Collections.emptyList()))
                        .flatMap(List::stream)
                        .map(SourceMaterial::fromValueOrNull)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .build();
    }

    private String normalizeEnum(String text) {
        return text.replaceAll("[^a-zA-Z0-9]+", "_").toUpperCase();
    }

    private AnimeDetails.Information parseInformation(Document doc) {
        String typeText = mapper.with(doc)
                .selectFirst("section.title-small-info dl.info-aside-list dt:matchesOwn(^Typ:$) + dd")
                .ownText()
                .orElse("");
        TitleType titleType = null;
        for (TitleType t : TitleType.values()) {
            if (typeText.toLowerCase().contains(t.name().toLowerCase())) {
                titleType = t;
                break;
            }
        }
        String statusText = mapper.with(doc)
                .selectFirst("section.title-small-info dl.info-aside-list dt:matchesOwn(^Status:$) + dd")
                .ownText()
                .orElse("");
        TitleStatus titleStatus = null;
        if (statusText.toLowerCase().contains("zakończone") || statusText.toLowerCase().contains("finished")) {
            titleStatus = TitleStatus.FINISHED_AIRING;
        }
        LocalDate startDate = mapper.with(doc)
                .selectFirst("section.title-small-info dl.info-aside-list dt:matchesOwn(^Data emisji:$) + dd")
                .ownText()
                .mapTo(LocalDate.class)
                .orElse(null);
        LocalDate endDate = mapper.with(doc)
                .selectFirst("section.title-small-info dl.info-aside-list dt:matchesOwn(^Koniec emisji:$) + dd")
                .ownText()
                .mapTo(LocalDate.class)
                .orElse(null);
        Integer episodes = mapper.with(doc)
                .selectFirst("section.title-small-info dl.info-aside-list dt:matchesOwn(^Liczba odcinków:$) + dd")
                .ownText()
                .toInteger()
                .orElse(null);
        List<Studio> studios = mapper.with(doc)
                .select("section.title-small-info dl.info-aside-list dt:matchesOwn(^Studio:$) + dd a[href^='/studio/']")
                .mapTo(e -> {
                    try {
                        return Studio.valueOf(normalizeEnum(e.ownText()));
                    } catch (Exception ex) {
                        return null;
                    }
                })
                .orElse(Collections.emptyList())
                .stream()
                .filter(s -> s != null)
                .collect(Collectors.toList());
        Integer duration = mapper.with(doc)
                .selectFirst("section.title-small-info dl.info-aside-list dt:matchesOwn(^Długość odcinka:$) + dd")
                .ownText()
                .replace("[^\\d]+", "")
                .toInteger()
                .orElse(null);
        String mpaaText = mapper.with(doc)
                .selectFirst("section.title-small-info dl.info-aside-list dt:matchesOwn(^MPAA:$) + dd")
                .ownText()
                .orElse("");
        MPAA rating = null;
        for (MPAA m : MPAA.values()) {
            if (mpaaText.equalsIgnoreCase(m.name()) || mpaaText.equalsIgnoreCase(m.getValue())) {
                rating = m;
                break;
            }
        }
        return AnimeDetails.Information.builder()
                .type(titleType)
                .status(titleStatus)
                .startDate(startDate)
                .endDate(endDate)
                .episodes(episodes)
                .studios(studios)
                .episodeDuration(duration)
                .mpaa(rating)
                .build();
    }

    private GeneralStatistics parseGeneralStatistics(Document doc) {
        Integer watching = mapper.with(doc)
                .selectFirst("section.title-stats dl.info-aside-list dt:matchesOwn(^Ogląda$) + dd")
                .ownText()
                .replace("\\D+", "")
                .toInteger()
                .orElse(0);
        Integer completed = mapper.with(doc)
                .selectFirst("section.title-stats dl.info-aside-list dt:matchesOwn(^Obejrzało$) + dd")
                .ownText()
                .replace("\\D+", "")
                .toInteger()
                .orElse(0);
        Integer skipped = mapper.with(doc)
                .selectFirst("section.title-stats dl.info-aside-list dt:matchesOwn(^Pominęło$) + dd")
                .ownText()
                .replace("\\D+", "")
                .toInteger()
                .orElse(0);
        Integer onHold = mapper.with(doc)
                .selectFirst("section.title-stats dl.info-aside-list dt:matchesOwn(^Wstrzymało$) + dd")
                .ownText()
                .replace("\\D+", "")
                .toInteger()
                .orElse(0);
        Integer dropped = mapper.with(doc)
                .selectFirst("section.title-stats dl.info-aside-list dt:matchesOwn(^Porzuciło$) + dd")
                .ownText()
                .replace("\\D+", "")
                .toInteger()
                .orElse(0);
        Integer plan = mapper.with(doc)
                .selectFirst("section.title-stats dl.info-aside-list dt:matchesOwn(^Planuje$) + dd")
                .ownText()
                .replace("\\D+", "")
                .toInteger()
                .orElse(0);
        Integer likes = mapper.with(doc)
                .selectFirst("section.title-stats dl.info-aside-list dt:matchesOwn(^Lubi$) + dd")
                .ownText()
                .replace("\\D+", "")
                .toInteger()
                .orElse(0);
        return GeneralStatistics.builder()
                .currentlyWatching(watching)
                .completed(completed)
                .skipped(skipped)
                .onHold(onHold)
                .dropped(dropped)
                .planToWatch(plan)
                .likes(likes)
                .build();
    }

    private List<CreatedBy> parsePageCreators(Document doc) {
        return mapper.with(doc)
                .select("section.title-coocreate ul.media-list li.media-item")
                .mapTo(e -> {
                    Integer id = mapper.with(e)
                            .selectFirst("a.media-user-cover")
                            .attr("href")
                            .pattern(PatternMatcher.match("/user/(\\d+)", 1))
                            .toInteger()
                            .orElse(null);
                    String name = mapper.with(e)
                            .selectFirst("h3.media-header a")
                            .text()
                            .orElse("");
                    String image = mapper.with(e)
                            .selectFirst("a.media-user-cover img")
                            .attr("src")
                            .orElse("");
                    Float score = mapper.with(e)
                            .selectFirst("div.progressbar-value")
                            .attr("style")
                            .replace("[^0-9\\.]+", "")
                            .toFloat()
                            .orElse(0f);
                    return CreatedBy.builder()
                            .id(id)
                            .name(name)
                            .image(image)
                            .score(score)
                            .build();
                })
                .orElse(Collections.emptyList());
    }

    private List<ConnectedTitle> parseConnectedTitles(Document doc) {
        return mapper.with(doc)
                .select("section.box h2:matchesOwn(^Powiązane Serie$) ~ ul.figure-list li.relation_t2t")
                .mapTo(e -> {
                    Integer id = mapper.with(e)
                            .selectFirst("figcaption > a[href^='/titles/'], figcaption > a[href^='/manga/'], figcaption > a[href^='/series/'], , figcaption > a[href^='/books/']")
                            .attr("href")
                            .pattern(PatternMatcher.match("/(?:titles|manga|series|books)/(\\d+)", 1))
                            .toInteger()
                            .orThrowWithCode("connected-title.id");
                    TitleConnectionType connectionType = mapper.with(e)
                            .select("figcaption.figure-type", 1)
                            .get(0)
                            .ownText()
                            .mapTo(TitleConnectionType.class)
                            .orThrowWithCode("connected-title.connection-type");
                    String title = mapper.with(e)
                            .selectFirst("figcaption > a")
                            .ownText()
                            .orThrowWithCode("connected-title.title");
                    UrlType urlType = mapper.with(e)
                            .selectFirst("figcaption > a")
                            .attr("href")
                            .pattern(ShindenConstants.MEDIA_URL_TYPE_MATCHER)
                            .mapTo(UrlType.class)
                            .orThrowWithCode("connected-title.url-type");
                    return ConnectedTitle.builder()
                            .id(id)
                            .title(title)
                            .urlType(urlType)
                            .connectionType(connectionType)
                            .build();
                })
                .orElse(Collections.emptyList());
    }

    private List<AnimeDetails.ForumTopic> parseForumTopics(Document doc) {
        return mapper.with(doc)
                .select("section.title-threads ul.info-aside-list li")
                .mapTo(e -> {

                    Integer id = mapper.with(e)
                            .selectFirst("a[href^='https://forum." + ShindenConstants.SHINDEN_HOST + "/posts/']")
                            .attr("href")
                            .pattern(PatternMatcher.match("posts/(\\d+)", 1))
                            .toInteger()
                            .orThrowWithCode("forum-topic.id");
                    String title = mapper.with(e)
                            .selectFirst("a[href^='https://forum." + ShindenConstants.SHINDEN_HOST + "/posts/']")
                            .ownText()
                            .orElse("");
                    String sub = mapper.with(e)
                            .attr("title")
                            .orElse("")
                            .replaceAll("(^W: |</?\\w+.*?>)", "")
                            .trim();
                    return AnimeDetails.ForumTopic.builder()
                            .id(id)
                            .title(title)
                            .subForum(sub)
                            .build();
                })
                .orElse(Collections.emptyList());
    }

    private List<Episode> parseEpisodes(Document document) {
        if (document == null) {
            return Collections.emptyList();
        }
        List<Episode> episodes = mapper.with(document)
                .select("table.data-view-table-episodes > tbody > tr")
                .mapTo(this::parseSingleEpisode)
                .orElse(Collections.emptyList());
        Collections.reverse(episodes);
        return episodes;
    }

    private Episode parseSingleEpisode(Element row) {

        return Episode.builder()
                .id(mapper.with(row)
                        .selectFirst("td.button-group a.detail[href*='/view/']")
                        .attr("href")
                        .pattern(PatternMatcher.match("/view/(\\d+)", 1))
                        .toInteger()
                        .orElse(null))
                .title(mapper.with(row.selectFirst("td.ep-title"))
                        .text()
                        .orElse(""))
                .number(mapper.with(row)
                        .attr("data-episode-no")
                        .toFloat()
                        .orElse(null))
                .filler(mapper.with(row)
                        .selectFirst("i.fa-facebook.button-with-tip")
                        .exists())
                .available(mapper.with(row)
                        .selectFirst("i.fa.fa-fw.fa-check")
                        .exists())
                .languages(mapper.with(row)
                        .select("td > span.flag-icon")
                        .mapTo(
                                element -> element.classNames().stream()
                                        .filter(name -> name.startsWith("flag-icon-"))
                                        .map(name -> name.substring("flag-icon-".length()))
                                        .findFirst()
                                        .orElse(null)
                        ).orElse(null).stream().filter(Objects::nonNull).collect(Collectors.toList()))
                .releaseDate(mapper.with(row.selectFirst("td.ep-date"))
                        .text()
                        .mapTo(LocalDate.class)
                        .orElse(null))
                .build();
    }

    private List<AnimeDetails.Character> mapToCharacters(Document document) {
        if (document == null) {
            return Collections.emptyList();
        }

        return mapper.with(document)
                .select("section.ch-st-list > div.ch-st-item")
                .mapTo(this::parseSingleCharacter)
                .orElse(Collections.emptyList());
    }

    private AnimeDetails.Character parseSingleCharacter(Element characterBlock) {

        String[] names = Stream.of(mapper.with(characterBlock.selectFirst("span.item-l"))
                        .selectFirst("div.p-txt > h3 > a")
                        .text()
                        .orElse("").split(","))
                .map(String::trim)
                .toArray(String[]::new);

        return AnimeDetails.Character.builder()
                .id(mapper.with(characterBlock.selectFirst("span.item-l"))
                        .selectFirst("a[href^='/character/']")
                        .attr("href")
                        .pattern(PatternMatcher.match("/character/(\\d+)", 1))
                        .toInteger()
                        .orThrowWithCode("character.id"))
                .firstName(names.length == 3 ? names[2] : names.length == 2 ? names[1] : names[0])
                .middleName(names.length == 3 ? names[1] : null)
                .lastName(names.length == 3 ? names[0] : names.length == 2 ? names[0] : null)
                .image(mapper.with(characterBlock.selectFirst("span.item-l"))
                        .selectFirst("a.img > img.character")
                        .attr("src")
                        .orElse(""))
                .role(parseCharacterRole(mapper.with(characterBlock.selectFirst("span.item-l"))
                        .selectFirst("div.p-txt > p")
                        .text()
                        .orElse("")))
                .voiceActors(mapper.with(characterBlock)
                        .select("span.item-r")
                        .mapTo(this::parseVoiceActor)
                        .orElse(Collections.emptyList()))
                .build();
    }


    private RoleType parseCharacterRole(String text) {
        String roleLower = text.toLowerCase();
        if (roleLower.contains("główna") || roleLower.contains("main")) {
            return RoleType.MAIN;
        } else if (roleLower.contains("wspierająca") || roleLower.contains("supporting")) {
            return RoleType.SUPPORTING;
        }
        return RoleType.SUPPORTING;
    }

    private VoiceActor parseVoiceActor(Element voiceActorSpan) {
        String[] names = Stream.of(mapper.with(voiceActorSpan)
                        .selectFirst("div.p-txt > h3 > a")
                        .text()
                        .orElse("").split(","))
                .map(String::trim)
                .toArray(String[]::new);

        return VoiceActor.builder()
                .id(mapper.with(voiceActorSpan)
                        .selectFirst("div.p-txt > h3 > a[href^='/staff/']")
                        .attr("href")
                        .pattern(PatternMatcher.match("/staff/(\\d+)", 1))
                        .toInteger()
                        .orThrowWithCode("voice-actor.id"))
                .firstName(names.length == 3 ? names[2] : names.length == 2 ? names[1] : names[0])
                .middleName(names.length == 3 ? names[1] : null)
                .lastName(names.length == 3 ? names[0] : names.length == 2 ? names[0] : null)
                .image(mapper.with(voiceActorSpan)
                        .selectFirst("a.img > img.staff")
                        .attr("src")
                        .orElse(""))
                .role(mapper.with(voiceActorSpan)
                        .selectFirst("div.p-txt > p")
                        .text()
                        .orElse(null))
                .build();
    }

    private List<Staff> mapToStaff(Document document) {
        if (document == null) {
            return Collections.emptyList();
        }

        return mapper.with(document)
                .select("section.person-list > div.person-character-item")
                .mapTo(this::parseStaffEntry)
                .orElse(Collections.emptyList());
    }


    private Staff parseStaffEntry(Element element) {

        String[] names = Stream.of(mapper.with(element)
                        .selectFirst("h3 > a")
                        .text()
                        .orElse("").split(","))
                .map(String::trim)
                .toArray(String[]::new);
        return Staff.builder()
                .id(mapper.with(element)
                        .selectFirst("h3 > a[href^='/staff/']")
                        .attr("href")
                        .pattern(PatternMatcher.match("/staff/(\\d+)", 1))
                        .toInteger()
                        .orElse(null))
                .firstName(names.length == 3 ? names[2] : names.length == 2 ? names[1] : names[0])
                .middleName(names.length == 3 ? names[1] : null)
                .lastName(names.length == 3 ? names[0] : names.length == 2 ? names[0] : null)
                .image(mapper.with(element)
                        .selectFirst("img.character")
                        .attr("src")
                        .orElse(""))
                .role(mapper.with(element)
                        .selectFirst("div.person-character-text > p")
                        .text()
                        .orElse(""))
                .build();
    }

    private Statistics mapToStatistics(Document document) {
        if (document == null) {
            return null;
        }

        return Statistics.builder()
                .maleVotes(mapToMaleVotes(document))
                .femaleVotes(mapToFemaleVotes(document))
                .unknownVotes(mapToUnknownVotes(document))
                .overallRates(mapToRates(document, 0))
                .storyRates(mapToRates(document, 1))
                .musicRates(mapToRates(document, 2))
                .graphicsRates(mapToRates(document, 3))
                .characterRates(mapToRates(document, 4))
                .build();
    }

    private List<Review> mapToReviews(Document document) {
        if (document == null) return Collections.emptyList();

        return mapper.with(document)
                .select(REVIEW_ITEM_SELECTOR)
                .mapTo(this::mapToReview)
                .orElse(Collections.emptyList());
    }

    private Review mapToReview(Element element) {
        return Review.builder()
                .id(mapper.with(element)
                        .selectFirst("div.bd > div.media")
                        .attr(REVIEW_ID_ATTR)
                        .pattern(PatternMatcher.match(REVIEW_ID_REGEX, 1))
                        .toInteger()
                        .orThrowWithCode("review.id"))
                .content(mapper.with(element)
                        .selectFirst("div.bd > p")
                        .text()
                        .orNull())
                .likes(mapper.with(element)
                        .selectFirst("a.binary-rate[data-value='1'] span")
                        .text()
                        .toInteger()
                        .orElse(0))
                .dislikes(mapper.with(element)
                        .selectFirst("a.binary-rate[data-value='0'] span")
                        .text()
                        .toInteger()
                        .orElse(0))
                .userId(mapper.with(element)
                        .selectFirst("a[href^='/user/']")
                        .attr("href")
                        .pattern(PatternMatcher.match("/user/(\\d+)-", 1))
                        .toInteger()
                        .orElse(null))
                .userName(mapper.with(element)
                        .selectFirst("h3.media-header a")
                        .text()
                        .orElse(""))
                .userImage(mapper.with(element)
                        .selectFirst("a.media-title-cover img")
                        .attr("src")
                        .orElse(""))
                .displayCount(mapper.with(element)
                        .selectFirst("figure:nth-child(1) figcaption")
                        .text()
                        .toInteger()
                        .orElse(0))
                .readCount(mapper.with(element)
                        .selectFirst("figure:nth-child(2) figcaption")
                        .text()
                        .toInteger()
                        .orElse(0))
                .build();
    }

    private List<TitleRecommendation> mapToRecommendations(Document document) {
        if (document == null) return Collections.emptyList();

        return mapper.with(document)
                .select(RECOMMENDATION_ITEM_SELECTOR)
                .mapTo(this::mapToRecommendation)
                .orElse(Collections.emptyList());
    }

    private TitleRecommendation mapToRecommendation(Element element) {
        return TitleRecommendation.builder()
                .id(mapper.with(element)
                        .selectFirst("div.bd > div.media.clearfix")
                        .attr("id")
                        .pattern(PatternMatcher.match(RECOMMENDATION_ID_REGEX, 1))
                        .toInteger()
                        .orThrowWithCode("recommendation.id"))
                .title(mapper.with(element)
                        .selectFirst("h3.box-art-title a")
                        .text()
                        .orElse(""))
                .image(mapper.with(element)
                        .selectFirst("img.media-title-cover")
                        .attr("src")
                        .orElse(""))
                .likes(mapper.with(element)
                        .selectFirst("a[data-value='1'] span")
                        .text()
                        .toInteger()
                        .orElse(0))
                .dislikes(mapper.with(element)
                        .selectFirst("a[data-value='0'] span")
                        .text()
                        .toInteger()
                        .orElse(0))
                .userId(mapper.with(element)
                        .selectFirst("h3.box-art-sub-title a")
                        .attr("href")
                        .pattern(PatternMatcher.match(USER_ID_REGEX, 1))
                        .toInteger()
                        .orElse(null))
                .userName(mapper.with(element)
                        .selectFirst("h3.box-art-sub-title a")
                        .text()
                        .orElse(""))
                .userImage(mapper.with(element)
                        .selectFirst(".text-wrap-left.avatar-image")
                        .attr("src")
                        .orElse(""))
                .content(mapper.with(element)
                        .selectFirst("div.bd > p:not(.box-art-text)")
                        .text()
                        .orElse(""))
                .date(mapper.with(element)
                        .selectFirst("span.add-date")
                        .attr("title")
                        .pattern(PatternMatcher.match(DATE_TIME_REGEX, 1))
                        .mapTo(LocalDateTime.class)
                        .orElse(null))
                .build();
    }

    private List<Rate> mapToRates(Document document, int index) {
        return mapper.with(document)
                .selectFirst("div.percentage-rates:nth-of-type(" + (index + 1) + ")")
                .select("li")
                .mapTo(this::mapToRate)
                .orElse(null);
    }

    private Rate mapToRate(Element element) {
        return Rate.builder()
                .type(mapper.with(element)
                        .selectFirst("div.img.media-stats-img-number")
                        .text()
                        .mapTo(RateType.class)
                        .orThrowWithCode("rate.type")
                )
                .votes(mapper.with(element)
                        .selectFirst("span.media-stats-small-number")
                        .text()
                        .replace("głosów", "")
                        .replace("głos", "")
                        .replace("głosy", "")
                        .toInteger()
                        .orElse(0)
                )
                .build();
    }

    private List<DemographicVotes> mapToUnknownVotes(Document document) {
        return mapper.with(document)
                .selectFirst(DEMOGRAPHIC_VOTES)
                .select(UNKNOWN_VOTES_ROW)
                .and()
                .select(AGE_ROW, 1)
                .mapTo(items -> mapToVotes(items, AGE_ROW, UNKNOWN_VOTES_ROW))
                .orThrowWithCode("stats.unknown.votes");
    }

    private List<DemographicVotes> mapToFemaleVotes(Document document) {
        return mapper.with(document)
                .selectFirst(DEMOGRAPHIC_VOTES)
                .select(FEMALE_VOTES_ROW)
                .and()
                .select(AGE_ROW, 1)
                .mapTo(items -> mapToVotes(items, AGE_ROW, FEMALE_VOTES_ROW))
                .orThrowWithCode("stats.female.votes");
    }

    private List<DemographicVotes> mapToMaleVotes(Document document) {
        return mapper.with(document)
                .selectFirst(DEMOGRAPHIC_VOTES)
                .select(MALE_VOTES_ROW)
                .and()
                .select(AGE_ROW, 1)
                .mapTo(items -> mapToVotes(items, AGE_ROW, MALE_VOTES_ROW))
                .orThrowWithCode("stats.male.votes");
    }

    private DemographicVotes mapToVotes(Map<String, Element> items, String ageRow, String votesRow) {
        return DemographicVotes.builder()
                .age(mapToAge(items.get(ageRow)))
                .votes(mapToVotes(items.get(votesRow)))
                .score(mapToScore(items.get(votesRow)))
                .build();
    }

    private Float mapToScore(Element element) {
        return mapper.with(element)
                .selectFirst("span.progressbar-text")
                .ownText()
                .replace(",", ".")
                .toFloat()
                .orElse(0.0f);
    }

    private Integer mapToVotes(Element element) {
        return mapper.with(element)
                .selectFirst("small")
                .text()
                .replace("/", "")
                .toInteger()
                .orElse(0);
    }

    private AgeType mapToAge(Element element) {
        return mapper.with(element)
                .text()
                .mapTo(AgeType.class)
                .orThrowWithCode("demographic-votes.age");
    }
}
