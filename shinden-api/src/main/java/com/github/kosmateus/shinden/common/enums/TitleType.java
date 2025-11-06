package com.github.kosmateus.shinden.common.enums;

import com.github.kosmateus.shinden.anime.request.AnimeSearchQueryParam;
import com.github.kosmateus.shinden.http.request.QueryParam;
import com.github.kosmateus.shinden.i18n.Translatable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration representing the type of media title (e.g., anime).
 * <p>
 * The {@code TitleType} enum defines the various types a media title can belong to, such as "TV", "OVA", or "Movie."
 * Each enum constant is associated with a display value and a translation key for internationalization purposes.
 * This enum also implements the {@link Translatable} interface to support localization.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@RequiredArgsConstructor
public enum TitleType implements Translatable, QueryParam, AnimeSearchQueryParam {

    /**
     * Represents a TV series.
     */
    TV("TV", "title.type.tv"),

    /**
     * Represents an Original Video Animation (OVA).
     */
    OVA("OVA", "title.type.ova"),

    /**
     * Represents an Original Net Animation (ONA).
     */
    ONA("ONA", "title.type.ona"),

    /**
     * Represents a feature-length movie.
     */
    MOVIE("Movie", "title.type.movie"),

    /**
     * Represents a special episode or feature, typically a bonus or extra.
     */
    SPECIAL("Special", "title.type.special"),

    /**
     * Represents a music video or concert.
     */
    MUSIC("Music", "title.type.music");

    public static final String ANIME_SEARCH_QUERY_PARAM = "series_type[]";
    /**
     * The display value associated with the title type.
     */
    private final String value;

    /**
     * The translation key used for internationalization of the title type.
     */
    private final String translationKey;
    private final String queryParameter = "animeType";
    private final String animeSearchQueryParameter = ANIME_SEARCH_QUERY_PARAM;

    @Override
    public String getQueryValue() {
        return value;
    }

    /**
     * Returns the {@code TitleType} corresponding to the specified value.
     * <p>
     * This method looks up and returns the {@code TitleType} enum constant that matches the provided
     * display value. If no match is found, it throws an {@link IllegalArgumentException}.
     * </p>
     *
     * @param value the display value representing the title type
     * @return the corresponding {@code TitleType} enum constant
     * @throws IllegalArgumentException if the specified value does not match any known title type
     */
    public static TitleType fromValue(String value) {
        for (TitleType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown title type: " + value);
    }
}
