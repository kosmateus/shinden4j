package com.github.kosmateus.shinden.common.enums;

import com.github.kosmateus.shinden.http.request.QueryParam;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration representing the Motion Picture Association of America (MPAA) ratings.
 * <p>
 * The {@code MPAA} enum defines the various MPAA ratings that classify the suitability
 * of a film or television show for different audiences, based on its content. Each enum
 * constant corresponds to a specific rating category.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@RequiredArgsConstructor
public enum MPAA implements QueryParam {

    /**
     * General audiences – All ages admitted.
     */
    G("G"),

    /**
     * Parental guidance suggested – Some material may not be suitable for children.
     */
    PG("PG"),

    /**
     * Parents strongly cautioned – Some material may be inappropriate for children under 13.
     */
    PG_13("PG-13"),

    /**
     * Restricted – Under 17 requires accompanying parent or adult guardian.
     */
    R("R"),

    /**
     * Restricted Plus – Contains strong content. Suitable for adults only.
     */
    R_PLUS("R+"),

    /**
     * Adult Only – Explicit content. Not suitable for anyone under 17.
     */
    RX("Rx"),

    /**
     * Restricted Young – Under 17 requires accompanying parent or adult guardian.
     */
    RY("Ry");

    /**
     * The string value associated with the MPAA rating.
     */
    private final String value;

    /**
     * Returns the {@code MPAA} rating corresponding to the specified string value.
     * <p>
     * This method looks up and returns the {@code MPAA} enum constant that matches the provided string
     * value. If no match is found, it throws an {@link IllegalArgumentException}.
     * </p>
     *
     * @param value the string value representing the MPAA rating
     * @return the corresponding {@code MPAA} enum constant
     * @throws IllegalArgumentException if the specified value does not match any known MPAA rating
     */
    public static MPAA fromValue(String value) {
        for (MPAA mpaa : values()) {
            if (mpaa.value.equals(value)) {
                return mpaa;
            }
        }
        throw new IllegalArgumentException("Unknown MPAA rating: " + value);
    }

    /**
     * Returns the name of the query parameter.
     *
     * @return the parameter name as a {@link String}.
     */
    @Override
    public String getQueryParameter() {
        return "mpaaRating";
    }

    /**
     * Returns the value of the query parameter.
     *
     * @return the parameter value as a {@link String}.
     */
    @Override
    public String getQueryValue() {
        return value;
    }
}
