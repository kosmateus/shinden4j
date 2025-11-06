package com.github.kosmateus.shinden.user.mapper;

import com.github.kosmateus.shinden.common.enums.TitleType;
import com.github.kosmateus.shinden.common.enums.UrlType;
import com.github.kosmateus.shinden.user.common.enums.UserTitleStatus;
import com.github.kosmateus.shinden.user.response.Comment;
import com.github.kosmateus.shinden.user.response.EntityOverview;
import com.github.kosmateus.shinden.user.response.FavouriteMediaItem;
import com.github.kosmateus.shinden.user.response.ListItemOverview;
import com.github.kosmateus.shinden.user.response.MediaStatistics;
import com.github.kosmateus.shinden.user.response.UserOverview;
import com.github.kosmateus.shinden.user.response.UserOverview.UserOverviewBuilder;
import com.github.kosmateus.shinden.utils.PatternMatcher;
import com.github.kosmateus.shinden.utils.jsoup.BaseDocumentMapper;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.github.kosmateus.shinden.constants.ShindenConstants.MEDIA_ID_MATCHER;
import static com.github.kosmateus.shinden.constants.ShindenConstants.MEDIA_URL_TYPE_MATCHER;
import static com.github.kosmateus.shinden.constants.ShindenConstants.USER_ID_MATCHER;

@Slf4j
public class UserOverviewMapper extends BaseDocumentMapper {

    public static final String ANIME_STATS = "anime-stats";
    public static final String MANGA_STATS = "manga-stats";
    private static final String EMPTY_ABOUT_ME = "Ten użytkownik jeszcze nic o sobie nie napisał.";
    private static final PatternMatcher FIRST_NAME_MATCHER = PatternMatcher.nullableMatch("^[^,]*", 0);
    private static final PatternMatcher LAST_NAME_MATCHER = PatternMatcher.nullableMatch("(?<=,).*", 0);

    public UserOverview map(Document document) {
        return userBasicInformation(document)
                .about(getAbout(document))
                .animeStatistics(getAnimeStatistics(document))
                .mangaStatistics(getMangaStatistics(document))
                .favouriteAnime(getFavouriteAnime(document))
                .favouriteManga(getFavouriteManga(document))
                .favouriteCharacters(getFavouriteCharacters(document))
                .favouritePeople(getFavouritePeople(document))
                .animeListUpdates(getAnimeListUpdates(document))
                .mangaListUpdates(getMangaListUpdates(document))
                .comments(getComments(document))
                .build();
    }

    @Override
    protected String getMapperCode() {
        return "user.overview";
    }

    @Override
    protected Map<Class<?>, Function<String, ?>> typeMappers() {
        return ImmutableMap.of(
                TitleType.class, TitleType::fromValue,
                UrlType.class, UrlType::fromValue,
                UserTitleStatus.class, UserTitleStatus::fromValue
        );
    }

    private UserOverviewBuilder userBasicInformation(Document document) {
        return UserOverview.builder()
                .id(mapper.with(document)
                        .location()
                        .pattern(USER_ID_MATCHER)
                        .toLong()
                        .orThrowWithCode("id"))
                .username(mapper.with(document)
                        .selectFirst("div.l-main-contantainer.controller-user > div > button > strong")
                        .text()
                        .orThrowWithCode("username"))
                .avatarUrl(mapper.with(document)
                        .selectFirst("aside.info-aside.aside-user > img")
                        .attr("src")
                        .orThrowWithCode("avatar"))
                .achievements(mapper.with(document)
                        .selectFirst("aside.info-aside.aside-user > div.achievements span")
                        .text()
                        .toInteger()
                        .orThrowWithCode("achievements"))
                .lastOnline(mapper.with(document)
                        .selectFirst("aside.info-aside.aside-user dl.stats dd:nth-of-type(1)")
                        .text()
                        .toLocalDateTime()
                        .orThrowWithCode("last-online"))
                .rank(mapper.with(document)
                        .selectFirst("aside.info-aside.aside-user dl.stats dd:nth-of-type(2)")
                        .text()
                        .orThrowWithCode("rank"))
                .language(mapper.with(document)
                        .selectFirst("aside.info-aside.aside-user dl.stats dd:nth-of-type(3)")
                        .text()
                        .orThrowWithCode("language"))
                .joinDate(mapper.with(document)
                        .selectFirst("aside.info-aside.aside-user dl.stats dd:nth-of-type(4)")
                        .text()
                        .toLocalDateTime()
                        .orThrowWithCode("join-date"))
                .score(mapper.with(document)
                        .selectFirst("aside.info-aside.aside-user dl.stats dd:nth-of-type(5)")
                        .text()
                        .toInteger()
                        .orThrowWithCode("score"));
    }

    private String getAbout(Document document) {
        String aboutMe = mapper.with(document)
                .selectFirst("div.box-userprofile div.row.about-me")
                .text()

                .orThrowWithCode("about");
        return EMPTY_ABOUT_ME.equalsIgnoreCase(aboutMe) ? null : aboutMe;
    }

    private MediaStatistics getAnimeStatistics(Document document) {
        return MediaStatistics.builder()
                .totalTime(mapper.with(document)
                        .selectFirst("section." + ANIME_STATS + " div.total-time > strong")
                        .attr("title")
                        .replace("min", "")
                        .toInteger()
                        .orThrowWithCode("stats.anime.total-time"))
                .meanScore(mapper.with(document)
                        .selectFirst("section." + ANIME_STATS + " div.mean-score > strong")
                        .text()
                        .replace(",", ".")
                        .toFloat()
                        .orThrowWithCode("stats.anime.mean-score"))
                .totalTitles(mapper.with(document)
                        .selectFirst("section." + ANIME_STATS + " table.data-view-table tr:eq(0) > td:eq(1)")
                        .text()
                        .toInteger()
                        .orThrowWithCode("stats.anime.total-titles"))
                .totalSegments(mapper.with(document)
                        .selectFirst("section." + ANIME_STATS + " table.data-view-table tr:eq(1) > td:eq(1)")
                        .text()
                        .toInteger()
                        .orThrowWithCode("stats.anime.total-episodes"))
                .totalRevisits(mapper.with(document)
                        .selectFirst("section." + ANIME_STATS + " table.data-view-table tr:eq(2) > td:eq(1)")
                        .text()
                        .toInteger()
                        .orThrowWithCode("stats.anime.total-re-watch"))
                .inProgress(mapper.with(document)
                        .selectFirst(
                                "section." + ANIME_STATS + " table.data-view-table:nth-of-type(2) tr:eq(0) > td:eq(1)")
                        .text()
                        .toInteger()
                        .orThrowWithCode("stats.anime.in-progress"))
                .completed(mapper.with(document)
                        .selectFirst(
                                "section." + ANIME_STATS + " table.data-view-table:nth-of-type(2) tr:eq(1) > td:eq(1)")
                        .text()
                        .toInteger()
                        .orThrowWithCode("stats.anime.completed"))
                .skip(mapper.with(document)
                        .selectFirst(
                                "section." + ANIME_STATS + " table.data-view-table:nth-of-type(2) tr:eq(2) > td:eq(1)")
                        .text()
                        .toInteger()
                        .orThrowWithCode("stats.anime.skip"))
                .hold(mapper.with(document)
                        .selectFirst(
                                "section." + ANIME_STATS + " table.data-view-table:nth-of-type(2) tr:eq(3) > td:eq(1)")
                        .text()
                        .toInteger()
                        .orThrowWithCode("stats.anime.hold"))
                .dropped(mapper.with(document)
                        .selectFirst(
                                "section." + ANIME_STATS + " table.data-view-table:nth-of-type(2) tr:eq(4) > td:eq(1)")
                        .text()
                        .toInteger()
                        .orThrowWithCode("stats.anime.dropped"))
                .planned(mapper.with(document)
                        .selectFirst(
                                "section." + ANIME_STATS + " table.data-view-table:nth-of-type(2) tr:eq(5) > td:eq(1)")
                        .text()
                        .toInteger()
                        .orThrowWithCode("stats.anime.planned"))
                .build();
    }

    private MediaStatistics getMangaStatistics(Document document) {
        return MediaStatistics.builder()
                .totalTime(mapper.with(document)
                        .selectFirst("section." + MANGA_STATS + " div.total-time > strong")
                        .attr("title")
                        .replace("min", "")
                        .toInteger()
                        .orThrowWithCode("stats.manga.total-time"))
                .meanScore(mapper.with(document)
                        .selectFirst("section." + MANGA_STATS + " div.mean-score > strong")
                        .text()
                        .replace(",", ".")
                        .toFloat()
                        .orThrowWithCode("stats.manga.mean-score"))
                .totalTitles(mapper.with(document)
                        .selectFirst("section." + MANGA_STATS + " table.data-view-table tr:eq(0) > td:eq(1)")
                        .text()
                        .toInteger()
                        .orThrowWithCode("stats.manga.total-titles"))
                .totalSegments(mapper.with(document)
                        .selectFirst("section." + MANGA_STATS + " table.data-view-table tr:eq(1) > td:eq(1)")
                        .text()
                        .toInteger()
                        .orThrowWithCode("stats.manga.total-chapters"))
                .totalRevisits(mapper.with(document)
                        .selectFirst("section." + MANGA_STATS + " table.data-view-table tr:eq(2) > td:eq(1)")
                        .text()
                        .toInteger()
                        .orThrowWithCode("stats.manga.total-re-read"))
                .inProgress(mapper.with(document)
                        .selectFirst(
                                "section." + MANGA_STATS + " table.data-view-table:nth-of-type(2) tr:eq(0) > td:eq(1)")
                        .text()
                        .toInteger()
                        .orThrowWithCode("stats.manga.in-progress"))
                .completed(mapper.with(document)
                        .selectFirst(
                                "section." + MANGA_STATS + " table.data-view-table:nth-of-type(2) tr:eq(1) > td:eq(1)")
                        .text()
                        .toInteger()
                        .orThrowWithCode("stats.manga.completed"))
                .skip(mapper.with(document)
                        .selectFirst(
                                "section." + MANGA_STATS + " table.data-view-table:nth-of-type(2) tr:eq(2) > td:eq(1)")
                        .text()
                        .toInteger()
                        .orThrowWithCode("stats.manga.skip"))
                .hold(mapper.with(document)
                        .selectFirst(
                                "section." + MANGA_STATS + " table.data-view-table:nth-of-type(2) tr:eq(3) > td:eq(1)")
                        .text()
                        .toInteger()
                        .orThrowWithCode("stats.manga.hold"))
                .dropped(mapper.with(document)
                        .selectFirst(
                                "section." + MANGA_STATS + " table.data-view-table:nth-of-type(2) tr:eq(4) > td:eq(1)")
                        .text()
                        .toInteger()
                        .orThrowWithCode("stats.manga.dropped"))
                .planned(mapper.with(document)
                        .selectFirst(
                                "section." + MANGA_STATS + " table.data-view-table:nth-of-type(2) tr:eq(5) > td:eq(1)")
                        .text()
                        .toInteger()
                        .orThrowWithCode("stats.manga.planned"))
                .build();
    }

    private List<FavouriteMediaItem> getFavouriteAnime(Document document) {
        return mapper.with(document)
                .selectFirst("section.favouritue.animes")
                .select("li")
                .mapTo(item -> this.createFavouriteItem(item, "anime"))
                .orThrowWithCode("favourite.anime");
    }

    private List<FavouriteMediaItem> getFavouriteManga(Document document) {
        return mapper.with(document)
                .selectFirst("section.favouritue.mangas")
                .select("li")
                .mapTo(item -> this.createFavouriteItem(item, "manga"))
                .orThrowWithCode("favourite.manga");
    }

    private List<EntityOverview> getFavouriteCharacters(Document document) {
        return mapper.with(document)
                .selectFirst("section.favouritue.characters")
                .select("li")
                .mapTo(item -> this.createFavouriteEntity(item, "characters"))
                .orThrowWithCode("favourite.characters");
    }

    private List<EntityOverview> getFavouritePeople(Document document) {
        return mapper.with(document)
                .selectFirst("section.favouritue.staffs")
                .select("li")
                .mapTo(item -> this.createFavouriteEntity(item, "people"))
                .orThrowWithCode("favourite.people");
    }

    private List<ListItemOverview> getAnimeListUpdates(Document document) {
        return mapper.with(document)
                .selectFirst("section.last-updates.anime-updates")
                .select("li")
                .mapTo(item -> this.createListItemOverview(item, "anime"))
                .orThrowWithCode("anime-list-updates");
    }

    private List<ListItemOverview> getMangaListUpdates(Document document) {
        return mapper.with(document)
                .selectFirst("section.push6.col6.box:nth-of-type(2)")
                .select("li")
                .mapTo(item -> this.createListItemOverview(item, "manga"))
                .orThrowWithCode("manga-list-updates");
    }

    private List<Comment> getComments(Document document) {
        return mapper.with(document)
                .selectFirst("section.box.comments")
                .select("ul li.media.media-comment")
                .mapTo(this::createComment)
                .orThrowWithCode("comments");
    }

    private FavouriteMediaItem createFavouriteItem(Element element, String code) {
        return FavouriteMediaItem.builder()
                .id(mapper.with(element)
                        .attr("data-id")
                        .toLong()
                        .orThrowWithCode("favourite." + code + ".id")
                )
                .urlType(mapper.with(element)
                        .selectFirst("h3 > a")
                        .attr("href")
                        .pattern(MEDIA_URL_TYPE_MATCHER)
                        .mapTo(UrlType.class)
                        .orThrowWithCode("favourite." + code + ".url-type")
                )
                .title(mapper.with(element)
                        .selectFirst("h3 > a")
                        .text()
                        .orThrowWithCode("favourite." + code + ".title")
                )
                .imageUrl(mapper.with(element)
                        .selectFirst("img")
                        .attr("src")
                        .orThrowWithCode("favourite." + code + ".image-url")
                )
                .type(mapper.with(element)
                        .selectFirst("span:nth-of-type(2)")
                        .text()
                        .mapTo(TitleType.class)
                        .orThrowWithCode("favourite." + code + ".type")
                )
                .year(mapper.with(element)
                        .selectFirst("span")
                        .text()
                        .toInteger()
                        .orThrowWithCode("favourite." + code + ".year")
                )
                .build();
    }

    private EntityOverview createFavouriteEntity(Element element, String code) {
        return EntityOverview.builder()
                .id(mapper.with(element)
                        .attr("data-id")
                        .toLong()
                        .orThrowWithCode("favourite." + code + ".id")
                )
                .urlType(mapper.with(element)
                        .selectFirst("h3 > a")
                        .attr("href")
                        .pattern(MEDIA_URL_TYPE_MATCHER)
                        .mapTo(UrlType.class)
                        .orThrowWithCode("favourite." + code + ".url-type")
                )
                .imageUrl(mapper.with(element)
                        .selectFirst("img")
                        .attr("src")
                        .orThrowWithCode("favourite." + code + ".image-url")
                )
                .firstName(mapper.with(element)
                        .selectFirst("h3 > a")
                        .text()
                        .pattern(FIRST_NAME_MATCHER)
                        .orElse(null)
                )
                .lastName(mapper.with(element)
                        .selectFirst("h3 > a")
                        .text()
                        .pattern(LAST_NAME_MATCHER)
                        .orElse(null)
                )
                .mediaId(mapper.with(element)
                        .selectFirst("div > a")
                        .attr("href")
                        .pattern(MEDIA_ID_MATCHER)
                        .toLong()
                        .orElse(null)
                )
                .mediaUrlType(mapper.with(element)
                        .selectFirst("div > a")
                        .attr("href")
                        .pattern(MEDIA_URL_TYPE_MATCHER)
                        .mapTo(UrlType.class)
                        .orElse(null)
                )
                .mediaTitle(mapper.with(element)
                        .selectFirst("div > a")
                        .text()
                        .orElse(null)
                )
                .build();
    }

    private ListItemOverview createListItemOverview(Element item, String code) {
        return ListItemOverview.builder()
                .id(mapper.with(item)
                        .selectFirst("a")
                        .attr("href")
                        .pattern(MEDIA_ID_MATCHER)
                        .toLong()
                        .orThrowWithCode("list-item." + code + ".id")
                )
                .urlType(mapper.with(item)
                        .selectFirst("a")
                        .attr("href")
                        .pattern(MEDIA_URL_TYPE_MATCHER)
                        .mapTo(UrlType.class)
                        .orThrowWithCode("list-item." + code + ".url-type")
                )
                .imageUrl(mapper.with(item)
                        .selectFirst("img")
                        .attr("src")
                        .orThrowWithCode("list-item." + code + ".image-url")
                )
                .title(mapper.with(item)
                        .selectFirst("h4 > a")
                        .text()
                        .orThrowWithCode("list-item." + code + ".title")
                )
                .lastModifiedAt(mapper.with(item)
                        .selectFirst("span time")
                        .attr("datetime")
                        .toLocalDateTime()
                        .orThrowWithCode("list-item." + code + ".last-modified-at")
                )
                .status(mapper.with(item)
                        .selectFirst("span")
                        .ownText()
                        .replace(":", "")
                        .mapTo(UserTitleStatus.class)
                        .orThrowWithCode("list-item." + code + ".status")
                )
                .build();
    }

    private Comment createComment(Element element) {
        return Comment.builder()
                .id(mapper.with(element)
                        .selectFirst("h3 > a:nth-of-type(2)")
                        .attr("href")
                        .replace("/comment/", "")
                        .toLong()
                        .orThrowWithCode("comment.id")
                )
                .userId(mapper.with(element)
                        .selectFirst("h3 > a")
                        .attr("href")
                        .pattern(USER_ID_MATCHER)
                        .toLong()
                        .orThrowWithCode("comment.user-id")
                )
                .username(mapper.with(element)
                        .selectFirst("h3 > a")
                        .text()
                        .orThrowWithCode("comment.username")
                )
                .userAvatar(mapper.with(element)
                        .selectFirst("img")
                        .attr("src")
                        .orThrowWithCode("comment.user-avatar")
                )
                .userRole(mapper.with(element)
                        .selectFirst("div.img > ul")
                        .text()
                        .orThrowWithCode("comment.user-role")
                )
                .userSignature(mapper.with(element)
                        .selectFirst("div.media-comment-signature")
                        .text()
                        .orElse(null)
                )
                .content(mapper.with(element)
                        .selectFirst("p")
                        .text()
                        .keepNewLines()
                        .orThrowWithCode("comment.content")
                )
                .createdAt(mapper.with(element)
                        .selectFirst("span")
                        .attr("title")
                        .toLocalDateTime()
                        .orThrowWithCode("comment.created-at")
                )
                .build();
    }

}
