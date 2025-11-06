package com.github.kosmateus.shinden.common.enums;

import com.github.kosmateus.shinden.i18n.Translatable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TitleConnectionType implements Translatable {

    PREQUEL("title.connection.prequel"),
    SEQUEL("title.connection.sequel"),
    ADAPTATION("title.connection.adaptation"),
    SOURCE_MATERIAL("title.connection.source-material"),
    ALTERNATIVE("title.connection.alternative"),
    SIDE_STORY("title.connection.side-story"),
    SPIN_OFF("title.connection.spin-off"),
    SUMMARY("title.connection.summary"),
    MAIN_STORY("title.connection.main-story"),
    CHARACTERS("title.connection.characters"),
    OTHER("title.connection.other");

    private final String translationKey;
}
