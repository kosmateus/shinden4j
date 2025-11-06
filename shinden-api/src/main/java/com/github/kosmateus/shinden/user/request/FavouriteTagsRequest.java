package com.github.kosmateus.shinden.user.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.kosmateus.shinden.http.request.HttpRequest.KeyValue;
import com.github.kosmateus.shinden.http.request.QueryParam;
import com.github.kosmateus.shinden.i18n.Translatable;
import com.github.kosmateus.shinden.user.common.UserId;
import com.github.kosmateus.shinden.utils.QueryParamsBuilder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Represents a request to retrieve a user's favourite tags.
 * <p>
 * The {@code FavouriteTagsRequest} class encapsulates the parameters needed to query
 * for a user's favourite tags, including the user ID, list type (anime or manga),
 * rate type, and tag type. It provides a method to convert these parameters into
 * a query string for use in an HTTP request.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@Builder
@ToString
@AllArgsConstructor(onConstructor_ = @JsonCreator)
public class FavouriteTagsRequest implements UserId {

    /**
     * The ID of the user for whom the favourite tags are being requested.
     * <p>
     * This field is mandatory and must be a non-null value.
     * </p>
     */
    @NotNull
    private final Long userId;

    /**
     * The type of list to query (anime, manga, or both).
     */
    private final ListType listType;

    /**
     * The type of rating to query (e.g., story, music).
     */
    private final RateType rateType;

    /**
     * The type of tag to query (e.g., genre, studio).
     */
    private final TagType tagType;

    /**
     * Creates a new builder for {@code FavouriteTagsRequest} with the specified user ID.
     *
     * @param userId the ID of the user for whom the favourite tags are being requested
     * @return a new builder for {@code FavouriteTagsRequest}
     */
    public static FavouriteTagsRequest.FavouriteTagsRequestBuilder userId(Long userId) {
        return new FavouriteTagsRequest.FavouriteTagsRequestBuilder().userId(userId);
    }

    /**
     * Converts the parameters of this request into a list of query parameters.
     * <p>
     * This method constructs a list of query parameters based on the list type, rate type, and tag type
     * specified in this request. The resulting list can be used to build a query string for an HTTP request.
     * </p>
     *
     * @return a list of query parameters for this request
     */
    public List<KeyValue> toQueryParams() {
        return QueryParamsBuilder.convertToKeyVal(QueryParamsBuilder.build(listType, rateType, tagType));
    }

    /**
     * Enum representing the type of list to query (anime, manga, or both).
     */
    @Getter
    @RequiredArgsConstructor
    public enum ListType implements QueryParam, Translatable {

        /**
         * Query for both anime and manga.
         */
        BOTH("", "user.favorite.tags.list-type.both"),

        /**
         * Query for anime only.
         */
        ANIME("animes", "user.favorite.tags.list-type.anime"),

        /**
         * Query for manga only.
         */
        MANGA("mangas", "user.favorite.tags.list-type.manga");

        private final String value;
        private final String translationKey;

        @Override
        public String getQueryParameter() {
            return "list_type";
        }

        @Override
        public String getQueryValue() {
            return value;
        }
    }

    /**
     * Enum representing the type of rating to query (e.g., story, music).
     */
    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public enum RateType implements QueryParam, Translatable {

        /**
         * Query based on the total rating.
         */
        TOTAL("total", "user.favorite.tags.rate-type.total"),

        /**
         * Query based on the story rating.
         */
        STORY("story", "user.favorite.tags.rate-type.story"),

        /**
         * Query based on the graphic rating.
         */
        GRAPHIC("graphic", "user.favorite.tags.rate-type.graphic"),

        /**
         * Query based on the music rating.
         */
        MUSIC("music", "user.favorite.tags.rate-type.music"),

        /**
         * Query based on the line rating.
         */
        LINE("line", "user.favorite.tags.rate-type.line"),

        /**
         * Query based on the characters rating.
         */
        CHARACTERS("titlecahracters", "user.favorite.tags.rate-type.characters");

        private static final String REQUEST_PARAM = "rate_type";

        private final String value;
        private final String translationKey;

        @Override
        public String getQueryParameter() {
            return REQUEST_PARAM;
        }

        @Override
        public String getQueryValue() {
            return value;
        }
    }

    /**
     * Enum representing the type of tag to query (e.g., genre, studio).
     */
    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public enum TagType implements QueryParam, Translatable {

        /**
         * Query based on the genre tag.
         */
        GENRE("genre", "user.favorite.tags.tag-type.genre"),

        /**
         * Query based on the target group tag.
         */
        TARGET_GROUP("target_group", "user.favorite.tags.tag-type.target-group"),

        /**
         * Query based on the entity tag.
         */
        ENTITY("entity", "user.favorite.tags.tag-type.entity"),

        /**
         * Query based on the place tag.
         */
        PLACE("place", "user.favorite.tags.tag-type.place"),

        /**
         * Query based on the generic tag.
         */
        TAG("tag", "user.favorite.tags.tag-type.tag"),

        /**
         * Query based on the studio tag.
         */
        STUDIO("studio", "user.favorite.tags.tag-type.studio"),

        /**
         * Query based on the production type tag.
         */
        PRODUCTION_TYPE("production_type", "user.favorite.tags.tag-type.production-type"),

        /**
         * Query based on the serialization tag.
         */
        SERIALIZATION("serialization", "user.favorite.tags.tag-type.serialization"),

        /**
         * Query based on the source tag.
         */
        SOURCE("source", "user.favorite.tags.tag-type.source");

        private static final String REQUEST_PARAM = "tag_type";

        private final String value;
        private final String translationKey;

        @Override
        public String getQueryParameter() {
            return REQUEST_PARAM;
        }

        @Override
        public String getQueryValue() {
            return value;
        }
    }
}
