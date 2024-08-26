package com.github.kosmateus.shinden.auth;

import com.github.kosmateus.shinden.exception.ErrorCode;
import lombok.Getter;

/**
 * Exception thrown when the structure of a web page changes, rendering the current parser incompatible.
 * <p>
 * The {@code PageStructureChangedException} is used to signal that the expected structure of the web page
 * has changed, causing parsing errors. It includes an {@link ErrorCode} that provides more detailed
 * information about the specific error encountered.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
public class PageStructureChangedException extends RuntimeException {

    private final ErrorCode errorCode;

    /**
     * Constructs a new {@code PageStructureChangedException} with the specified error code.
     *
     * @param errorCode the error code indicating the specific issue related to the page structure change.
     */
    public PageStructureChangedException(ErrorCode errorCode) {
        super("Page structure changed. Error code: " + errorCode.getCode());
        this.errorCode = errorCode;
    }
}
