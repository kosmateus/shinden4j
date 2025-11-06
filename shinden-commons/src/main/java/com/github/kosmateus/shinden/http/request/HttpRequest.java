package com.github.kosmateus.shinden.http.request;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents an HTTP request with various configurable parameters.
 * <p>
 * The {@code HttpRequest} class provides a way to build and manage the details of an HTTP request,
 * including the target URL, path, query parameters, headers, cookies, path parameters, form fields,
 * a request body, and file resources. It also includes functionality to construct the full URL
 * from these components.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@Builder
@EqualsAndHashCode
public final class HttpRequest {

    /**
     * The base target URL for the HTTP request.
     * <p>
     * This is the root URL to which the path and query parameters will be appended to form the full request URL.
     * </p>
     */
    private final String target;

    /**
     * The specific path of the request, relative to the target URL.
     * <p>
     * This path is appended to the target URL to create the full URL for the HTTP request.
     * </p>
     */
    private final String path;


    /**
     * A list of query parameters to be included in the request.
     * <p>
     * These parameters are appended to the URL in the standard query string format, such as `key=value`.
     * </p>
     */
    private final List<KeyValue> queryParams;

    /**
     * The media type of the request body.
     * <p>
     * This field indicates the type of data being sent in the request body, such as `application/json` or `multipart/form-data`.
     * </p>
     */
    private final MediaType mediaType;

    /**
     * A map of HTTP headers to be included in the request.
     * <p>
     * These headers can include things like authentication tokens, content types, and other metadata required by the server.
     * </p>
     */
    private final Map<String, String> headers;

    /**
     * A map of cookies to be included in the request.
     * <p>
     * Cookies are often used for session management and authentication in web applications.
     * </p>
     */
    private final Map<String, String> cookies;

    /**
     * A map of path parameters to be replaced in the request path.
     * <p>
     * These parameters are placeholders in the path that will be replaced with actual values when the full URL is constructed.
     * </p>
     */
    private final Map<String, String> pathParams;

    /**
     * A map of form fields to be included in the request body.
     * <p>
     * These fields are typically used in `application/x-www-form-urlencoded` or `multipart/form-data` requests.
     * </p>
     */
    private final Map<String, String> formFields;

    /**
     * The raw body content of the request, typically used for `application/json` requests.
     * <p>
     * This field allows sending a JSON or other structured payload as the body of the request.
     * </p>
     */
    private final String body;

    /**
     * A map of file resources to be included in the request, typically used in multipart form-data requests.
     * <p>
     * The key represents the form field name, and the value is the file resource to be uploaded.
     * </p>
     */
    private final Map<String, FileResource> fileResources;

    /**
     * Constructs the full URL for the HTTP request by combining the target, path,
     * path parameters, and query parameters.
     * <p>
     * This method builds the complete URL by appending the path and query parameters
     * to the base target URL. If any path parameters are specified, they are replaced
     * in the URL accordingly. If any query parameters are specified, they are appended
     * to the URL in the standard query string format.
     * </p>
     *
     * @return the full URL as a {@link String}
     * @throws IllegalArgumentException if any required path parameters are missing or not provided
     */
    public String getURL() {
        String path = getTarget() + (StringUtils.isNotBlank(getPath()) ? getPath() : "");
        if (getPathParams() != null && !getPathParams().isEmpty()) {
            Map<String, String> filteredPathParams = getPathParams().entrySet().stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            for (Map.Entry<String, String> entry : filteredPathParams.entrySet()) {
                path = path.replace("{" + entry.getKey() + "}", entry.getValue());
            }
        }
        if (path.contains("{")) {
            throw new IllegalArgumentException("Missing path parameter value for path: " + path);
        }
        if (getQueryParams() != null && !getQueryParams().isEmpty()) {
            path += "?" + createQueryParams();
        }

        return path.replaceAll(" ", "%20");
    }

    /**
     * Creates the query parameters string for the URL based on the provided map.
     * <p>
     * This method generates a properly formatted query string by iterating over
     * the {@code queryParams} map and concatenating each key-value pair in the standard
     * `key=value` format, separated by an ampersand (&).
     * </p>
     *
     * @return the query parameters as a {@link String}
     */
    private String createQueryParams() {
        return queryParams
                .stream()
                .filter(entry -> entry != null && StringUtils.isNotBlank(entry.getKey()) && StringUtils.isNotBlank(entry.getValue()))
                .collect(
                        StringBuilder::new,
                        (stringBuilder, entry) -> {
                            if (stringBuilder.length() > 0) {
                                stringBuilder.append("&");
                            }
                            stringBuilder.append(entry.getKey());
                            stringBuilder.append("=");
                            stringBuilder.append(entry.getValue());
                        },
                        StringBuilder::append)
                .toString();
    }

    /**
     * Represents a key-value pair used in HTTP requests.
     * <p>
     * This class is typically used to represent query parameters, headers, cookies,
     * form fields, and other key-value pairs in an HTTP request.
     * </p>
     */
    @Getter
    @RequiredArgsConstructor(staticName = "of")
    public static class KeyValue {
        /**
         * The key or name of the parameter.
         */
        private final String key;

        /**
         * The value associated with the key.
         */
        private final String value;
    }

}
