package com.github.kosmateus.shinden.http.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

/**
 * Handles the response of an HTTP request, encapsulating the entity, status, headers, and cookies.
 * <p>
 * The {@code ResponseHandler} class provides a standardized way to manage and inspect the results
 * of an HTTP request. It includes utility methods to determine the status of the response,
 * access headers, cookies, and the entity itself, as well as handle cases where the entity is absent.
 * </p>
 *
 * @param <T> the type of the entity contained in the response.
 * @version 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResponseHandler<T> {

    private T entity;
    @Getter
    private Map<String, String> httpHeaders;
    @Getter
    private Map<String, String> cookies;
    private int httpStatus;
    @Getter
    private EmptyReason emptyReason;

    /**
     * Constructs a {@code ResponseHandler} with the provided entity, headers, and status code.
     *
     * @param entity      the entity contained in the response.
     * @param httpHeaders the HTTP headers from the response.
     * @param httpStatus  the HTTP status code.
     */
    private ResponseHandler(T entity, Map<String, String> httpHeaders, int httpStatus) {
        this.entity = entity;
        this.httpHeaders = httpHeaders;
        this.httpStatus = httpStatus;
        this.cookies = Collections.emptyMap();
    }

    /**
     * Constructs a {@code ResponseHandler} with the provided entity, headers, cookies, and status code.
     *
     * @param entity      the entity contained in the response.
     * @param httpHeaders the HTTP headers from the response.
     * @param cookies     the cookies from the response.
     * @param httpStatus  the HTTP status code.
     */
    private ResponseHandler(T entity, Map<String, String> httpHeaders, Map<String, String> cookies,
                            int httpStatus) {
        this.entity = entity;
        this.httpHeaders = httpHeaders;
        this.httpStatus = httpStatus;
        this.cookies = cookies;
    }

    /**
     * Constructs a {@code ResponseHandler} for an empty response, with the provided headers, status code, and reason.
     *
     * @param httpHeaders the HTTP headers from the response.
     * @param httpStatus  the HTTP status code.
     * @param emptyReason the reason why the response is empty.
     */
    public ResponseHandler(Map<String, String> httpHeaders, int httpStatus, EmptyReason emptyReason) {
        this.httpHeaders = httpHeaders;
        this.httpStatus = httpStatus;
        this.emptyReason = emptyReason;
        this.cookies = Collections.emptyMap();
    }

    /**
     * Creates a {@code ResponseHandler} for a nullable entity, handling a not found case.
     *
     * @param <T>          the type of the entity.
     * @param entity       the entity contained in the response, or {@code null}.
     * @param httpStatus   the HTTP status code.
     * @param httpHeaders  the HTTP headers from the response.
     * @param errorDetails additional error details if the entity is not found.
     * @return a {@code ResponseHandler} containing the entity if present, or an empty handler if not found.
     */
    public static <T> ResponseHandler<T> ofNullable(T entity, int httpStatus, Map<String, String> httpHeaders,
                                                    ErrorDetails errorDetails) {
        return entity != null && httpHeaders != null ? of(entity, httpStatus, httpHeaders)
                : emptyByNotFound(httpHeaders, errorDetails);
    }

    /**
     * Creates a {@code ResponseHandler} for a non-null entity.
     *
     * @param <T>         the type of the entity.
     * @param entity      the entity contained in the response.
     * @param httpStatus  the HTTP status code.
     * @param httpHeaders the HTTP headers from the response.
     * @return a {@code ResponseHandler} containing the entity.
     * @throws IllegalArgumentException if the entity is {@code null}.
     */
    public static <T> ResponseHandler<T> of(T entity, int httpStatus, Map<String, String> httpHeaders) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        return new ResponseHandler<T>(entity, httpHeaders, httpStatus);
    }

    /**
     * Creates a {@code ResponseHandler} for a non-null entity with cookies.
     *
     * @param <T>         the type of the entity.
     * @param entity      the entity contained in the response.
     * @param httpStatus  the HTTP status code.
     * @param httpHeaders the HTTP headers from the response.
     * @param cookies     the cookies from the response.
     * @return a {@code ResponseHandler} containing the entity.
     * @throws IllegalArgumentException if the entity is {@code null}.
     */
    public static <T> ResponseHandler<T> of(T entity, int httpStatus, Map<String, String> httpHeaders,
                                            Map<String, String> cookies) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        return new ResponseHandler<>(entity, httpHeaders, cookies, httpStatus);
    }

    /**
     * Creates a {@code ResponseHandler} for a not found response.
     *
     * @param <T>          the type of the entity.
     * @param httpHeaders  the HTTP headers from the response.
     * @param errorDetails additional error details.
     * @return a {@code ResponseHandler} indicating the entity was not found.
     */
    public static <T> ResponseHandler<T> emptyByNotFound(Map<String, String> httpHeaders,
                                                         ErrorDetails errorDetails) {
        return empty(HttpStatus.NOT_FOUND.value(), httpHeaders, EmptyReason.notFound(errorDetails));
    }

    /**
     * Creates a {@code ResponseHandler} for an empty response with a specific reason.
     *
     * @param <T>        the type of the entity.
     * @param httpStatus the HTTP status code.
     * @param headers    the HTTP headers from the response.
     * @param reason     the reason why the response is empty.
     * @return a {@code ResponseHandler} for an empty response.
     */
    public static <T> ResponseHandler<T> empty(int httpStatus, Map<String, String> headers,
                                               EmptyReason reason) {
        return new ResponseHandler<>(headers, httpStatus, reason);
    }

    /**
     * Creates a {@code ResponseHandler} for an empty but successful response.
     *
     * @param <T>         the type of the entity.
     * @param httpStatus  the HTTP status code.
     * @param httpHeaders the HTTP headers from the response.
     * @return a {@code ResponseHandler} indicating an empty but successful response.
     */
    public static <T> ResponseHandler<T> emptyOk(int httpStatus, Map<String, String> httpHeaders) {
        return empty(httpStatus, httpHeaders, EmptyReason.ok());
    }

    /**
     * Determines if the response is successful or represents an empty success.
     *
     * @return {@code true} if the response is successful or empty success, {@code false} otherwise.
     */
    public boolean isOk() {
        return isPresent() || EmptyReason.ok().equals(this.emptyReason);
    }

    /**
     * Determines if the response indicates a failure.
     *
     * @return {@code true} if the response is a failure, {@code false} otherwise.
     */
    public boolean isFailure() {
        return !isOk();
    }

    /**
     * Determines if the response indicates a forbidden status.
     *
     * @return {@code true} if the response is forbidden, {@code false} otherwise.
     */
    public boolean isForbidden() {
        return hasStatus(HttpStatus.FORBIDDEN);
    }

    /**
     * Determines if the response indicates a not found status.
     *
     * @return {@code true} if the response indicates not found, {@code false} otherwise.
     */
    public boolean isNotFound() {
        return hasStatus(HttpStatus.NOT_FOUND);
    }

    /**
     * Checks if the response has a specific HTTP status.
     *
     * @param httpStatus the {@link HttpStatus} to check.
     * @return {@code true} if the response has the specified status, {@code false} otherwise.
     */
    public boolean hasStatus(HttpStatus httpStatus) {
        return this.httpStatus == httpStatus.value();
    }

    public boolean hasStatus(int status) {
        return this.httpStatus == status;
    }

    /**
     * Determines if the response contains an entity.
     *
     * @return {@code true} if the entity is present, {@code false} otherwise.
     */
    public boolean isPresent() {
        return entity != null;
    }

    /**
     * Returns the entity contained in the response.
     *
     * @return the entity contained in the response.
     * @throws NoSuchElementException if the entity is not present.
     */
    public T getEntity() {
        if (isPresent()) {
            return entity;
        }
        throw new NoSuchElementException("Entity was not provided in response");
    }

    /**
     * Returns the entity if present, otherwise returns the provided default value.
     *
     * @param other the default value to return if the entity is not present.
     * @return the entity if present, or the provided default value.
     */
    public T orElse(T other) {
        return this.isPresent() ? this.entity : other;
    }

    /**
     * Returns the entity if present, otherwise applies the provided function to handle the empty reason.
     *
     * @param mapper the function to handle the empty reason.
     * @return the entity if present, or the result of applying the function to the empty reason.
     */
    public T orElseHandleEmptyReason(Function<EmptyReason, T> mapper) {
        return this.isPresent() ? this.entity : mapper.apply(this.emptyReason);
    }

    /**
     * Retrieves a specific cookie by its name.
     *
     * @param name the name of the cookie.
     * @return an {@link Optional} containing the cookie value if present, or {@link Optional#empty()} if not.
     */
    public Optional<String> getCookie(String name) {
        return Optional.ofNullable(cookies.get(name));
    }
}
