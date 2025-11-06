package com.github.kosmateus.shinden.http.request;

/**
 * Represents a parameter that has a display value.
 * <p>
 * The {@code DisplayValue} interface is intended to be implemented by classes
 * that represent parameters with a displayable value. This display value is typically
 * used in contexts where a human-readable representation of the parameter is required,
 * such as in UI components, logs, or debugging output.
 * </p>
 *
 * @version 1.0.0
 */
public interface DisplayValue {

    /**
     * Returns the display value of the parameter.
     * <p>
     * This method should provide a human-readable representation of the parameter
     * that implements this interface. The display value can be used for display
     * purposes in UI components, logging, or other contexts where a string representation
     * is required.
     * </p>
     *
     * @return the display value as a {@link String}
     */
    String getDisplayValue();
}
