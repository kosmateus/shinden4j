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
 * Generated: 2025-11-07T18:08:57.0323484
 */
@Getter
public enum TargetGroup implements Tag {
    KIDS(218, "tags.target-group.kids"),
    JOSEI(39, "tags.target-group.josei"),
    SEINEN(48, "tags.target-group.seinen"),
    SHOUJO(128, "tags.target-group.shoujo"),
    SHOUNEN(23, "tags.target-group.shounen");

    private final Integer id;
    private final String translationKey;
    private final String tagType;
    private final String queryParameter;
    private final String animeSearchQueryParameter;

    TargetGroup(Integer id, String translationKey) {
        this.id = id;
        this.translationKey = translationKey;
        this.tagType = "targetgroup";
        this.queryParameter = "tag";
        this.animeSearchQueryParameter = "genres";
    }

    public static TargetGroup fromValue(Integer value) {
        return Stream.of(TargetGroup.values())
                .filter(e -> e.id.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No TargetGroup with id " + value));
    }

    public static TargetGroup fromValueOrNull(Integer value) {
        return Stream.of(TargetGroup.values())
                .filter(e -> e.id.equals(value))
                .findFirst()
                .orElse(null);
    }

    public static TargetGroup fromValueOrNull(String value) {
        return fromValueOrNull(Integer.parseInt(value));
    }

    public static TargetGroup fromValue(String value) {
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
