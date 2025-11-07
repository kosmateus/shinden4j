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
 * Generated: 2025-11-07T18:08:56.6578393
 */
@Getter
public enum Genre implements Tag {
    ACTION(5, "tags.genre.action"),
    CYBERPUNK(106, "tags.genre.cyberpunk"),
    DRAMA(8, "tags.genre.drama"),
    ECCHI(78, "tags.genre.ecchi"),
    EKSPERYMENTALNE(1741, "tags.genre.eksperymentalne"),
    FANTASY(22, "tags.genre.fantasy"),
    HAREM(130, "tags.genre.harem"),
    HENTAI(234, "tags.genre.hentai"),
    HISTORICAL(92, "tags.genre.historical"),
    HORROR(51, "tags.genre.horror"),
    COMEDY(7, "tags.genre.comedy"),
    POLICE(20, "tags.genre.police"),
    MAGIC(18, "tags.genre.magic"),
    MECHA(98, "tags.genre.mecha"),
    REVERSE_HAREM(263, "tags.genre.reverse-harem"),
    MUSIC(136, "tags.genre.music"),
    SUPERNATURAL(19, "tags.genre.supernatural"),
    DEMENTIA(97, "tags.genre.dementia"),
    SLICE_OF_LIFE(42, "tags.genre.slice-of-life"),
    PARODY(165, "tags.genre.parody"),
    ADVENTURE(6, "tags.genre.adventure"),
    PSYCHOLOGICAL(52, "tags.genre.psychological"),
    ROMANS(2672, "tags.genre.romans"),
    ROMANCE_OLD(38, "tags.genre.romance-old"),
    SCIENCE_FICTION(549, "tags.genre.science-fiction"),
    SHOUJO_AI(167, "tags.genre.shoujo-ai"),
    SHOUNEN_AI(207, "tags.genre.shounen-ai"),
    SPACE_OPERA(384, "tags.genre.space-opera"),
    SPORTS(31, "tags.genre.sports"),
    STEAMPUNK(1734, "tags.genre.steampunk"),
    SCHOOL(65, "tags.genre.school"),
    MARTIAL_ARTS(57, "tags.genre.martial-arts"),
    MYSTERY(12, "tags.genre.mystery"),
    THRILLER(53, "tags.genre.thriller"),
    MILITARY(93, "tags.genre.military"),
    YAOI(364, "tags.genre.yaoi"),
    YURI(380, "tags.genre.yuri");

    private final Integer id;
    private final String translationKey;
    private final String tagType;
    private final String queryParameter;
    private final String animeSearchQueryParameter;

    Genre(Integer id, String translationKey) {
        this.id = id;
        this.translationKey = translationKey;
        this.tagType = "genre";
        this.queryParameter = "tag";
        this.animeSearchQueryParameter = "genres";
    }

    public static Genre fromValue(Integer value) {
        return Stream.of(Genre.values())
                .filter(e -> e.id.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No Genre with id " + value));
    }

    public static Genre fromValueOrNull(Integer value) {
        return Stream.of(Genre.values())
                .filter(e -> e.id.equals(value))
                .findFirst()
                .orElse(null);
    }

    public static Genre fromValueOrNull(String value) {
        return fromValueOrNull(Integer.parseInt(value));
    }

    public static Genre fromValue(String value) {
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
