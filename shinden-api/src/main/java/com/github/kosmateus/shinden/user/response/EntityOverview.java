package com.github.kosmateus.shinden.user.response;

import com.github.kosmateus.shinden.common.enums.UrlType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Represents an overview of an entity, such as a character or person, associated with a media title.
 * <p>
 * The {@code EntityOverview} class encapsulates key details about an entity, including its name, image,
 * associated media, and related URLs. This class is typically used to provide a summarized view of an entity
 * within the context of a media title, such as in search results, lists, or summaries.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@Builder
@ToString
public class EntityOverview {

    /**
     * The unique identifier for the entity.
     * <p>
     * This ID is used to uniquely identify the entity (e.g., a character or person) within the system.
     * </p>
     */
    private final Long id;

    /**
     * The type of URL associated with the entity (e.g., "character", "person").
     * <p>
     * The {@link UrlType} denotes the category of the entity, such as a character or staff member,
     * which helps in generating the correct URL for accessing the entity's page.
     * </p>
     */
    private final UrlType urlType;

    /**
     * The URL of the entity's image.
     * <p>
     * A URL pointing to an image that represents the entity, which could be a portrait or profile picture.
     * </p>
     */
    private final String imageUrl;

    /**
     * The first name of the entity (if applicable).
     * <p>
     * Represents the first name of the entity, which is relevant for entities like people or characters.
     * It may be {@code null} if the entity does not have a first name.
     * </p>
     */
    private final String firstName;

    /**
     * The last name of the entity (if applicable).
     * <p>
     * Represents the last name of the entity, which is relevant for entities like people or characters.
     * It may be {@code null} if the entity does not have a last name.
     * </p>
     */
    private final String lastName;

    /**
     * The unique identifier for the media title associated with the entity.
     * <p>
     * This ID uniquely identifies the media title (e.g., an anime or manga) that the entity is associated with.
     * </p>
     */
    private final Long mediaId;

    /**
     * The title of the media associated with the entity.
     * <p>
     * Represents the name of the media (such as an anime or manga title) in which the entity appears
     * or is otherwise associated.
     * </p>
     */
    private final String mediaTitle;

    /**
     * The type of URL associated with the media (e.g., "anime", "manga").
     * <p>
     * The {@link UrlType} for the associated media, which determines how the media URL should be constructed
     * or displayed (e.g., a link to an anime or manga page).
     * </p>
     */
    private final UrlType mediaUrlType;
}
