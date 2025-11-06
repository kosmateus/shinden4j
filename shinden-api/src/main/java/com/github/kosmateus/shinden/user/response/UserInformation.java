package com.github.kosmateus.shinden.user.response;

import com.github.kosmateus.shinden.user.common.enums.UserGender;
import lombok.Builder;
import lombok.Getter;

/**
 * Represents the personal information of a user.
 * <p>
 * The {@code UserInformation} class encapsulates details about a user's personal information,
 * including their signature, biography, gender, birth date, and email address. This class is typically
 * used to display or manage the user's profile information.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@Builder
public class UserInformation {

    /**
     * The signature of the user, typically displayed alongside posts or comments.
     */
    private final String signature;

    /**
     * A brief biography or "about me" section for the user.
     */
    private final String aboutMe;

    /**
     * The gender of the user.
     */
    private final UserGender gender;

    /**
     * The year of birth of the user.
     */
    private final Integer birthYear;

    /**
     * The month of birth of the user.
     */
    private final Integer birthMonth;

    /**
     * The day of birth of the user.
     */
    private final Integer birthDay;

    /**
     * The email address of the user.
     */
    private final String email;
}
