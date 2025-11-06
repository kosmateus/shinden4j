package com.github.kosmateus.shinden.user.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Represents a comment made by a user.
 * <p>
 * The {@code Comment} class encapsulates the details of a comment, including the content of the comment,
 * information about the user who made the comment, and the timestamp when the comment was created.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@Builder
@ToString
public class Comment {

    /**
     * The unique identifier for the comment.
     */
    private final Long id;

    /**
     * The content of the comment.
     */
    private final String content;

    /**
     * The username of the user who made the comment.
     */
    private final String username;

    /**
     * The unique identifier of the user who made the comment.
     */
    private final Long userId;

    /**
     * The role of the user within the community (e.g., Admin, Moderator, Member).
     */
    private final String userRole;

    /**
     * The URL or path to the user's avatar image.
     */
    private final String userAvatar;

    /**
     * The signature of the user, typically displayed alongside comments.
     */
    private final String userSignature;

    /**
     * The timestamp when the comment was created.
     */
    private final LocalDateTime createdAt;
}
