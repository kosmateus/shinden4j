package com.github.kosmateus.shinden.user.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * Represents a generic response wrapper for a list of items.
 * <p>
 * The {@code ListResponse} class is a generic wrapper designed to handle responses
 * that return a list of items along with additional metadata, such as the total count
 * of items. This class is typically used in REST API responses to encapsulate a list
 * of results and associated metadata.
 * </p>
 *
 * @param <T> the type of items contained in the list response
 * @version 1.0.0
 */
@Getter
@Builder
@ToString
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public final class ListResponse<T> {

    /**
     * The result containing the list of items and related metadata.
     * <p>
     * This field holds a {@link Result} object, which includes the list of items and
     * a count representing the total number of items in the result set.
     * </p>
     */
    @JsonProperty("result")
    private final Result<T> result;

    /**
     * Represents the result of a list response, containing the items and their count.
     * <p>
     * The {@code Result} class encapsulates the core data returned in a list response,
     * including a list of items of a generic type {@code T} and the total count of these items.
     * </p>
     *
     * @param <T> the type of items contained in the result
     */
    @Getter
    @Builder
    @ToString
    @Jacksonized
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Result<T> {

        /**
         * The total count of items in the result.
         * <p>
         * Represents the total number of items in the list, which can be useful for
         * pagination or understanding the size of the dataset returned by an API call.
         * </p>
         */
        @JsonProperty("count")
        private final Integer count;

        /**
         * The list of items in the result.
         * <p>
         * Contains the actual data returned by the API, represented as a list of items
         * of a generic type {@code T}. This list can include any type of object, depending
         * on the context of the API response.
         * </p>
         */
        @JsonProperty("items")
        private final List<T> items;
    }

}
