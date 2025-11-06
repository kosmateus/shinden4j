package com.github.kosmateus.shinden.login;

import com.github.kosmateus.shinden.auth.PageStructureChangedException;
import com.github.kosmateus.shinden.auth.SessionManager;
import com.github.kosmateus.shinden.http.response.ResponseHandler;
import com.github.kosmateus.shinden.login.request.LoginRequest;
import com.github.kosmateus.shinden.login.response.LoginDetails;
import com.github.kosmateus.shinden.login.response.LoginDetails.Status;
import com.github.kosmateus.shinden.utils.PatternMatcher;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;

/**
 * Implementation of the {@link LoginApi} interface for handling user login.
 * <p>
 * The {@code LoginApiImpl} class provides the logic to authenticate a user by interacting with a login client and managing the user's session upon successful
 * authentication. It validates the login credentials, handles the response from the server, and stores session details for future use.
 * </p>
 *
 * @version 1.0.0
 */
@RequiredArgsConstructor(onConstructor = @__(@Inject))
class LoginApiImpl implements LoginApi {

    private static final PatternMatcher USER_ID_PATTERN = PatternMatcher.match(
            "_Storage\\.userId\\s*=\\s*(\\d+);", 1);
    private final LoginClient client;
    private final SessionManager sessionManager;

    @Override
    public LoginDetails login(LoginRequest loginRequest) {
        ResponseHandler<Document> loginFormResponse = client.sendLoginForm(loginRequest.toFormData());

        if (loginFormResponse.isPresent() && !loginFormResponse.getCookie("jwtCookie").isPresent()) {
            return LoginDetails.builder().status(Status.INVALID_CREDENTIALS).build();
        } else if (loginFormResponse.isPresent() && !USER_ID_PATTERN.get(loginFormResponse.getEntity().head().toString())
                .isPresent()) {
            throw new PageStructureChangedException(() -> "login.user-id");
        } else if (!loginFormResponse.isPresent()) {
            return LoginDetails.builder().status(Status.FAILURE)
                    .details(loginFormResponse.getEmptyReason().getErrorDetails()).build();
        }

        ResponseHandler<Document> loginResult = client.getMainPage(loginFormResponse.getCookies());
        if (!loginResult.isPresent()) {
            return LoginDetails.builder().status(Status.FAILURE)
                    .details(loginResult.getEmptyReason().getErrorDetails()).build();
        }

        String authToken = AuthTokenParser.parseAuthToken(loginResult.getEntity());
        storeSuccessfulLoginDetails(loginRequest, authToken, loginFormResponse);

        return LoginDetails.builder().status(Status.SUCCESS).build();
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private void storeSuccessfulLoginDetails(LoginRequest loginRequest, String authToken,
                                             ResponseHandler<Document> loginFormResponse) {
        sessionManager.setAuthToken(authToken);
        sessionManager.setCookies(loginFormResponse.getCookies());
        sessionManager.setUsername(loginRequest.getUsername());
        sessionManager.setPassword(loginRequest.getPassword());
        sessionManager.setRememberMe(loginRequest.isRememberMe());
        sessionManager.setUserId(
                Long.valueOf(USER_ID_PATTERN.get(loginFormResponse.getEntity().head().toString()).get()));
    }

}
