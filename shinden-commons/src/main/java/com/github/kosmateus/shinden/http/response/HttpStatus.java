package com.github.kosmateus.shinden.http.response;

import lombok.Getter;

import java.util.Optional;

/**
 * Enumeration of HTTP status codes.
 * <p>
 * The {@code HttpStatus} enum defines a comprehensive list of HTTP status codes
 * and their associated series. It provides utility methods to resolve status codes
 * and check the type of the status code (e.g., informational, successful, redirection, client error, or server error).
 * </p>
 *
 * @version 1.0.0
 */
public enum HttpStatus {

    // 1xx Informational
    CONTINUE(100, Series.INFORMATIONAL, "Continue"),
    SWITCHING_PROTOCOLS(101, Series.INFORMATIONAL, "Switching Protocols"),
    PROCESSING(102, Series.INFORMATIONAL, "Processing"),
    CHECKPOINT(103, Series.INFORMATIONAL, "Checkpoint"),

    // 2xx Success
    OK(200, Series.SUCCESSFUL, "OK"),
    CREATED(201, Series.SUCCESSFUL, "Created"),
    ACCEPTED(202, Series.SUCCESSFUL, "Accepted"),
    NON_AUTHORITATIVE_INFORMATION(203, Series.SUCCESSFUL, "Non-Authoritative Information"),
    NO_CONTENT(204, Series.SUCCESSFUL, "No Content"),
    RESET_CONTENT(205, Series.SUCCESSFUL, "Reset Content"),
    PARTIAL_CONTENT(206, Series.SUCCESSFUL, "Partial Content"),
    MULTI_STATUS(207, Series.SUCCESSFUL, "Multi-Status"),
    ALREADY_REPORTED(208, Series.SUCCESSFUL, "Already Reported"),
    IM_USED(226, Series.SUCCESSFUL, "IM Used"),

    // 3xx Redirection
    MULTIPLE_CHOICES(300, Series.REDIRECTION, "Multiple Choices"),
    MOVED_PERMANENTLY(301, Series.REDIRECTION, "Moved Permanently"),
    FOUND(302, Series.REDIRECTION, "Found"),
    SEE_OTHER(303, Series.REDIRECTION, "See Other"),
    NOT_MODIFIED(304, Series.REDIRECTION, "Not Modified"),
    TEMPORARY_REDIRECT(307, Series.REDIRECTION, "Temporary Redirect"),
    PERMANENT_REDIRECT(308, Series.REDIRECTION, "Permanent Redirect"),

    // 4xx Client Error
    BAD_REQUEST(400, Series.CLIENT_ERROR, "Bad Request"),
    UNAUTHORIZED(401, Series.CLIENT_ERROR, "Unauthorized"),
    PAYMENT_REQUIRED(402, Series.CLIENT_ERROR, "Payment Required"),
    FORBIDDEN(403, Series.CLIENT_ERROR, "Forbidden"),
    NOT_FOUND(404, Series.CLIENT_ERROR, "Not Found"),
    METHOD_NOT_ALLOWED(405, Series.CLIENT_ERROR, "Method Not Allowed"),
    NOT_ACCEPTABLE(406, Series.CLIENT_ERROR, "Not Acceptable"),
    PROXY_AUTHENTICATION_REQUIRED(407, Series.CLIENT_ERROR, "Proxy Authentication Required"),
    REQUEST_TIMEOUT(408, Series.CLIENT_ERROR, "Request Timeout"),
    CONFLICT(409, Series.CLIENT_ERROR, "Conflict"),
    GONE(410, Series.CLIENT_ERROR, "Gone"),
    LENGTH_REQUIRED(411, Series.CLIENT_ERROR, "Length Required"),
    PRECONDITION_FAILED(412, Series.CLIENT_ERROR, "Precondition Failed"),
    PAYLOAD_TOO_LARGE(413, Series.CLIENT_ERROR, "Payload Too Large"),
    URI_TOO_LONG(414, Series.CLIENT_ERROR, "URI Too Long"),
    UNSUPPORTED_MEDIA_TYPE(415, Series.CLIENT_ERROR, "Unsupported Media Type"),
    REQUESTED_RANGE_NOT_SATISFIABLE(416, Series.CLIENT_ERROR, "Requested range not satisfiable"),
    EXPECTATION_FAILED(417, Series.CLIENT_ERROR, "Expectation Failed"),
    I_AM_A_TEAPOT(418, Series.CLIENT_ERROR, "I'm a teapot"),
    UNPROCESSABLE_ENTITY(422, Series.CLIENT_ERROR, "Unprocessable Entity"),
    LOCKED(423, Series.CLIENT_ERROR, "Locked"),
    FAILED_DEPENDENCY(424, Series.CLIENT_ERROR, "Failed Dependency"),
    TOO_EARLY(425, Series.CLIENT_ERROR, "Too Early"),
    UPGRADE_REQUIRED(426, Series.CLIENT_ERROR, "Upgrade Required"),
    PRECONDITION_REQUIRED(428, Series.CLIENT_ERROR, "Precondition Required"),
    TOO_MANY_REQUESTS(429, Series.CLIENT_ERROR, "Too Many Requests"),
    REQUEST_HEADER_FIELDS_TOO_LARGE(431, Series.CLIENT_ERROR, "Request Header Fields Too Large"),
    UNAVAILABLE_FOR_LEGAL_REASONS(451, Series.CLIENT_ERROR, "Unavailable For Legal Reasons"),

    // 5xx Server Error
    INTERNAL_SERVER_ERROR(500, Series.SERVER_ERROR, "Internal Server Error"),
    NOT_IMPLEMENTED(501, Series.SERVER_ERROR, "Not Implemented"),
    BAD_GATEWAY(502, Series.SERVER_ERROR, "Bad Gateway"),
    SERVICE_UNAVAILABLE(503, Series.SERVER_ERROR, "Service Unavailable"),
    GATEWAY_TIMEOUT(504, Series.SERVER_ERROR, "Gateway Timeout"),
    HTTP_VERSION_NOT_SUPPORTED(505, Series.SERVER_ERROR, "HTTP Version not supported"),
    VARIANT_ALSO_NEGOTIATES(506, Series.SERVER_ERROR, "Variant Also Negotiates"),
    INSUFFICIENT_STORAGE(507, Series.SERVER_ERROR, "Insufficient Storage"),
    LOOP_DETECTED(508, Series.SERVER_ERROR, "Loop Detected"),
    BANDWIDTH_LIMIT_EXCEEDED(509, Series.SERVER_ERROR, "Bandwidth Limit Exceeded"),
    NOT_EXTENDED(510, Series.SERVER_ERROR, "Not Extended"),
    NETWORK_AUTHENTICATION_REQUIRED(511, Series.SERVER_ERROR, "Network Authentication Required");


    private static final HttpStatus[] VALUES;

    static {
        VALUES = values();
    }

    private final int value;
    private final Series series;
    @Getter
    private final String reasonPhrase;

    HttpStatus(int value, Series series, String reasonPhrase) {
        this.value = value;
        this.series = series;
        this.reasonPhrase = reasonPhrase;
    }

    /**
     * Returns the {@code HttpStatus} enum constant for the specified HTTP status code.
     *
     * @param statusCode the HTTP status code.
     * @return the corresponding {@code HttpStatus} enum constant.
     * @throws IllegalArgumentException if no matching {@code HttpStatus} is found.
     */
    public static HttpStatus valueOf(int statusCode) {
        Optional<HttpStatus> status = resolve(statusCode);
        if (!status.isPresent()) {
            throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
        }
        return status.get();
    }

    /**
     * Resolves the {@code HttpStatus} for the given HTTP status code.
     *
     * @param statusCode the HTTP status code.
     * @return an {@link Optional} containing the corresponding {@code HttpStatus}, or {@link Optional#empty()} if not found.
     */
    public static Optional<HttpStatus> resolve(int statusCode) {
        for (HttpStatus status : VALUES) {
            if (status.value == statusCode) {
                return Optional.of(status);
            }
        }
        return Optional.empty();
    }

    /**
     * Returns the integer value of this HTTP status code.
     *
     * @return the HTTP status code as an integer.
     */
    public int value() {
        return this.value;
    }

    /**
     * Returns the series of this HTTP status code.
     *
     * @return the {@link Series} of this HTTP status code.
     */
    public Series series() {
        return this.series;
    }

    /**
     * Determines if the status code is informational (1xx).
     *
     * @return {@code true} if the status code is informational, {@code false} otherwise.
     */
    public boolean is1xxInformational() {
        return (series() == Series.INFORMATIONAL);
    }

    /**
     * Determines if the status code is successful (2xx).
     *
     * @return {@code true} if the status code is successful, {@code false} otherwise.
     */
    public boolean is2xxSuccessful() {
        return (series() == Series.SUCCESSFUL);
    }

    /**
     * Determines if the status code is a redirection (3xx).
     *
     * @return {@code true} if the status code is a redirection, {@code false} otherwise.
     */
    public boolean is3xxRedirection() {
        return (series() == Series.REDIRECTION);
    }

    /**
     * Determines if the status code is a client error (4xx).
     *
     * @return {@code true} if the status code is a client error, {@code false} otherwise.
     */
    public boolean is4xxClientError() {
        return (series() == Series.CLIENT_ERROR);
    }

    /**
     * Determines if the status code is a server error (5xx).
     *
     * @return {@code true} if the status code is a server error, {@code false} otherwise.
     */
    public boolean is5xxServerError() {
        return (series() == Series.SERVER_ERROR);
    }

    /**
     * Determines if the status code represents an error (either client or server error).
     *
     * @return {@code true} if the status code is an error, {@code false} otherwise.
     */
    public boolean isError() {
        return (is4xxClientError() || is5xxServerError());
    }

    @Override
    public String toString() {
        return this.value + " " + name();
    }


    /**
     * Enumeration of HTTP status code series.
     * <p>
     * The {@code Series} enum categorizes HTTP status codes into their respective series,
     * such as informational (1xx), successful (2xx), redirection (3xx), client error (4xx),
     * and server error (5xx).
     * </p>
     */
    public enum Series {

        INFORMATIONAL(1),
        SUCCESSFUL(2),
        REDIRECTION(3),
        CLIENT_ERROR(4),
        SERVER_ERROR(5);

        private final int value;

        Series(int value) {
            this.value = value;
        }

        /**
         * Returns the {@code Series} enum constant for the specified HTTP status code.
         *
         * @param statusCode the HTTP status code.
         * @return the corresponding {@code Series} enum constant.
         * @throws IllegalArgumentException if no matching {@code Series} is found.
         */
        public static Series valueOf(int statusCode) {
            Optional<Series> series = resolve(statusCode);
            if (!series.isPresent()) {
                throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
            }
            return series.get();
        }

        /**
         * Resolves the {@code Series} for the given HTTP status code.
         *
         * @param statusCode the HTTP status code.
         * @return an {@link Optional} containing the corresponding {@code Series}, or {@link Optional#empty()} if not found.
         */
        public static Optional<Series> resolve(int statusCode) {
            int seriesCode = statusCode / 100;
            for (Series series : values()) {
                if (series.value == seriesCode) {
                    return Optional.of(series);
                }
            }
            return Optional.empty();
        }

        /**
         * Returns the integer value of this series.
         *
         * @return the series value as an integer.
         */
        public int value() {
            return this.value;
        }
    }
}
