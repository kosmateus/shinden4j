package com.github.kosmateus.shinden.login;

import com.github.kosmateus.shinden.http.jsoup.JsoupClient;
import com.github.kosmateus.shinden.http.request.HttpRequest;
import com.github.kosmateus.shinden.http.response.ResponseHandler;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;

import java.util.Map;

import static com.github.kosmateus.shinden.constants.ShindenClientHeaders.LOGIN_HEADERS;
import static com.github.kosmateus.shinden.constants.ShindenConstants.SHINDEN_URL;

/**
 * Handles communication with the Shinden login system.
 * <p>
 * The {@code LoginClient} class is responsible for sending login requests and retrieving
 * the main page after a successful login. It interacts with the Shinden website through
 * HTTP requests, utilizing the {@link JsoupClient} to perform these operations.
 * </p>
 *
 * @version 1.0.0
 */
@RequiredArgsConstructor(onConstructor = @__(@Inject))
class LoginClient {

    private final JsoupClient client;

    /**
     * Sends the login form data to the Shinden login endpoint.
     * <p>
     * This method constructs an HTTP POST request with the provided form data and
     * sends it to the Shinden login URL. The response is returned as a {@link ResponseHandler}
     * containing a {@link Document} that represents the resulting HTML page.
     * </p>
     *
     * @param formData a map containing the form data to be sent in the login request.
     * @return a {@link ResponseHandler} containing the response document.
     */
    ResponseHandler<Document> sendLoginForm(Map<String, String> formData) {
        return client.post(
                HttpRequest.builder()
                        .target(SHINDEN_URL)
                        .path("/main/0/login")
                        .headers(LOGIN_HEADERS)
                        .build(),
                formData
        );
    }

    /**
     * Retrieves the main page of Shinden after a successful login.
     * <p>
     * This method constructs an HTTP GET request using the cookies obtained from
     * the login response. The request is sent to the Shinden main page URL, and
     * the resulting HTML document is returned as a {@link ResponseHandler}.
     * </p>
     *
     * @param cookies a map containing the cookies from the login response.
     * @return a {@link ResponseHandler} containing the response document.
     */
    ResponseHandler<Document> getMainPage(Map<String, String> cookies) {
        return client.get(
                HttpRequest.builder()
                        .target(SHINDEN_URL)
                        .headers(LOGIN_HEADERS)
                        .cookies(cookies)
                        .build()
        );
    }
}
