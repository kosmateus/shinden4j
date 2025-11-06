package com.github.kosmateus.shinden.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration representing different types of URLs associated with entities or media titles.
 * <p>
 * The {@code UrlType} enum defines various types of URLs that can be associated with entities
 * (such as characters or staff) or media titles (such as series or manga). Each enum constant
 * represents a specific type and is associated with a corresponding string value.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@RequiredArgsConstructor
public enum UrlType {

    /**
     * Represents a URL type for a series (e.g., anime or TV series).
     */
    SERIES("series"),

    /**
     * Represents a URL type for titles (e.g., a list of titles).
     */
    TITLES("titles"),

    /**
     * Represents a URL type for a character (e.g., anime or manga character).
     */
    CHARACTER("character"),

    /**
     * Represents a URL type for staff (e.g., anime or manga creators, voice actors).
     */
    STAFF("staff"),

    /**
     * Represents a URL type for manga.
     */
    MANGA("manga");

    /**
     * The string value associated with the URL type.
     */
    private final String value;

    /**
     * Returns the {@code UrlType} corresponding to the specified string value.
     * <p>
     * This method looks up and returns the {@code UrlType} that matches the provided string
     * value. If no match is found, it throws an {@link IllegalArgumentException}.
     * </p>
     *
     * @param value the string value representing the URL type
     * @return the corresponding {@code UrlType} enum constant
     * @throws IllegalArgumentException if the specified value does not match any URL type
     */
    public static UrlType fromValue(String value) {
        for (UrlType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown url type: " + value);
    }
}
