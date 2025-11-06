package com.github.kosmateus.shinden.anime.response;

import com.github.kosmateus.shinden.common.enums.MPAA;
import com.github.kosmateus.shinden.common.enums.TitleConnectionType;
import com.github.kosmateus.shinden.common.enums.TitleStatus;
import com.github.kosmateus.shinden.common.enums.TitleType;
import com.github.kosmateus.shinden.common.enums.UrlType;
import com.github.kosmateus.shinden.enums.tag.CharacterType;
import com.github.kosmateus.shinden.enums.tag.Genre;
import com.github.kosmateus.shinden.enums.tag.Other;
import com.github.kosmateus.shinden.enums.tag.PlaceAndTime;
import com.github.kosmateus.shinden.enums.tag.SourceMaterial;
import com.github.kosmateus.shinden.enums.tag.Studio;
import com.github.kosmateus.shinden.enums.tag.TargetGroup;
import com.github.kosmateus.shinden.i18n.Translatable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class AnimeDetails {
    private final Long id;
    private final String title;
    private final List<String> alternativeTitles;
    private final String description;
    private final String image;
    @Nullable
    private final UserRating userRating;
    private final Rating rating;
    private final Tags tags;
    private final Information information;
    private final GeneralStatistics generalStatistics;
    private final List<Episode> episodes;
    private final List<CreatedBy> pageCreators;
    private final List<ConnectedTitle> connectedTitles;
    private final List<Character> characters;
    private final List<Staff> staff;
    private final List<ForumTopic> forumTopics;
    private final List<Review> reviews;
    private final List<TitleRecommendation> recommendations;
    @Nullable
    private final Statistics statistics;

    @Getter
    @RequiredArgsConstructor
    public enum RoleType implements Translatable {
        MAIN("character.role.main"),
        SUPPORTING("character.role.supporting");

        private final String translationKey;
    }

    @Getter
    @RequiredArgsConstructor
    public enum AgeType implements Translatable {
        UNKNOWN("nieznany", "age.type.unknown"),
        AGE_55_TO_150("55-150", "age.type.55-150"),
        AGE_41_TO_54("41-54", "age.type.41-54"),
        AGE_35_TO_40("35-40", "age.type.35-40"),
        AGE_30_TO_34("30-34", "age.type.30-34"),
        AGE_25_TO_29("25-29", "age.type.25-29"),
        AGE_19_TO_24("19-24", "age.type.19-24"),
        AGE_16_TO_18("16-18", "age.type.16-18"),
        AGE_13_TO_15("13-15", "age.type.13-15"),
        AGE_5_TO_12("5-12", "age.type.5-12"),
        UNRECOGNIZED("invalid", "enum.unrecognized");

        private final String value;
        private final String translationKey;

        public static AgeType fromValue(String value) {
            for (AgeType ageType : values()) {
                if (ageType.getValue().equals(value)) {
                    return ageType;
                }
            }
            return UNRECOGNIZED;
        }
    }

    @Getter
    @RequiredArgsConstructor
    public enum RateType implements Translatable {
        RATE_1("1", "rate.1"),
        RATE_2("2", "rate.2"),
        RATE_3("3", "rate.3"),
        RATE_4("4", "rate.4"),
        RATE_5("5", "rate.5"),
        RATE_6("6", "rate.6"),
        RATE_7("7", "rate.7"),
        RATE_8("8", "rate.8"),
        RATE_9("9", "rate.9"),
        RATE_10("10", "rate.10"),
        UNRECOGNIZED("invalid", "enum.unrecognized");


        private final String value;
        private final String translationKey;

        public static RateType fromValue(String value) {
            for (RateType rateType : values()) {
                if (rateType.getValue().equals(value)) {
                    return rateType;
                }
            }
            return UNRECOGNIZED;
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Rating {
        private final Float overall;
        private final Integer votes;
        private final Float story;
        private final Float graphics;
        private final Float music;
        private final Float characters;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class UserRating {
        private final Integer overall;
        private final Integer story;
        private final Integer graphics;
        private final Integer music;
        private final Integer characters;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Tags {
        private final List<Genre> genres;
        private final List<TargetGroup> targetGroups;
        private final List<Other> otherTags;
        private final List<PlaceAndTime> placeAndTimeTags;
        private final List<CharacterType> characterTypes;
        private final List<SourceMaterial> sourceMaterials;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Information {
        private final TitleType type;
        private final TitleStatus status;
        private final LocalDate startDate;
        private final LocalDate endDate;
        private final Integer episodes;
        private final List<Studio> studios;
        private final Integer episodeDuration;
        private final MPAA mpaa;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class GeneralStatistics {
        private final Integer currentlyWatching;
        private final Integer completed;
        private final Integer skipped;
        private final Integer onHold;
        private final Integer dropped;
        private final Integer planToWatch;
        private final Integer likes;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CreatedBy {
        private final Integer id;
        private final String name;
        private final String image;
        private final Float score;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ConnectedTitle {
        private final Integer id;
        private final String title;
        private final String image;
        private final UrlType urlType;
        private final TitleConnectionType connectionType;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Character {
        private final Integer id;
        private final String firstName;
        private final String middleName;
        private final String lastName;
        private final String image;
        private final RoleType role;
        private final List<VoiceActor> voiceActors;

        public String getFullName() {
            return String.join(" ", firstName, middleName, lastName);
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class VoiceActor {
        private final Integer id;
        private final String firstName;
        private final String middleName;
        private final String lastName;
        private final String image;
        private final String role;

        public String getFullName() {
            return String.join(" ", firstName, middleName, lastName);
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Staff {
        private final Integer id;
        private final String firstName;
        private final String middleName;
        private final String lastName;
        private final String image;
        private final String role;

        public String getFullName() {
            return String.join(" ", firstName, middleName, lastName);
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ForumTopic {
        private final Integer id;
        private final String title;
        private final String subForum;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Episode {
        @Nullable
        private final Integer id;
        private final String title;
        private final Float number;
        private final boolean filler;
        private final boolean available;
        private final List<String> languages;
        private final LocalDate releaseDate;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class TitleRecommendation {
        private final Integer id;
        private final String title;
        private final String image;
        private final Integer likes;
        private final Integer dislikes;
        private final Integer userId;
        private final String userName;
        private final String userImage;
        private final String content;
        private final LocalDateTime date;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Statistics {
        private final List<DemographicVotes> maleVotes;
        private final List<DemographicVotes> femaleVotes;
        private final List<DemographicVotes> unknownVotes;
        private final List<Rate> overallRates;
        private final List<Rate> storyRates;
        private final List<Rate> graphicsRates;
        private final List<Rate> musicRates;
        private final List<Rate> characterRates;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class DemographicVotes {
        private final AgeType age;
        private final Integer votes;
        private final Float score;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Rate {
        private final RateType type;
        private final Integer votes;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Review {
        private final Integer id;
        private final String content;
        private final Integer likes;
        private final Integer dislikes;
        private final Integer userId;
        private final String userName;
        private final String userImage;
        private final Integer displayCount;
        private final Integer readCount;
    }

}
