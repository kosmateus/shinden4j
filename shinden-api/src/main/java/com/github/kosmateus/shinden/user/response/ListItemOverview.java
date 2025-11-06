package com.github.kosmateus.shinden.user.response;

import com.github.kosmateus.shinden.common.enums.UrlType;
import com.github.kosmateus.shinden.user.common.enums.UserTitleStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Represents a brief overview of an item in a user's list, such as an anime or manga.
 * <p>
 * The {@code ListItemOverview} class provides essential details about a list item, including
 * its title, associated URL, image, last modified timestamp, and current status. This class is
 * typically used to display a summarized view of a user's list item, such as in a user's profile
 * or list view.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@Builder
@ToString
public class ListItemOverview {

    /**
     * The unique identifier for the list item.
     * <p>
     * This ID is used to uniquely identify the item within the user's list. It could represent
     * an anime or manga title, or any other media item that a user has added to their list.
     * </p>
     */
    private final Long id;

    /**
     * The type of URL associated with the list item (e.g., "anime", "manga").
     * <p>
     * The {@link UrlType} enum specifies the category of the list item, which helps determine
     * how the URL for the item should be constructed or displayed in the application.
     * </p>
     */
    private final UrlType urlType;

    /**
     * The title of the list item.
     * <p>
     * Represents the name of the media item (e.g., the name of an anime or manga)
     * as it appears in the user's list.
     * </p>
     */
    private final String title;

    /**
     * The URL of the list item's image.
     * <p>
     * A URL pointing to an image associated with the list item, such as a cover art or thumbnail.
     * This is typically used to visually represent the item in a list view.
     * </p>
     */
    private final String imageUrl;

    /**
     * The timestamp when the list item was last modified.
     * <p>
     * This field indicates the date and time when the list item was last updated. It is useful
     * for tracking changes or recent updates made by the user to their list.
     * </p>
     */
    private final LocalDateTime lastModifiedAt;

    /**
     * The current status of the list item (e.g., "Watching", "Completed").
     * <p>
     * The {@link UserTitleStatus} enum represents the user's current engagement or progress status
     * with the list item, such as whether the user is currently watching, has completed, or has dropped
     * the media item.
     * </p>
     */
    private final UserTitleStatus status;
}
