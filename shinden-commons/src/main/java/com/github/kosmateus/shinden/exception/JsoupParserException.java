package com.github.kosmateus.shinden.exception;

import lombok.Getter;

import java.util.function.Supplier;

/**
 * Exception thrown when an error occurs during the parsing of a web page using Jsoup.
 * <p>
 * The {@code JsoupParserException} indicates that the parsing process has failed, likely due to
 * a change in the web page structure or an issue with the API. This exception includes an
 * {@link ErrorCode} to provide additional details about the specific error encountered.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
public class JsoupParserException extends RuntimeException {

    private final ErrorCode errorCode;

    /**
     * Constructs a new {@code JsoupParserException} with the specified error code.
     *
     * @param errorCode the error code indicating the specific issue encountered during parsing.
     */
    public JsoupParserException(ErrorCode errorCode) {
        super("Invalid parsing of web page. Most probably API needs to be updated. Contact us to fix the problem.");
        this.errorCode = errorCode;
    }

    /**
     * Creates a supplier for a new {@code JsoupParserException} with the specified error code.
     *
     * @param errorCode the error code for the exception.
     * @return a supplier that returns a new {@code JsoupParserException}.
     */
    public static Supplier<JsoupParserException> of(ErrorCode errorCode) {
        return () -> new JsoupParserException(errorCode);
    }
}
