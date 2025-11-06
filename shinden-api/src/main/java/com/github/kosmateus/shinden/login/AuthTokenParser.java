package com.github.kosmateus.shinden.login;

import com.github.kosmateus.shinden.exception.JsoupParserException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jsoup.nodes.Document;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for parsing authentication tokens from an HTML document.
 * <p>
 * The {@code AuthTokenParser} class provides a method to extract an authentication token
 * from the HTML content of a login response. It uses a regular expression pattern to
 * locate the token within the document.
 * </p>
 *
 * @version 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class AuthTokenParser {

    private static final Pattern TOKEN_PATTERN = Pattern.compile("_Storage\\.basic.*=.*'(.*?)'");

    /**
     * Parses the authentication token from the given HTML document.
     * <p>
     * This method searches the HTML content of the provided {@link Document} for
     * an authentication token using a predefined regular expression pattern.
     * If the token is found, it is returned as a string. If not, a {@link JsoupParserException}
     * is thrown indicating that the token could not be found.
     * </p>
     *
     * @param document the {@link Document} containing the HTML content to parse.
     * @return the extracted authentication token as a {@link String}.
     * @throws JsoupParserException if the authentication token is not found in the document.
     */
    static String parseAuthToken(Document document) {
        String htmlDocument = document.html();
        Matcher matcher = TOKEN_PATTERN.matcher(htmlDocument);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new JsoupParserException(LoginErrorCodes.NO_AUTH_TOKEN_IN_LOGIN_RESPONSE);
    }
}
