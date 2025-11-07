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
 * Generated: 2025-11-07T18:08:58.4401023
 */
@Getter
public enum PlaceAndTime implements Tag {
    ALTERNATIVE_EARTH(2328, "tags.place-and-time.alternative-earth"),
    NORTH_AMERICA(1789, "tags.place-and-time.north-america"),
    OFFICE(2844, "tags.place-and-time.office"),
    APARTMENT_LIFE(2336, "tags.place-and-time.apartment-life"),
    CHINY(1949, "tags.place-and-time.chiny"),
    DUNGEON(2663, "tags.place-and-time.dungeon"),
    DYSTOPIA(2348, "tags.place-and-time.dystopia"),
    EUROPA(1745, "tags.place-and-time.europa"),
    FEUDAL_JAPAN(1730, "tags.place-and-time.feudal-japan"),
    JAK_FEUDALNA_WSCHODNIA_AZJA(3048, "tags.place-and-time.jak-feudalna-wschodnia-azja"),
    LIKE_GAME(2322, "tags.place-and-time.like-game"),
    MEDIEVAL(2362, "tags.place-and-time.medieval"),
    JAPONIA(1740, "tags.place-and-time.japonia"),
    CAFE(2341, "tags.place-and-time.cafe"),
    SOUTH_KOREA(3051, "tags.place-and-time.south-korea"),
    SPACE(10, "tags.place-and-time.space"),
    MIASTO(1785, "tags.place-and-time.miasto"),
    OCEAN(2363, "tags.place-and-time.ocean"),
    OMEGAVERSE(2875, "tags.place-and-time.omegaverse"),
    PODROZ(1788, "tags.place-and-time.podroz"),
    POST_APOCALYPTIC(470, "tags.place-and-time.post-apocalyptic"),
    FUTURE(2326, "tags.place-and-time.future"),
    DESERT(2988, "tags.place-and-time.desert"),
    ALTERNATIVE_WORLD(2327, "tags.place-and-time.alternative-world"),
    ALL_BOYS_SCHOOL(2333, "tags.place-and-time.all-boys-school"),
    ALL_GIRLS_SCHOOL(2332, "tags.place-and-time.all-girls-school"),
    VIRTUAL_REALITY(1729, "tags.place-and-time.virtual-reality"),
    GREAT_BRITAIN(2858, "tags.place-and-time.great-britain"),
    WIES(1784, "tags.place-and-time.wies"),
    WSPOLCZESNOSC(1739, "tags.place-and-time.wspolczesnosc"),
    ISLAND(2357, "tags.place-and-time.island");

    private final Integer id;
    private final String translationKey;
    private final String tagType;
    private final String queryParameter;
    private final String animeSearchQueryParameter;

    PlaceAndTime(Integer id, String translationKey) {
        this.id = id;
        this.translationKey = translationKey;
        this.tagType = "place";
        this.queryParameter = "tag";
        this.animeSearchQueryParameter = "genres";
    }

    public static PlaceAndTime fromValue(Integer value) {
        return Stream.of(PlaceAndTime.values())
                .filter(e -> e.id.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No PlaceAndTime with id " + value));
    }

    public static PlaceAndTime fromValueOrNull(Integer value) {
        return Stream.of(PlaceAndTime.values())
                .filter(e -> e.id.equals(value))
                .findFirst()
                .orElse(null);
    }

    public static PlaceAndTime fromValueOrNull(String value) {
        return fromValueOrNull(Integer.parseInt(value));
    }

    public static PlaceAndTime fromValue(String value) {
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
