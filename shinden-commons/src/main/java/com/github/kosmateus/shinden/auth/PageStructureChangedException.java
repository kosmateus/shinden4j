package com.github.kosmateus.shinden.auth;

import com.github.kosmateus.shinden.exception.ErrorCode;
import lombok.Getter;

/**
 * Exception thrown when the structure of a web page changes, rendering the current parser incompatible.
 * <p>
 * The {@code PageStructureChangedException} is used to signal that the expected structure of the web page
 * has changed, causing parsing errors. This exception is typically thrown when a web scraper or parser
 * encounters a page that does not conform to the anticipated format, potentially due to a redesign or
 * content changes on the target website.
 * </p>
 * <p>
 * This exception includes an {@link ErrorCode} that provides more detailed information about the specific
 * error encountered, aiding in diagnosing and handling the issue.
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
     * @param errorCode the error code indicating the specific issue related to the page structure change
     */
    public PageStructureChangedException(ErrorCode errorCode) {
        super("Page structure changed. Error code: " + errorCode.getCode());
        this.errorCode = errorCode;
    }

    /**
     * Constructs a new {@code PageStructureChangedException} with the specified error code and cause.
     * <p>
     * This constructor allows you to specify an underlying cause (such as another exception) that triggered
     * this exception, in addition to providing the specific error code. This is useful for preserving the
     * original stack trace and context of the error.
     * </p>
     *
     * @param errorCode the error code indicating the specific issue related to the page structure change
     * @param throwable the underlying cause of this exception
     */
    public PageStructureChangedException(ErrorCode errorCode, Throwable throwable) {
        super("Page structure changed. Error code: " + errorCode.getCode(), throwable);
        this.errorCode = errorCode;
    }
}
