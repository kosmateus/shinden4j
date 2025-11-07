package com.github.kosmateus.shinden.anime.mapper;

import com.github.kosmateus.shinden.anime.response.VideoSource;
import com.github.kosmateus.shinden.utils.jsoup.BaseDocumentMapper;
import com.google.gson.reflect.TypeToken;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class VideoSourceMapper extends BaseDocumentMapper {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @Override
    protected String getMapperCode() {
        return "anime.episode.video.source";
    }

    public List<VideoSource> intialMapping(Document doc) {
        return mapper.with(doc)
                .selectFirst("section.box.episode-player-list table tbody")
                .select("tr", 1)
                .mapTo(this::mapToVideoSourceWithoutUrl)
                .orElse(Collections.emptyList());
    }


    private VideoSource mapToVideoSourceWithoutUrl(Element row) {

        Map<String, String> dataEpisode = gson.fromJson(mapper.with(row)
                .selectFirst("td.ep-buttons > a")
                .attr("data-episode")
                .orThrowWithCode("data-episode"), new TypeToken<Map<String, String>>() {
        }.getType());
        return VideoSource.builder()
                .id(Long.parseLong(dataEpisode.get("online_id")))
                .service(dataEpisode.get("player"))
                .audioLanguage(dataEpisode.get("lang_audio"))
                .subtitlesLanguage(dataEpisode.get("lang_subs"))
                .quality(VideoSource.VideoQuality.fromValue(dataEpisode.get("max_res")))
                .createdAt(LocalDateTime.parse(dataEpisode.get("added"), DATE_TIME_FORMATTER))
                .build();
    }

    public List<String> extractIframeSrc(Document doc) {
        return mapper.with(doc)
                .select("iframe")
                .mapTo(e -> e.attr("src"))
                .orThrowWithCode("anime.episode.video.iframe.src");
    }

}
