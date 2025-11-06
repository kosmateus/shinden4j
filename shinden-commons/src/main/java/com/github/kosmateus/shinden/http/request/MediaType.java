package com.github.kosmateus.shinden.http.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum representing the media type for HTTP requests.
 * <p>
 * The {@code MediaType} enum provides a set of predefined media types that can be used
 * in HTTP requests. Each enum value corresponds to a specific media type string.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@AllArgsConstructor
public enum MediaType {
    /**
     * Media type representing JSON format.
     */
    APPLICATION_JSON("application/json");

    private final String value;
}
