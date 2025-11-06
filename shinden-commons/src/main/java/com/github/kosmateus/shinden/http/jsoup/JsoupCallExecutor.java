package com.github.kosmateus.shinden.http.jsoup;

import com.github.kosmateus.shinden.auth.SessionManager;
import com.github.kosmateus.shinden.http.response.EmptyReason;
import com.github.kosmateus.shinden.http.response.ErrorDetails;
import com.github.kosmateus.shinden.http.response.HttpStatus;
import com.github.kosmateus.shinden.http.response.HttpStatusExceptionRetrieval;
import com.github.kosmateus.shinden.http.response.ResponseHandler;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Executor for making authenticated HTTP calls using Jsoup.
 * <p>
 * The {@code JsoupCallExecutor} is responsible for executing HTTP requests
 * with the help of the Jsoup library, managing the session cookies for authentication,
 * and handling the response. If the request fails, it handles the exception and provides
 * a detailed error response.
 * </p>
 *
 * @version 1.0.0
 */
@RequiredArgsConstructor(onConstructor = @__(@Inject))
class JsoupCallExecutor {

    private final SessionManager sessionManager;

    /**
     * Executes the provided connection with authentication and handles the response.
     *
     * @param connectionSupplier a supplier that provides a Jsoup {@link Connection} to be executed.
     * @return a {@link ResponseHandler} containing the parsed {@link Document}, HTTP status, headers, and cookies.
     */
    protected ResponseHandler<Document> executeConnection(Supplier<Connection> connectionSupplier) {
        try {
            Response response = authenticatedConnection(connectionSupplier.get()).execute();
            return ResponseHandler.of(response.parse(), response.statusCode(), response.headers(), response.cookies());
        } catch (IOException e) {
            int httpStatus = HttpStatusExceptionRetrieval.getHttpStatus(e).orElse(HttpStatus.BAD_REQUEST).value();
            return ResponseHandler.empty(httpStatus, new HashMap<>(),
                    EmptyReason.fromHttpStatus(httpStatus,
                            ErrorDetails.builder()
                                    .errorName("Invalid response from server")
                                    .code("JSOUP_EX")
                                    .message(e.getMessage())
                                    .build()
                    )
            );
        }
    }

    /**
     * Adds authentication cookies to the Jsoup connection.
     *
     * @param connection the Jsoup {@link Connection} to which the cookies should be added.
     * @return the authenticated {@link Connection}.
     */
    private Connection authenticatedConnection(Connection connection) {
        Map<String, String> cookies = sessionManager.getCookies();
        if (cookies != null && !cookies.isEmpty()) {
            connection.cookies(cookies);
        }
        return connection;
    }
}
