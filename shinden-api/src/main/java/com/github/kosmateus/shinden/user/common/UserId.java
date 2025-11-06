package com.github.kosmateus.shinden.user.common;

/**
 * Interface representing an entity that has a user ID.
 * <p>
 * The {@code UserId} interface provides a method to retrieve the unique identifier
 * of a user. This interface can be implemented by any class that needs to associate
 * a user ID with an entity.
 * </p>
 *
 * @version 1.0.0
 */
public interface UserId {

    /**
     * Returns the unique identifier of the user.
     *
     * @return the user's ID as a {@link Long}.
     */
    Long getUserId();
}
