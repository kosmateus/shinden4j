package com.github.kosmateus.shinden.user.mapper;

import com.github.kosmateus.shinden.user.common.AddToListSettings;
import com.github.kosmateus.shinden.user.common.AnimeListSettings;
import com.github.kosmateus.shinden.user.common.MangaListSettings;
import com.github.kosmateus.shinden.user.common.PageSettings;
import com.github.kosmateus.shinden.user.common.ReadTimeSettings;
import com.github.kosmateus.shinden.user.common.enums.ChapterLanguage;
import com.github.kosmateus.shinden.user.common.enums.ChapterStatus;
import com.github.kosmateus.shinden.user.common.enums.PageMainMenu;
import com.github.kosmateus.shinden.user.common.enums.PageTheme;
import com.github.kosmateus.shinden.user.common.enums.ShowOption;
import com.github.kosmateus.shinden.user.common.enums.SkipFillers;
import com.github.kosmateus.shinden.user.common.enums.SliderPosition;
import com.github.kosmateus.shinden.user.common.enums.StatusAutoChange;
import com.github.kosmateus.shinden.user.common.enums.SubtitlesLanguage;
import com.github.kosmateus.shinden.user.common.enums.UserTitleStatus;
import com.github.kosmateus.shinden.user.request.AddToListSettingsRequest;
import com.github.kosmateus.shinden.user.request.BaseSettingsRequest;
import com.github.kosmateus.shinden.user.request.ListsSettingsRequest;
import com.github.kosmateus.shinden.user.response.UserSettings;
import com.github.kosmateus.shinden.utils.jsoup.BaseDocumentMapper;
import com.google.common.collect.ImmutableMap;
import org.jsoup.Connection.KeyVal;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.github.kosmateus.shinden.utils.FormParamUtils.createTypeMapping;
import static com.github.kosmateus.shinden.utils.FormParamUtils.merge;
import static org.jsoup.helper.HttpConnection.KeyVal.create;

public class UserSettingsMapper extends BaseDocumentMapper {
    public UserSettings map(Document document) {
        return UserSettings.builder()
                .pageSettings(mapPageSettings(document))
                .readTimeSettings(mapReadTimeSettings(document))
                .addToListSettings(mapAddToListSettings(document))
                .animeListSettings(mapAnimeListSettings(document))
                .mangaListSettings(mapMangaListSettings(document))
                .build();
    }

    public List<KeyVal> map(Document entity, BaseSettingsRequest request) {
        return createPageSettingsFormData(entity, request, map(entity));
    }

    public List<KeyVal> map(Document entity, ListsSettingsRequest request) {
        return createListsSettingsFormData(entity, request, map(entity));
    }

    public List<KeyVal> map(Document entity, AddToListSettingsRequest request) {
        return createAddToListSettingsFormData(entity, request, map(entity));
    }

    @Override
    protected String getMapperCode() {
        return "user.settings.edit";
    }

    @Override
    protected Map<Class<?>, Function<String, ?>> typeMappers() {
        return ImmutableMap.ofEntries(
                createTypeMapping(PageTheme.class, PageTheme.values()),
                createTypeMapping(PageMainMenu.class, PageMainMenu.values()),
                createTypeMapping(SliderPosition.class, SliderPosition.values()),
                createTypeMapping(ShowOption.class, ShowOption.values()),
                createTypeMapping(SubtitlesLanguage.class, SubtitlesLanguage.values()),
                createTypeMapping(UserTitleStatus.class, UserTitleStatus.values()),
                createTypeMapping(SkipFillers.class, SkipFillers.values()),
                createTypeMapping(StatusAutoChange.class, StatusAutoChange.values()),
                createTypeMapping(ChapterLanguage.class, ChapterLanguage.values()),
                createTypeMapping(ChapterStatus.class, ChapterStatus.values())
        );
    }

    private List<KeyVal> createListsSettingsFormData(Document document, ListsSettingsRequest request, UserSettings currentUserSettings) {
        if (request.getMangaListSettings() == null && request.getAnimeListSettings() == null) {
            return Collections.emptyList();
        }

        MangaListSettings mangaListSettings = currentUserSettings.getMangaListSettings();
        AnimeListSettings animeListSettings = currentUserSettings.getAnimeListSettings();

        List<KeyVal> subtitlesLanguages = merge(animeListSettings.getSubtitlesLanguages(), request.getAnimeListSettings().getSubtitlesLanguages()).stream()
                .map(language -> create(language.getFormParameter(), language.getFormValue()))
                .collect(Collectors.toList());

        List<KeyVal> animeWatchStatuses = merge(animeListSettings.getAnimeWatchStatus(), request.getAnimeListSettings().getAnimeWatchStatus())
                .stream()
                .map(status -> create(status.getFormParameter(), status.getFormValue()))
                .collect(Collectors.toList());

        List<KeyVal> chaptersLanguages = merge(mangaListSettings.getChapterLanguages(), request.getMangaListSettings().getChapterLanguages())
                .stream()
                .map(language -> create(language.getFormParameter(), language.getFormValue()))
                .collect(Collectors.toList());

        List<KeyVal> mangaReadStatuses = merge(mangaListSettings.getMangaReadStatus(), request.getMangaListSettings().getMangaReadStatus())
                .stream()
                .map(status -> create(status.getFormParameter(), status.getFormValue()))
                .collect(Collectors.toList());

        List<KeyVal> formData = new ArrayList<>();
        formData.add(create("setting-type", "pedding-list"));
        formData.add(create("csrf", mapper.with(document)
                .selectFirst("form.horizontal-form input[name=csrf]")
                .attr("value")
                .orThrowWithCode("csrf")));
        formData.addAll(subtitlesLanguages);
        formData.addAll(animeWatchStatuses);
        formData.add(create(SkipFillers.NO.getFormParameter(), merge(animeListSettings.getSkipFillers(), request.getAnimeListSettings().getSkipFillers()).getFormValue()));
        formData.add(create(StatusAutoChange.NO.getFormParameter(), merge(animeListSettings.getStatusAutoChange(), request.getAnimeListSettings().getStatusAutoChange()).getFormValue()));
        formData.addAll(chaptersLanguages);
        formData.addAll(mangaReadStatuses);
        formData.add(create(StatusAutoChange.NO.getFormParameter(), merge(mangaListSettings.getStatusAutoChange(), request.getMangaListSettings().getStatusAutoChange()).getFormValue()));

        return formData;
    }

    private List<KeyVal> createAddToListSettingsFormData(Document document, AddToListSettingsRequest request, UserSettings currentUserSettings) {
        if (request == null) {
            return Collections.emptyList();
        }

        AddToListSettings addToListSettings = currentUserSettings.getAddToListSettings();
        return Arrays.asList(
                create("setting-type", "add-to-list"),
                create("csrf", mapper.with(document)
                        .selectFirst("form.creator-form.box input[name=csrf]")
                        .attr("value")
                        .orThrowWithCode("csrf")),
                create(SliderPosition.NO_LIMIT.getFormParameter(), merge(addToListSettings.getSliderPosition(), request.getSliderPosition()).getFormValue()),
                create(ShowOption.NO.getFormParameter(), merge(addToListSettings.getShowAddToList(), request.getShowAddToList()).getFormValue())
        );
    }

    private List<KeyVal> createPageSettingsFormData(Document document, BaseSettingsRequest request, UserSettings currentUserSettings) {
        if (request.getPageSettings() == null && request.getReadTimeSettings() == null) {
            return Collections.emptyList();
        }

        PageSettings pageSettings = currentUserSettings.getPageSettings();
        ReadTimeSettings readTimeSettings = currentUserSettings.getReadTimeSettings();
        return Arrays.asList(
                create("csrf", mapper.with(document)
                        .selectFirst("form.creator-form input[name=csrf]")
                        .attr("value")
                        .orThrowWithCode("csrf")),
                create(PageTheme.DEFAULT.getFormParameter(), merge(pageSettings.getPageTheme(), request.getPageSettings().getPageTheme()).getFormValue()),
                create(PageMainMenu.ALL.getFormParameter(), merge(pageSettings.getPageMainMenu(), request.getPageSettings().getPageMainMenu()).getFormValue()),
                create("manga_time", merge(readTimeSettings.getMangaChapterReadTime(), request.getReadTimeSettings().getMangaChapterReadTime()).toString()),
                create("novel_time", merge(readTimeSettings.getVisualNovelChapterReadTime(), request.getReadTimeSettings().getVisualNovelChapterReadTime()).toString())
        );
    }

    private MangaListSettings mapMangaListSettings(Document document) {
        return MangaListSettings.builder()
                .chapterLanguages(mapper.with(document)
                        .select("div.push0.col4.box input[type=checkbox][name~=^chap_lang\\[\\]][checked=checked]")
                        .mapTo(item -> mapper.with(item).attr("value").mapTo(ChapterLanguage.class).orThrowWithCode("chapter-language-single"))
                        .orThrowWithCode("chapter-languages")
                )
                .mangaReadStatus(mapper.with(document)
                        .select("div.push4.col4.box input[type=checkbox][name~=^chap_status\\[\\]][checked=checked]")
                        .mapTo(item -> mapper.with(item).attr("value").mapTo(ChapterStatus.class).orThrowWithCode("manga-read-status-single"))
                        .orThrowWithCode("manga-read-status")
                )
                .statusAutoChange(mapper.with(document)
                        .select("select[name=status_autochange] > option[selected=selected]")
                        .get(1)
                        .attr("value")
                        .mapTo(StatusAutoChange.class)
                        .orElse(StatusAutoChange.NO)
                )
                .build();
    }

    private AnimeListSettings mapAnimeListSettings(Document document) {
        return AnimeListSettings.builder()
                .subtitlesLanguages(mapper.with(document)
                        .select("div.push0.col4.box input[type=checkbox][name~=^lang\\[\\]][checked=checked]")
                        .mapTo(
                                item -> mapper.with(item).attr("value").mapTo(SubtitlesLanguage.class).orThrowWithCode("subtitles-language-single")
                        )
                        .orThrowWithCode("subtitles-languages")
                )
                .animeWatchStatus(mapper.with(document)
                        .select("div.push4.col4.box input[type=checkbox][name~=^status\\[\\]][checked=checked]")
                        .mapTo(item -> mapper.with(item).attr("value").mapTo(UserTitleStatus.class).orThrowWithCode("anime-watch-status-single"))
                        .orThrowWithCode("anime-watch-status")
                )
                .skipFillers(mapper.with(document)
                        .selectFirst("input[name=skip_filers][checked=checked]")
                        .attr("value")
                        .mapTo(SkipFillers.class)
                        .orThrowWithCode("skip-fillers")
                )
                .statusAutoChange(mapper.with(document)
                        .select("select[name=status_autochange] > option[selected=selected]")
                        .get(0)
                        .attr("value")
                        .mapTo(StatusAutoChange.class)
                        .orElse(StatusAutoChange.NO)
                )
                .build();
    }

    private AddToListSettings mapAddToListSettings(Document document) {
        return AddToListSettings.builder()
                .sliderPosition(mapper.with(document)
                        .selectFirst("select[name=steps] > option[selected=selected]")
                        .attr("value")
                        .mapTo(SliderPosition.class)
                        .orThrowWithCode("slider-position")
                )
                .showAddToList(mapper.with(document)
                        .selectFirst("select[name=show] > option[selected=selected]")
                        .attr("value")
                        .mapTo(ShowOption.class)
                        .orElse(ShowOption.NO)
                )
                .build();
    }

    private ReadTimeSettings mapReadTimeSettings(Document document) {
        return ReadTimeSettings.builder()
                .mangaChapterReadTime(mapper.with(document)
                        .selectFirst("input[name=manga_time]")
                        .attr("value")
                        .mapTo(Integer.class)
                        .orThrowWithCode("manga-chapter-read-time")
                )
                .visualNovelChapterReadTime(mapper.with(document)
                        .selectFirst("input[name=novel_time]")
                        .attr("value")
                        .mapTo(Integer.class)
                        .orThrowWithCode("visual-novel-chapter-read-time")
                )
                .build();
    }

    private PageSettings mapPageSettings(Document document) {
        return PageSettings.builder()
                .pageTheme(mapper.with(document)
                        .selectFirst("select#skin_id > option[selected=selected]")
                        .attr("value")
                        .mapTo(PageTheme.class)
                        .orElse(PageTheme.DEFAULT)
                )
                .pageMainMenu(mapper.with(document)
                        .selectFirst("select#pinned_menu > option[selected=selected]")
                        .attr("value")
                        .mapTo(PageMainMenu.class)
                        .orThrowWithCode("page-main-menu")
                )
                .build();
    }
}
