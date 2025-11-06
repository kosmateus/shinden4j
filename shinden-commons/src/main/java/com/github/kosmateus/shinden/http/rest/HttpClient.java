package com.github.kosmateus.shinden.http.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.kosmateus.shinden.http.request.HttpRequest;
import com.github.kosmateus.shinden.http.response.ResponseHandler;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

/**
 * HTTP client for making API calls.
 * <p>
 * The {@code HttpClient} class provides methods to perform HTTP GET, POST, and PUT requests
 * to RESTful APIs. It leverages the {@link HttpRestClientExecutor} to handle the execution
 * of these requests and process the responses. This class supports the inclusion of various
 * data types in requests, such as JSON bodies, form fields, and file uploads.
 * </p>
 *
 * @version 1.0.0
 */
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public final class HttpClient {

    private final HttpRestClientExecutor executor;

    /**
     * Executes an HTTP GET request.
     * <p>
     * Sends an HTTP GET request to the specified target URL to retrieve resources from the server.
     * The method returns a {@link ResponseHandler} that contains the data of the specified return type.
     * </p>
     *
     * @param <T>         the type of the entity expected in the response
     * @param httpRequest the {@link HttpRequest} containing the details of the request, such as the target URL and headers
     * @param returnType  the {@link Class} of the expected return type. This is used to deserialize the response body
     *                    into an instance of the specified type
     * @return a {@link ResponseHandler} containing the response data
     */
    public <T> ResponseHandler<T> get(HttpRequest httpRequest, Class<T> returnType) {
        return executor.executeRequest(() -> Pair.of(executor.createHttpRequest(httpRequest, HttpGet.METHOD_NAME), httpRequest), returnType);
    }

    /**
     * Executes an HTTP GET request with a custom type reference.
     * <p>
     * This method is similar to {@link #get(HttpRequest, Class)}, but it allows for the specification
     * of a more complex return type using {@link TypeReference}. This is useful for deserializing
     * responses into generic types, collections, or other complex data structures.
     * </p>
     *
     * @param <T>         the type of the entity expected in the response
     * @param httpRequest the {@link HttpRequest} containing the details of the request, such as the target URL and headers
     * @param returnType  a {@link TypeReference} representing the complex type to which the response should be deserialized
     * @return a {@link ResponseHandler} containing the response data
     */
    public <T> ResponseHandler<T> get(HttpRequest httpRequest, TypeReference<T> returnType) {
        return executor.executeRequest(() -> Pair.of(executor.createHttpRequest(httpRequest, HttpGet.METHOD_NAME), httpRequest), returnType);
    }

    /**
     * Executes an HTTP POST request with a combination of JSON body, form fields, and multiple file uploads.
     * <p>
     * Sends an HTTP POST request to the specified target URL, allowing the inclusion of a JSON body,
     * form fields, and multiple files as part of a multipart form-data request. The method will prioritize
     * sending the JSON body if it is provided and will handle any form fields and files specified in the request.
     * </p>
     *
     * @param <T>         the type of the entity expected in the response
     * @param httpRequest the {@link HttpRequest} containing the details of the request, such as the target URL,
     *                    headers, form fields, JSON body, and multiple file resources
     * @param returnType  the {@link Class} of the expected return type. This is used to deserialize the response body
     *                    into an instance of the specified type
     * @return a {@link ResponseHandler} containing the response data
     */
    public <T> ResponseHandler<T> post(HttpRequest httpRequest, Class<T> returnType) {
        return executor.executeRequest(() -> Pair.of(executor.createHttpRequest(httpRequest, HttpPost.METHOD_NAME), httpRequest), returnType);
    }

    /**
     * Executes an HTTP POST request with a custom type reference.
     * <p>
     * This method sends an HTTP POST request to the specified target URL, allowing for the inclusion
     * of various payload types such as JSON bodies, form fields, and multiple files in a multipart form-data request.
     * It leverages a {@link TypeReference} to specify the complex return type, making it suitable for deserializing
     * the response into generic types, collections, or other complex data structures.
     * </p>
     *
     * @param <T>         the type of the entity expected in the response
     * @param httpRequest the {@link HttpRequest} containing the details of the request, such as the target URL,
     *                    headers, form fields, JSON body, and multiple file resources
     * @param returnType  a {@link TypeReference} representing the complex type to which the response should be deserialized
     * @return a {@link ResponseHandler} containing the response data
     */
    public <T> ResponseHandler<T> post(HttpRequest httpRequest, TypeReference<T> returnType) {
        return executor.executeRequest(() -> Pair.of(executor.createHttpRequest(httpRequest, HttpPost.METHOD_NAME), httpRequest), returnType);
    }

    /**
     * Executes an HTTP PUT request with a JSON body.
     * <p>
     * Sends an HTTP PUT request to the specified target URL, allowing the inclusion of a JSON body
     * to update existing resources on the server. The method returns a {@link ResponseHandler} containing
     * the data of the specified return type.
     * </p>
     *
     * @param <T>         the type of the entity expected in the response
     * @param httpRequest the {@link HttpRequest} containing the details of the request, such as the target URL and headers
     * @param returnType  the {@link Class} of the expected return type. This is used to deserialize the response body
     *                    into an instance of the specified type
     * @return a {@link ResponseHandler} containing the response data
     */
    public <T> ResponseHandler<T> put(HttpRequest httpRequest, Class<T> returnType) {
        return executor.executeRequest(() -> Pair.of(executor.createHttpRequest(httpRequest, HttpPut.METHOD_NAME), httpRequest), returnType);
    }

    /**
     * Executes an HTTP PUT request with a custom type reference.
     * <p>
     * This method sends an HTTP PUT request to the specified target URL, allowing for the inclusion
     * of a JSON body to update existing resources on the server. It utilizes a {@link TypeReference}
     * to specify a complex return type, which is useful for deserializing the response into generic types,
     * collections, or other complex data structures.
     * </p>
     *
     * @param <T>         the type of the entity expected in the response
     * @param httpRequest the {@link HttpRequest} containing the details of the request, such as the target URL and headers
     * @param returnType  a {@link TypeReference} representing the complex type to which the response should be deserialized
     * @return a {@link ResponseHandler} containing the response data
     */
    public <T> ResponseHandler<T> put(HttpRequest httpRequest, TypeReference<T> returnType) {
        return executor.executeRequest(() -> Pair.of(executor.createHttpRequest(httpRequest, HttpPut.METHOD_NAME), httpRequest), returnType);
    }
}
