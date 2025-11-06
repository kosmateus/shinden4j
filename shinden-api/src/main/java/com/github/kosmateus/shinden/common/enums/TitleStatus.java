package com.github.kosmateus.shinden.common.enums;

import com.github.kosmateus.shinden.anime.request.AnimeSearchQueryParam;
import com.github.kosmateus.shinden.http.request.QueryParam;
import com.github.kosmateus.shinden.i18n.Translatable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration representing the status of a title (e.g., anime or manga).
 *
 * <p>The {@code TitleStatus} enum defines the various states a media title can have, such as "Currently Airing"
 * or "Finished Airing." Each enum constant is associated with a value for display and a translation key for
 * internationalization purposes. This enum also implements the {@link Translatable} and {@link QueryParam}
 * interfaces to support translation and usage as a query parameter in HTTP requests.</p>
 *
 * @version 1.0.0
 */
@Getter
@RequiredArgsConstructor
public enum TitleStatus implements Translatable, QueryParam, AnimeSearchQueryParam {

    /**
     * Indicates that the title is a proposal and not yet confirmed for production.
     */
    PROPOSAL("Proposal", "user.settings.edit.title-status.proposal"),

    /**
     * Indicates that the title is currently airing.
     */
    CURRENTLY_AIRING("Currently Airing", "user.settings.edit.title-status.airing"),

    /**
     * Indicates that the title is scheduled to air in the future.
     */
    NOT_YET_AIRED("Not yet aired", "user.settings.edit.title-status.not-yet-aired"),

    /**
     * Indicates that the title has finished airing.
     */
    FINISHED_AIRING("Finished Airing", "user.settings.edit.title-status.finished");

    public static final String ANIME_SEARCH_QUERY_PARAM = "series_status[]";

    /**
     * The display value associated with the title status.
     */
    private final String value;

    /**
     * The translation key used for internationalization of the title status.
     */
    private final String translationKey;
    private final String queryParameter = "titleStatus";
    private final String animeSearchQueryParameter = ANIME_SEARCH_QUERY_PARAM;

    /**
     * Returns the {@code TitleStatus} corresponding to the specified value.
     *
     * <p>This method looks up and returns the {@code TitleStatus} enum constant that matches the provided
     * display value. If no match is found, it throws an {@link IllegalArgumentException}.</p>
     *
     * @param value the display value representing the title status
     * @return the corresponding {@code TitleStatus} enum constant
     * @throws IllegalArgumentException if the specified value does not match any known title status
     */
    public static TitleStatus fromValue(String value) {
        for (TitleStatus status : values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        for (TitleStatus status : values()) {
            if (status.getTranslation().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown title status: " + value);
    }

    @Override
    public String getQueryValue() {
        return value;
    }
}
