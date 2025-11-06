package com.github.kosmateus.shinden.common.enums;

import com.github.kosmateus.shinden.i18n.Translatable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TitleConnectionType implements Translatable {

    PREQUEL("title.connection.prequel"),
    SEQUEL("title.connection.sequel"),
    ALTERNATIVE("title.connection.alternative"),
    SIDE_STORY("title.connection.side-story"),
    SPIN_OFF("title.connection.spin-off"),
    SUMMARY("title.connection.summary"),
    CHARACTERS("title.connection.characters"),
    OTHER("title.connection.other");

    private final String translationKey;
}
