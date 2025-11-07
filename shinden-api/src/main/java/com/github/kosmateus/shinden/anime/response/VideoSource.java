package com.github.kosmateus.shinden.anime.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;


@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class VideoSource {
    private Long id;
    private String service;
    private VideoQuality quality;
    private String subtitlesLanguage;
    private String audioLanguage;
    private LocalDateTime createdAt;


    @Getter
    @RequiredArgsConstructor
    public enum VideoQuality {
        SD_360P("360p"),
        HD_480P("480p"),
        HD_720P("720p"),
        FULL_HD_1080P("1080p"),
        QHD_1440P("1440p"),
        UHD_4K("4k");
        private final String resolution;

        public static VideoQuality fromValue(String value) {
            for (VideoQuality quality : values()) {
                if (quality.getResolution().equalsIgnoreCase(value)) {
                    return quality;
                }
            }
            throw new IllegalArgumentException("Unknown video quality: " + value);
        }

    }


}
