package com.github.kosmateus.shinden.http.jsoup;

import com.github.kosmateus.shinden.http.request.HttpRequest;
import com.github.kosmateus.shinden.http.response.ResponseHandler;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.jsoup.Connection.KeyVal;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.jsoup.Jsoup.connect;

/**
 * HTTP client for making requests using Jsoup.
 * <p>
 * The {@code JsoupClient} provides methods to send HTTP POST and GET requests
 * using the Jsoup library. It utilizes the {@link JsoupCallExecutor} to handle
 * the execution of requests and manage the session cookies, headers, and response parsing.
 * </p>
 *
 * @version 1.0.0
 */
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public final class JsoupClient {

    private final JsoupCallExecutor executor;

    /**
     * Sends an HTTP POST request to the specified URL with the provided data.
     *
     * @param httpRequest the HTTP request containing the URL and headers.
     * @param data        the data to be sent in the POST request as form parameters.
     * @return a {@link ResponseHandler} containing the parsed {@link Document} and response details.
     */
    public ResponseHandler<Document> post(HttpRequest httpRequest, Map<String, String> data) {
        return executor.executeConnection(() -> connect(httpRequest.getURL())
                .headers(httpRequest.getHeaders() != null ? httpRequest.getHeaders() : new HashMap<>())
                .data(data)
                .method(Method.POST)
        );
    }

    /**
     * Sends an HTTP POST request to the specified URL with the provided data.
     *
     * @param httpRequest the HTTP request containing the URL and headers.
     * @param data        the data to be sent in the POST request as form parameters.
     * @return a {@link ResponseHandler} containing the parsed {@link Document} and response details.
     */
    public ResponseHandler<Document> post(HttpRequest httpRequest, List<KeyVal> data) {
        return executor.executeConnection(() -> connect(httpRequest.getURL())
                .headers(httpRequest.getHeaders() != null ? httpRequest.getHeaders() : new HashMap<>())
                .data(data)
                .method(Method.POST)
        );
    }

    /**
     * Sends an HTTP GET request to the specified URL.
     *
     * @param httpRequest the HTTP request containing the URL and headers.
     * @return a {@link ResponseHandler} containing the parsed {@link Document} and response details.
     */
    public ResponseHandler<Document> get(HttpRequest httpRequest) {
        return executor.executeConnection(() -> connect(httpRequest.getURL())
                .headers(httpRequest.getHeaders() != null ? httpRequest.getHeaders() : new HashMap<>())
                .method(Method.GET)
        );
    }
}
