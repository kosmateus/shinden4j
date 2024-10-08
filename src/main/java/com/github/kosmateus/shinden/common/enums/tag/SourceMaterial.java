package com.github.kosmateus.shinden.common.enums.tag;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration representing different source materials for anime and related media.
 *
 * <p>Each source material has an associated unique identifier and a translation key for localization
 * purposes, along with predefined query parameters used for API searches.</p>
 *
 * @version 1.0.0
 */
@Getter
@RequiredArgsConstructor
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
    private final String tagType = "source";
    private final String queryParameter = "tag";
    private final String animeSearchQueryParameter = "genres";

    @Override
    public String getQueryValue() {
        return String.valueOf(id);
    }
}
