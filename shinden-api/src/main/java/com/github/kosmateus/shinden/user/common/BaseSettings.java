package com.github.kosmateus.shinden.user.common;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Base class for user settings.
 *
 * <p>The {@code BaseSettings} class serves as a common parent class for various types of user settings,
 * encapsulating general settings such as page settings and read time settings.</p>
 *
 * @version 1.0.0
 */
@Getter
@ToString
@SuperBuilder
public class BaseSettings {

    /**
     * The settings related to page configuration.
     */
    private final PageSettings pageSettings;

    /**
     * The settings related to read time configuration.
     */
    private final ReadTimeSettings readTimeSettings;
}
