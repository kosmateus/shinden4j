package com.github.kosmateus.shinden.user;

import com.github.kosmateus.shinden.exception.ForbiddenException;
import com.github.kosmateus.shinden.exception.NotFoundException;
import com.github.kosmateus.shinden.http.jsoup.JsoupClient;
import com.github.kosmateus.shinden.http.request.HttpRequest;
import com.github.kosmateus.shinden.http.response.ResponseHandler;
import com.github.kosmateus.shinden.user.request.FavouriteTagsRequest;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.jsoup.Connection.KeyVal;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.Map;

import static com.github.kosmateus.shinden.constants.ShindenConstants.SHINDEN_URL;

/**
 * Client for handling HTTP requests related to user operations on Shinden.
 * <p>
 * The {@code UserJsoupClient} class is responsible for making HTTP requests to the Shinden website
 * to perform various user-related operations such as fetching user pages, achievements,
 * favourite tags, reviews, recommendations, and updating user information.
 * </p>
 *
 * @version 1.0.0
 */
@RequiredArgsConstructor(onConstructor_ = @__(@Inject))
class UserJsoupClient {

    private final JsoupClient client;

    /**
     * Retrieves the user page for the specified user ID.
     *
     * @param userId the ID of the user
     * @return a {@link ResponseHandler} containing the response document
     * @throws NotFoundException if the user page is not found
     */
    ResponseHandler<Document> getUserPage(Long userId) {
        return client.get(
                HttpRequest.builder()
                        .target(SHINDEN_URL)
                        .path("/user/" + userId)
                        .build()
        );
    }

    /**
     * Retrieves the achievements page for the specified user ID.
     *
     * @param userId the ID of the user
     * @return a {@link ResponseHandler} containing the response document
     * @throws NotFoundException if the achievements page is not found
     */
    ResponseHandler<Document> getAchievementsPage(Long userId) {
        return client.get(
                HttpRequest.builder()
                        .target(SHINDEN_URL)
                        .path("/user/" + userId + "/achievements")
                        .build()
        );
    }

    /**
     * Retrieves the favourite tags page for the specified user.
     *
     * @param request the {@link FavouriteTagsRequest} containing user and tag details
     * @return a {@link ResponseHandler} containing the response document
     * @throws NotFoundException if the favourite tags page is not found
     */
    ResponseHandler<Document> getFavouriteTagsPage(FavouriteTagsRequest request) {
        return client.get(
                HttpRequest.builder()
                        .target(SHINDEN_URL)
                        .path("/user/" + request.getUserId() + "/favourite-tags")
                        .queryParams(request.toQueryParams())
                        .build()
        );
    }

    /**
     * Retrieves the reviews page for the specified user ID.
     *
     * @param userId the ID of the user
     * @return a {@link ResponseHandler} containing the response document
     * @throws NotFoundException if the reviews page is not found
     */
    ResponseHandler<Document> getReviewsPage(Long userId) {
        return client.get(
                HttpRequest.builder()
                        .target(SHINDEN_URL)
                        .path("/user/" + userId + "/reviews")
                        .build()
        );
    }

    /**
     * Retrieves the recommendations page for the specified user ID.
     *
     * @param userId the ID of the user
     * @return a {@link ResponseHandler} containing the response document
     * @throws NotFoundException if the recommendations page is not found
     */
    ResponseHandler<Document> getRecommendationsPage(Long userId) {
        return client.get(
                HttpRequest.builder()
                        .target(SHINDEN_URL)
                        .path("/user/" + userId + "/recommendations")
                        .build()
        );
    }

    /**
     * Retrieves the information edit page for the specified user ID.
     *
     * @param userId the ID of the user
     * @return a {@link ResponseHandler} containing the response document
     * @throws NotFoundException if the information edit page is not found
     * @throws ForbiddenException if the user is not authorized to access the information edit page
     */
    ResponseHandler<Document> getInformationEditPage(Long userId) {
        return client.get(
                HttpRequest.builder()
                        .target(SHINDEN_URL)
                        .path("/user/" + userId + "/edit")
                        .build()
        );
    }

    /**
     * Updates the user's information with the provided form data.
     *
     * @param userId   the ID of the user
     * @param formData a map containing the form data to update the user's information
     * @return a {@link ResponseHandler} containing the response document after the update
     * @throws NotFoundException if the user is not found or the update fails
     * @throws ForbiddenException if the user is not authorized to update the information
     */
    ResponseHandler<Document> updateInformation(Long userId, Map<String, String> formData) {
        return client.post(
                HttpRequest.builder()
                        .target(SHINDEN_URL)
                        .path("/user/" + userId + "/edit")
                        .build(),
                formData
        );
    }

    /**
     * Retrieves the settings page for the specified user ID.
     *
     * @param userId the ID of the user
     * @return a {@link ResponseHandler} containing the response document
     * @throws NotFoundException if the settings page is not found
     * @throws ForbiddenException if the user is not authorized to access the settings page
     */
    ResponseHandler<Document> getSettingsPage(Long userId) {
        return client.get(
                HttpRequest.builder()
                        .target(SHINDEN_URL)
                        .path("/user/" + userId + "/settings")
                        .build()
        );
    }

    /**
     * Updates the user's page settings with the provided settings.
     *
     * @param userId       the ID of the user
     * @param pageSettings a list of {@link KeyVal} representing the page settings to update
     * @return a {@link ResponseHandler} containing the response document after the update
     * @throws NotFoundException if the user is not found or the update fails
     * @throws ForbiddenException if the user is not authorized to update the page settings
     */
    ResponseHandler<Document> updatePageSettings(Long userId, List<KeyVal> pageSettings) {
        return client.post(
                HttpRequest.builder()
                        .target(SHINDEN_URL)
                        .path("/user/" + userId + "/edit-skin-and-time")
                        .build(),
                pageSettings
        );
    }

    /**
     * Updates the user's settings with the provided settings.
     *
     * @param userId   the ID of the user
     * @param settings a list of {@link KeyVal} representing the settings to update
     * @return a {@link ResponseHandler} containing the response document after the update
     * @throws NotFoundException if the user is not found or the update fails
     * @throws ForbiddenException if the user is not authorized to update the settings
     */
    ResponseHandler<Document> updateSettings(Long userId, List<KeyVal> settings) {
        return client.post(
                HttpRequest.builder()
                        .target(SHINDEN_URL)
                        .path("/user/" + userId + "/settings")
                        .build(),
                settings
        );
    }

    /**
     * Retrieves the avatar edit page for the specified user ID.
     * <p>
     * This method fetches the page where the user can edit or update their avatar. The page is typically
     * accessed when a user wants to upload a new avatar or modify their existing one.
     * </p>
     *
     * @param userId the ID of the user
     * @return a {@link ResponseHandler} containing the response document
     * @throws NotFoundException  if the avatar edit page is not found
     * @throws ForbiddenException if the user is not authorized to access the avatar edit page
     */
    ResponseHandler<Document> getEditAvatarPage(Long userId) {
        return client.get(
                HttpRequest.builder()
                        .target(SHINDEN_URL)
                        .path("/user/" + userId + "/edit_avatar")
                        .build()
        );
    }

    /**
     * Retrieves the edit password page for a specific user.
     * <p>
     * This method sends an HTTP GET request to the URL associated with the user's edit password page.
     * The page allows the user to change their password. The method returns a {@link ResponseHandler}
     * that contains the HTML document of the edit password page.
     * </p>
     *
     * @param userId the unique identifier of the user whose edit password page is being requested.
     *               Must not be null.
     * @return a {@link ResponseHandler} containing the HTML {@link Document} of the edit password page.
     * @throws IllegalArgumentException if the userId is null or invalid.
     * @throws NotFoundException        if the user or the edit password page is not found.
     * @throws ForbiddenException       if the user is not authorized to access the edit password page.
     */
    ResponseHandler<Document> getEditPasswordPage(Long userId) {
        return client.get(
                HttpRequest.builder()
                        .target(SHINDEN_URL)
                        .path("/user/" + userId + "/edit_passwd")
                        .build()
        );
    }

    /**
     * Retrieves the MyAnimeList (MAL) import page for a specific user.
     * <p>
     * This method sends an HTTP GET request to fetch the page that allows users to import their
     * MyAnimeList data into their Shinden account. The method returns a {@link ResponseHandler}
     * containing the HTML document of the MAL import page.
     * </p>
     *
     * @param userId the unique identifier of the user whose MAL import page is being requested.
     *               Must not be null.
     * @return a {@link ResponseHandler} containing the HTML {@link Document} of the MAL import page.
     * @throws IllegalArgumentException if the userId is null or invalid.
     * @throws NotFoundException        if the user or the MAL import page is not found.
     * @throws ForbiddenException       if the user is not authorized to access the MAL import page.
     */
    ResponseHandler<Document> getImportMalListPage(Long userId) {
        return client.get(
                HttpRequest.builder()
                        .target(SHINDEN_URL)
                        .path("/user/" + userId + "/import-mal")
                        .build()
        );
    }
}
