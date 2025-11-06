package com.github.kosmateus.shinden.user.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.kosmateus.shinden.common.enums.MPAA;
import com.github.kosmateus.shinden.common.enums.TitleStatus;
import com.github.kosmateus.shinden.common.enums.TitleType;
import com.github.kosmateus.shinden.user.common.enums.UserTitleStatus;
import com.github.kosmateus.shinden.utils.ShindenJsonDeserializer.BooleanDeserializer;
import com.github.kosmateus.shinden.utils.ShindenJsonDeserializer.MPAADeserializer;
import com.github.kosmateus.shinden.utils.ShindenJsonDeserializer.TitleStatusDeserializer;
import com.github.kosmateus.shinden.utils.ShindenJsonDeserializer.TitleTypeDeserializer;
import com.github.kosmateus.shinden.utils.ShindenJsonDeserializer.UserTitleStatusDeserializer;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;

/**
 * Represents an item in a user's anime list.
 * <p>
 * The {@code AnimeListItem} class encapsulates detailed information about an anime
 * that is part of a user's anime list. This includes fields such as the anime's title,
 * image URL, user and total scores, episode counts, type, status, and a brief description.
 * </p>
 *
 * <p>
 * Each field is mapped to a corresponding JSON property using Jackson annotations to
 * facilitate deserialization from a JSON response.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@Builder
@ToString
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnimeListItem {

    /**
     * The unique identifier of the anime title.
     */
    @JsonProperty("titleId")
    private final Long id;

    /**
     * The user's watch status for the anime.
     */
    @JsonProperty("watchStatus")
    @JsonDeserialize(using = UserTitleStatusDeserializer.class)
    private final UserTitleStatus status;

    /**
     * Indicates whether the anime is marked as a favourite by the user.
     */
    @JsonProperty("isFavourite")
    @JsonDeserialize(using = BooleanDeserializer.class)
    private final boolean isFavourite;

    /**
     * Indicates whether the anime is subject to DMCA (Digital Millennium Copyright Act) restrictions.
     */
    @JsonProperty("dmca")
    @JsonDeserialize(using = BooleanDeserializer.class)
    private final boolean dmca;

    /**
     * The title of the anime.
     */
    @JsonProperty("title")
    private final String title;

    /**
     * The URL or identifier for the cover image of the anime.
     */
    @JsonProperty("coverId")
    private final String image;

    /**
     * The premiere date of the anime.
     */
    @JsonProperty("premiereDate")
    private final LocalDate startDate;

    /**
     * The precision level of the premiere date.
     */
    @JsonProperty("premierePrecision")
    private final Integer premierePrecision;

    /**
     * The date when the anime finished airing.
     */
    @JsonProperty("finishDate")
    private final LocalDate endDate;

    /**
     * The precision level of the finish date.
     */
    @JsonProperty("finishPrecision")
    private final Integer finishPrecision;

    /**
     * The current status of the anime (e.g., airing, finished).
     */
    @JsonProperty("titleStatus")
    @JsonDeserialize(using = TitleStatusDeserializer.class)
    private final TitleStatus titleStatus;

    /**
     * The MPAA rating of the anime.
     */
    @JsonProperty("mpaaRating")
    @JsonDeserialize(using = MPAADeserializer.class)
    private final MPAA mpaa;

    /**
     * The average rating for the characters in the anime.
     */
    @JsonProperty("summaryRatingTitlecahracters")
    private final Double charactersRating;

    /**
     * The total average rating of the anime.
     */
    @JsonProperty("summaryRatingTotal")
    private final Double totalRating;

    /**
     * The average rating for the story of the anime.
     */
    @JsonProperty("summaryRatingStory")
    private final Double storyRating;

    /**
     * The total number of episodes in the anime.
     */
    @JsonProperty("episodes")
    private final Integer totalEpisodes;

    /**
     * The type of the anime (e.g., TV, OVA, Movie).
     */
    @JsonProperty("animeType")
    @JsonDeserialize(using = TitleTypeDeserializer.class)
    private final TitleType type;

    /**
     * The average rating for the music in the anime.
     */
    @JsonProperty("summaryRatingMusic")
    private final Double musicRating;

    /**
     * The average rating for the graphics in the anime.
     */
    @JsonProperty("summaryRatingGraphics")
    private final Double graphicRating;

    /**
     * The number of episodes watched by the user.
     */
    @JsonProperty("watchedEpisodesCnt")
    private final Integer watchedEpisodes;

    /**
     * The total rating given by the user.
     */
    @JsonProperty("rateTotal")
    private final Integer userTotalRating;

    /**
     * The rating given by the user for the story of the anime.
     */
    @JsonProperty("rateStory")
    private final Integer userStoryRating;

    /**
     * The rating given by the user for the graphics of the anime.
     */
    @JsonProperty("rateGraphic")
    private final Integer userGraphicRating;

    /**
     * The rating given by the user for the music of the anime.
     */
    @JsonProperty("rateMusic")
    private final Integer userMusicRating;

    /**
     * The rating given by the user for the characters in the anime.
     */
    @JsonProperty("rateCharacters")
    private final Integer userCharactersRating;

    /**
     * A note added by the user regarding the anime.
     */
    @JsonProperty("userNote")
    private final String userNote;

    /**
     * Indicates whether the user's note is private.
     */
    @JsonProperty("userNoteIsPrivate")
    @JsonDeserialize(using = BooleanDeserializer.class)
    private final boolean userNotePrivate;

    /**
     * The English description of the anime.
     */
    @JsonProperty("descriptionEn")
    private final String descriptionEn;

    /**
     * The description of the anime in Polish.
     */
    @JsonProperty("descriptionPl")
    private final String description;
}
