package com.github.kosmateus.shinden.http.request;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Represents an HTTP request with various configurable parameters.
 * <p>
 * The {@code HttpRequest} class provides a way to build and manage the details of an HTTP request,
 * including the target URL, path, query parameters, headers, cookies, and path parameters.
 * It also includes functionality to construct the full URL from these components.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@Builder
@EqualsAndHashCode
public final class HttpRequest {

    private final String target;
    private final String path;
    private final Map<String, String> queryParams;
    private final MediaType mediaType;
    private final Map<String, String> headers;
    private final Map<String, String> cookies;
    private final Map<String, String> pathParams;

    /**
     * Constructs the full URL for the HTTP request by combining the target, path,
     * path parameters, and query parameters.
     *
     * @return the full URL as a {@link String}.
     */
    public String getURL() {
        String path = getTarget() + (StringUtils.isNotBlank(getPath()) ? getPath() : "");
        if (getPathParams() != null && !getPathParams().isEmpty()) {
            for (Map.Entry<String, String> entry : getPathParams().entrySet()) {
                path = StringUtils.replace(path, "{" + entry.getKey() + "}", entry.getValue());
            }
        }

        if (getQueryParams() != null && !getQueryParams().isEmpty()) {
            path += "?" + createQueryParams();
        }

        return path;
    }

    /**
     * Creates the query parameters string for the URL based on the provided map.
     *
     * @return the query parameters as a {@link String}.
     */
    private String createQueryParams() {
        return queryParams.entrySet()
                .stream()
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
}
