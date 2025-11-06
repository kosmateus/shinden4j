package com.github.kosmateus.shinden.user.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.kosmateus.shinden.user.common.UserId;
import com.github.kosmateus.shinden.user.common.enums.UserGender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Represents a request to update a user's information.
 * <p>
 * The {@code UserInformationRequest} class encapsulates the data required to update a user's
 * profile information, including fields such as signature, about me, gender, birth date, and email.
 * It also includes an option to accept null values for fields that are not provided.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@Builder
@ToString
@AllArgsConstructor(onConstructor_ = @JsonCreator)
public class UserInformationRequest implements UserId {

    /**
     * The ID of the user whose information is being updated.
     * <p>
     * This field is mandatory and must be provided as a non-null {@link Long} value.
     * </p>
     */
    @NotNull
    private final Long userId;

    /**
     * The signature of the user.
     * <p>
     * This field holds the user's signature, which is typically displayed on their profile or posts.
     * </p>
     */
    private final String signature;

    /**
     * Information about the user.
     * <p>
     * This field contains a brief description or biography of the user, often shown on the user's profile.
     * </p>
     */
    private final String aboutMe;

    /**
     * The gender of the user.
     * <p>
     * This field represents the user's gender, as defined by the {@link UserGender} enum.
     * </p>
     */
    private final UserGender gender;

    /**
     * The birthdate of the user.
     * <p>
     * This field stores the user's birth date, which can be used to display age or verify age-related content.
     * </p>
     */
    private final LocalDate birthDate;

    /**
     * The email address of the user.
     * <p>
     * This field holds the user's email address, which is used for account management and communication.
     * </p>
     */
    private final String email;

    /**
     * Flag indicating whether to accept null values for fields that are not provided.
     * <p>
     * If this flag is set to {@code true}, the system will accept and process the request even if some fields
     * are null. If set to {@code false}, null values will not be allowed, and the request may be rejected if
     * any mandatory fields are missing.
     * </p>
     */
    private final boolean acceptNullFields;

    /**
     * Creates a new builder for {@code UserInformationRequest} with the specified user ID.
     * <p>
     * This method initializes a builder with the provided user ID, allowing for the convenient creation
     * of a {@code UserInformationRequest} instance with this ID as a starting point.
     * </p>
     *
     * @param userId the ID of the user whose information is being updated
     * @return a new builder for {@code UserInformationRequest}
     */
    public static UserInformationRequest.UserInformationRequestBuilder userId(Long userId) {
        return new UserInformationRequest.UserInformationRequestBuilder().userId(userId);
    }
}
