package com.github.kosmateus.shinden.user.response;

import com.github.kosmateus.shinden.common.enums.TitleType;
import com.github.kosmateus.shinden.common.enums.UrlType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Represents a favorite media item, such as an anime or manga, marked by a user.
 * <p>
 * The {@code FavouriteMediaItem} class encapsulates key details about a media item that
 * a user has marked as a favorite. This includes the item's title, image, type, and the year
 * it was released. The class provides a summarized view of the media item, suitable for
 * displaying in user profiles or favorite lists.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@Builder
@ToString
public class FavouriteMediaItem {

    /**
     * The unique identifier for the media item.
     * <p>
     * This ID uniquely identifies the media item (e.g., an anime or manga) within the system.
     * </p>
     */
    private final Long id;

    /**
     * The type of URL associated with the media item (e.g., "anime", "manga").
     * <p>
     * The {@link UrlType} indicates the category of the media item, such as "anime" or "manga".
     * This helps in generating the correct URL for accessing the media's detail page.
     * </p>
     */
    private final UrlType urlType;

    /**
     * The title of the media item.
     * <p>
     * Represents the name of the media item (e.g., an anime or manga title) as it is known
     * in the system. This is the display name used in user interfaces.
     * </p>
     */
    private final String title;

    /**
     * The URL of the media item's image.
     * <p>
     * A URL pointing to the image associated with the media item, such as a cover or poster.
     * This image can be displayed in user interfaces alongside the media's title.
     * </p>
     */
    private final String imageUrl;

    /**
     * The type of the media item (e.g., TV series, Movie, OVA).
     * <p>
     * The {@link TitleType} represents the format or category of the media item, such as a TV series,
     * Movie, or OVA (Original Video Animation). This helps users quickly understand the nature
     * of the media.
     * </p>
     */
    private final TitleType type;

    /**
     * The year the media item was released.
     * <p>
     * Represents the release year of the media item. This can be useful for identifying when the media
     * was first made available or aired, providing additional context to the user.
     * </p>
     */
    private final Integer year;
}
