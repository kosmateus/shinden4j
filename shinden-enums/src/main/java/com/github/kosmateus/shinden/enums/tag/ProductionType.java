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
 * Generated: 2025-11-07T18:09:00.4335817
 */
@Getter
public enum ProductionType implements Tag {
    GRAFIKA_2_5D(2658, "tags.production-type.grafika-2-5d"),
    ANIMACJA_3D(2617, "tags.production-type.animacja-3d"),
    CHINESE_ANIMATION(2343, "tags.production-type.chinese-animation"),
    KOREANSKA_ANIMACJA(2634, "tags.production-type.koreanska-animacja"),
    ANTOLOGIA(2747, "tags.production-type.antologia"),
    BRAK_DIALOGOW(2743, "tags.production-type.brak-dialogow"),
    CHINSKO_JAPONSKA_KOPRODUKCJA(2604, "tags.production-type.chinsko-japonska-koprodukcja"),
    CZARNO_BIALE(2819, "tags.production-type.czarno-biale"),
    DOUJINSHI(1178, "tags.production-type.doujinshi"),
    REALNE_SCENERIE(2660, "tags.production-type.realne-scenerie"),
    EPIZODYCZNE(2646, "tags.production-type.epizodyczne"),
    INDEPENDENT_FILM(3036, "tags.production-type.independent-film"),
    PICTURE_DRAMA(2683, "tags.production-type.picture-drama"),
    PIONOWE_ANIME(2637, "tags.production-type.pionowe-anime"),
    REKLAMA(2753, "tags.production-type.reklama"),
    FULL_COLOR(2418, "tags.production-type.full-color"),
    WEBNOVEL(2878, "tags.production-type.webnovel"),
    WEBTOON(2877, "tags.production-type.webtoon"),
    WYDANE_W_PAPIERZE(2879, "tags.production-type.wydane-w-papierze"),
    WYDANE_W_POLSCE(2665, "tags.production-type.wydane-w-polsce"),
    YONKOMA(1884, "tags.production-type.yonkoma"),
    YOUNG_ANIMATOR_TRAINING_PROJECT(2644, "tags.production-type.young-animator-training-project");

    private final Integer id;
    private final String translationKey;
    private final String tagType;
    private final String queryParameter;
    private final String animeSearchQueryParameter;

    ProductionType(Integer id, String translationKey) {
        this.id = id;
        this.translationKey = translationKey;
        this.tagType = "productiontype";
        this.queryParameter = "tag";
        this.animeSearchQueryParameter = "genres";
    }

    public static ProductionType fromValue(Integer value) {
        return Stream.of(ProductionType.values())
                .filter(e -> e.id.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No ProductionType with id " + value));
    }

    public static ProductionType fromValueOrNull(Integer value) {
        return Stream.of(ProductionType.values())
                .filter(e -> e.id.equals(value))
                .findFirst()
                .orElse(null);
    }

    public static ProductionType fromValueOrNull(String value) {
        return fromValueOrNull(Integer.parseInt(value));
    }

    public static ProductionType fromValue(String value) {
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
