package com.github.kosmateus.shinden.http.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.kosmateus.shinden.auth.SessionManager;
import com.github.kosmateus.shinden.http.request.FileResource;
import com.github.kosmateus.shinden.http.request.HttpRequest;
import com.github.kosmateus.shinden.http.response.EmptyReason;
import com.github.kosmateus.shinden.http.response.ErrorDetails;
import com.github.kosmateus.shinden.http.response.ResponseHandler;
import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Executor for making HTTP requests using Apache HttpClient.
 * <p>
 * The {@code HttpRestClientExecutor} class is responsible for creating and executing
 * HTTP requests, including GET, POST, and PUT methods. It also manages session authentication
 * by adding cookies from the {@link SessionManager} and handles responses, including processing
 * errors and exceptions.
 * </p>
 *
 * <p>
 * The class supports various HTTP content types, including JSON, form fields, and file uploads.
 * It is designed to handle common HTTP operations in a RESTful API context and encapsulate the
 * complexities of request construction and response handling.
 * </p>
 *
 * @version 1.0.0
 */
class HttpRestClientExecutor {

    private final CloseableHttpClient client;
    private final ObjectMapper objectMapper;
    private final SessionManager sessionManager;

    @Inject
    public HttpRestClientExecutor(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.client = HttpClients.createDefault();
    }

    /**
     * Executes an HTTP request and processes the response into a specified complex type.
     * <p>
     * This method sends an HTTP request and deserializes the response body into a complex type
     * defined by a {@link TypeReference}. It is particularly useful for handling responses that
     * need to be mapped into generic or parameterized types, such as lists or maps.
     * </p>
     *
     * @param <T>           the type of the entity expected in the response
     * @param executor      a supplier providing a pair of the HTTP request and the original {@link HttpRequest}
     * @param typeReference a {@link TypeReference} representing the complex type to which the response should be deserialized
     * @return a {@link ResponseHandler} containing the response data or error details
     */
    public <T> ResponseHandler<T> executeRequest(Supplier<Pair<HttpUriRequest, HttpRequest>> executor, TypeReference<T> typeReference) {
        return executeRequestInternal(executor, response -> handleResponseResult(response, typeReference));
    }

    /**
     * Executes an HTTP request and processes the response into a specified type.
     * <p>
     * This method sends an HTTP request and deserializes the response body into the specified
     * return type. It is suitable for handling responses that map directly to a specific class.
     * </p>
     *
     * @param <T>      the type of the entity expected in the response
     * @param executor a supplier providing a pair of the HTTP request and the original {@link HttpRequest}
     * @param clazz    the {@link Class} of the expected return type. This is used to deserialize the response body
     *                 into an instance of the specified type
     * @return a {@link ResponseHandler} containing the response data or error details
     */
    <T> ResponseHandler<T> executeRequest(Supplier<Pair<HttpUriRequest, HttpRequest>> executor, Class<T> clazz) {
        return executeRequestInternal(executor, response -> handleResponseResult(response, clazz));
    }

    /**
     * Creates an HTTP request, adding necessary headers, cookies, and handling body content such as JSON,
     * form fields, and files.
     *
     * @param httpRequest the {@link HttpRequest} containing the details of the request
     * @param method      the HTTP method (e.g., GET, POST, PUT)
     * @return a {@link HttpUriRequest} with headers, cookies, and the body applied
     */
    HttpUriRequest createHttpRequest(HttpRequest httpRequest, String method) {
        HttpUriRequest request = createRequest(method, URI.create(httpRequest.getURL()));
        addAuthenticationCookies(request);

        return request;
    }

    /**
     * Executes the HTTP request and handles the response.
     * <p>
     * This internal method sends the HTTP request and uses a response handler function to process
     * the response. It supports various HTTP methods and content types, including JSON, form fields,
     * and multipart file uploads.
     * </p>
     *
     * @param <T>                     the type of the entity expected in the response
     * @param executor                a supplier providing a pair of the HTTP request and the original {@link HttpRequest}
     * @param responseHandlerFunction a function to process the response and convert it into a {@link ResponseHandler}
     * @return a {@link ResponseHandler} containing the response data or error details
     */
    private <T> ResponseHandler<T> executeRequestInternal(Supplier<Pair<HttpUriRequest, HttpRequest>> executor, ResponseHandlerFunction<T> responseHandlerFunction) {
        Pair<HttpUriRequest, HttpRequest> requestPair = executor.get();
        HttpUriRequest request = requestPair.getLeft();
        HttpRequest httpRequest = requestPair.getRight();

        if (httpRequest.getHeaders() != null && !httpRequest.getHeaders().isEmpty()) {
            httpRequest.getHeaders().forEach(request::addHeader);
        }

        if (request instanceof HttpPost || request instanceof HttpPut) {
            HttpEntityEnclosingRequestBase entityRequest = (HttpEntityEnclosingRequestBase) request;
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            try {
                if (StringUtils.isNotBlank(httpRequest.getBody())) {
                    builder.addTextBody("body", httpRequest.getBody(), ContentType.APPLICATION_JSON);
                }
                if (httpRequest.getFormFields() != null && !httpRequest.getFormFields().isEmpty()) {
                    httpRequest.getFormFields().forEach((key, value) ->
                            builder.addTextBody(key, value, ContentType.TEXT_PLAIN));
                }

                if (httpRequest.getFileResources() != null && !httpRequest.getFileResources().isEmpty()) {
                    addFileResources(builder, httpRequest.getFileResources());
                }

                HttpEntity entity = builder.build();
                entityRequest.setEntity(entity);
            } catch (IOException e) {
                return handleIOException(e);
            }
        }

        try (CloseableHttpResponse response = client.execute(request)) {
            return responseHandlerFunction.handle(response);
        } catch (IOException e) {
            return handleIOException(e);
        } catch (Exception e) {
            return handleGenericException(e);
        }
    }

    /**
     * Adds authentication cookies to the HTTP request.
     *
     * @param request the {@link HttpUriRequest} to which the cookies should be added
     */
    private void addAuthenticationCookies(HttpUriRequest request) {
        Map<String, String> cookies = sessionManager.getCookies();
        if (cookies != null && !cookies.isEmpty()) {
            String cookieHeader = cookies.entrySet()
                    .stream()
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .collect(Collectors.joining("; "));
            request.addHeader("Cookie", cookieHeader);
        }
    }

    /**
     * Creates an instance of {@link HttpUriRequest} based on the specified HTTP method and URI.
     *
     * @param method the HTTP method (e.g., GET, POST, PUT)
     * @param uri    the {@link URI} for the request
     * @return a new instance of {@link HttpUriRequest}
     * @throws IllegalArgumentException if the HTTP method is not supported
     */
    private HttpUriRequest createRequest(String method, URI uri) {
        switch (method) {
            case "GET":
                return new HttpGet(uri);
            case "POST":
                return new HttpPost(uri);
            case "PUT":
                return new HttpPut(uri);
            default:
                throw new IllegalArgumentException("Method not supported");
        }
    }

    /**
     * Adds file resources to the {@link MultipartEntityBuilder}.
     *
     * @param builder       the {@link MultipartEntityBuilder} to which the file resources will be added
     * @param fileResources the map of file resources to add
     * @throws IOException if an error occurs while reading a file resource
     */
    private void addFileResources(MultipartEntityBuilder builder, Map<String, FileResource> fileResources) throws IOException {
        for (Map.Entry<String, FileResource> entry : fileResources.entrySet()) {
            String key = entry.getKey();
            FileResource fileResource = entry.getValue();
            builder.addBinaryBody(key, fileResource.getInputStream(), ContentType.DEFAULT_BINARY, fileResource.getFileName());
        }
    }

    /**
     * Handles the response from the server, deserializing it into the specified type.
     *
     * @param <T>      the type of the entity expected in the response
     * @param response the {@link HttpResponse} received from the server
     * @param clazz    the {@link Class} of the expected return type
     * @return a {@link ResponseHandler} containing the response data
     * @throws IOException if an error occurs while processing the response
     */
    private <T> ResponseHandler<T> handleResponseResult(HttpResponse response, Class<T> clazz)
            throws IOException {
        return handleResponseResult(response, body -> objectMapper.readValue(body, clazz));
    }

    /**
     * Handles the response from the server, deserializing it into the specified type.
     *
     * @param <T>           the type of the entity expected in the response
     * @param response      the {@link HttpResponse} received from the server
     * @param typeReference the {@link TypeReference} of the expected return type
     * @return a {@link ResponseHandler} containing the response data
     * @throws IOException if an error occurs while processing the response
     */
    private <T> ResponseHandler<T> handleResponseResult(HttpResponse response, TypeReference<T> typeReference) throws IOException {
        return handleResponseResult(response, body -> objectMapper.readValue(body, typeReference));
    }

    @SuppressWarnings("unchecked")
    private <T> ResponseHandler<T> handleResponseResult(HttpResponse response, BodyConverter<T> converter) throws IOException {
        HttpEntity entity = response.getEntity();
        String body = EntityUtils.toString(entity);

        int status = response.getStatusLine().getStatusCode();
        Map<String, String> headers = Arrays.stream(response.getAllHeaders())
                .collect(Collectors.toMap(
                        Header::getName,
                        Header::getValue,
                        (existingValue, newValue) -> existingValue + ", " + newValue
                ));

        if (status < 400) {
            if (StringUtils.isNotBlank(body)) {
                if (body.startsWith("[") || body.startsWith("{")) {
                    return ResponseHandler.of(converter.convert(body), status, headers);
                }
                return ResponseHandler.of((T) body, status, headers);
            }
            return ResponseHandler.emptyOk(status, headers);
        }

        return ResponseHandler.empty(status, headers,
                EmptyReason.fromHttpStatus(status, ErrorDetails.builder().build()));
    }

    /**
     * Handles an {@link IOException} that occurs during the execution of the request.
     *
     * @param <T> the type of the entity expected in the response
     * @param e   the {@link IOException} that was thrown
     * @return a {@link ResponseHandler} containing an error response
     */
    private <T> ResponseHandler<T> handleIOException(IOException e) {
        return ResponseHandler.empty(800, null,
                EmptyReason.errorResponse(
                        ErrorDetails.builder()
                                .cause(e)
                                .message(e.getMessage())
                                .errorName("IOException")
                                .build()
                )
        );
    }

    /**
     * Handles a generic exception that occurs during the execution of the request.
     *
     * @param <T> the type of the entity expected in the response
     * @param e   the {@link Exception} that was thrown
     * @return a {@link ResponseHandler} containing an error response
     */
    private <T> ResponseHandler<T> handleGenericException(Exception e) {
        return ResponseHandler.empty(801, null, EmptyReason.errorResponse(
                ErrorDetails.builder().message(e.getMessage())
                        .errorName("Unexpected exception during REST call").build()));
    }

    @FunctionalInterface
    private interface ResponseHandlerFunction<T> {
        ResponseHandler<T> handle(HttpResponse response) throws IOException;
    }

    @FunctionalInterface
    private interface BodyConverter<T> {
        T convert(String body) throws JsonProcessingException;
    }
}
