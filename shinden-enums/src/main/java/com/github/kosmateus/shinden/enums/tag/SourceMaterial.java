package com.github.kosmateus.shinden.enums.tag;

import com.github.kosmateus.shinden.enums.Tag;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * WARNING: This is an auto-generated class!
 * <p>
 * DO NOT MODIFY this file manually. Any changes will be overwritten.
 * <p>
 * To regenerate this class, run:
 * mvn clean compile -pl shinden-enums -am -Pgenerate-enums
 * <p>
 * Generated: 2025-11-07T18:09:02.4331057
 */
@Getter
public enum SourceMaterial implements Tag {
    ANIME(2314, "tags.source-material.anime"),
    GAME(193, "tags.source-material.game"),
    GAME_OTHER(2323, "tags.source-material.game-other"),
    INNE(2410, "tags.source-material.inne"),
    CARD_GAME(2016, "tags.source-material.card-game"),
    BOOK(2029, "tags.source-material.book"),
    LIGHT_NOVEL(1976, "tags.source-material.light-novel"),
    MANGA(1956, "tags.source-material.manga"),
    _4_KOMA_MANGA(1996, "tags.source-material.4-koma-manga"),
    NOVEL(2127, "tags.source-material.novel"),
    ORIGINAL(1966, "tags.source-material.original"),
    VISUAL_NOVEL(1990, "tags.source-material.visual-novel"),
    WEB_MANGA(2025, "tags.source-material.web-manga"),
    WEB_NOVEL(2872, "tags.source-material.web-novel");

    private final Integer id;
    private final String translationKey;
    private final String tagType;
    private final String queryParameter;
    private final String animeSearchQueryParameter;

    SourceMaterial(Integer id, String translationKey) {
        this.id = id;
        this.translationKey = translationKey;
        this.tagType = "source";
        this.queryParameter = "tag";
        this.animeSearchQueryParameter = "genres";
    }

    public static SourceMaterial fromValue(Integer value) {
        return Stream.of(SourceMaterial.values())
                .filter(e -> e.id.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No SourceMaterial with id " + value));
    }

    public static SourceMaterial fromValueOrNull(Integer value) {
        return Stream.of(SourceMaterial.values())
                .filter(e -> e.id.equals(value))
                .findFirst()
                .orElse(null);
    }

    public static SourceMaterial fromValueOrNull(String value) {
        return fromValueOrNull(Integer.parseInt(value));
    }

    public static SourceMaterial fromValue(String value) {
        return fromValue(Integer.parseInt(value));
    }

    @Override
    public String getTagType() {
        return tagType;
    }

    @Override
    public String getQueryValue() {
        return String.valueOf(id);
    }
}
