package com.github.kosmateus.shinden.user;import static com.github.kosmateus.shinden.constants.ShindenConstants.SHINDEN_URL;import com.github.kosmateus.shinden.http.jsoup.JsoupClient;import com.github.kosmateus.shinden.http.request.HttpRequest;import com.github.kosmateus.shinden.http.response.ResponseHandler;import com.github.kosmateus.shinden.user.request.FavouriteTagsRequest;import com.google.inject.Inject;import java.util.List;import java.util.Map;import lombok.RequiredArgsConstructor;import org.jsoup.Connection.KeyVal;import org.jsoup.nodes.Document;/** * Client for handling HTTP requests related to user operations on Shinden. * <p> * The {@code UserClient} class is responsible for making HTTP requests to the Shinden website * to perform various user-related operations such as fetching user pages, achievements, * favourite tags, reviews, recommendations, and updating user information. * </p> * * @version 1.0.0 */@RequiredArgsConstructor(onConstructor_ = @__(@Inject))class UserClient {    private final JsoupClient client;    /**     * Retrieves the user page for the specified user ID.     *     * @param userId the ID of the user     * @return a {@link ResponseHandler} containing the response document     */    ResponseHandler<Document> getUserPage(Long userId) {        return client.get(                HttpRequest.builder()                        .target(SHINDEN_URL)                        .path("/user/" + userId)                        .build()        );    }    /**     * Retrieves the achievements page for the specified user ID.     *     * @param userId the ID of the user     * @return a {@link ResponseHandler} containing the response document     */    ResponseHandler<Document> getAchievementsPage(Long userId) {        return client.get(                HttpRequest.builder()                        .target(SHINDEN_URL)                        .path("/user/" + userId + "/achievements")                        .build()        );    }    /**     * Retrieves the favourite tags page for the specified user.     *     * @param request the {@link FavouriteTagsRequest} containing user and tag details     * @return a {@link ResponseHandler} containing the response document     */    ResponseHandler<Document> getFavouriteTagsPage(FavouriteTagsRequest request) {        return client.get(                HttpRequest.builder()                        .target(SHINDEN_URL)                        .path("/user/" + request.getUserId() + "/favourite-tags")                        .queryParams(request.toQueryParams())                        .build()        );    }    /**     * Retrieves the reviews page for the specified user ID.     *     * @param userId the ID of the user     * @return a {@link ResponseHandler} containing the response document     */    ResponseHandler<Document> getReviewsPage(Long userId) {        return client.get(                HttpRequest.builder()                        .target(SHINDEN_URL)                        .path("/user/" + userId + "/reviews")                        .build()        );    }    /**     * Retrieves the recommendations page for the specified user ID.     *     * @param userId the ID of the user     * @return a {@link ResponseHandler} containing the response document     */    ResponseHandler<Document> getRecommendationsPage(Long userId) {        return client.get(                HttpRequest.builder()                        .target(SHINDEN_URL)                        .path("/user/" + userId + "/recommendations")                        .build()        );    }    /**     * Retrieves the information edit page for the specified user ID.     *     * @param userId the ID of the user     * @return a {@link ResponseHandler} containing the response document     */    ResponseHandler<Document> getInformationEditPage(Long userId) {        return client.get(                HttpRequest.builder()                        .target(SHINDEN_URL)                        .path("/user/" + userId + "/edit")                        .build()        );    }    /**     * Updates the user's information with the provided form data.     *     * @param userId   the ID of the user     * @param formData a map containing the form data to update the user's information     */    void updateInformation(Long userId, Map<String, String> formData) {        client.post(                HttpRequest.builder()                        .target(SHINDEN_URL)                        .path("/user/" + userId + "/edit")                        .build(),                formData        );    }    /**     * Retrieves the settings page for the specified user ID.     *     * @param userId the ID of the user     * @return a {@link ResponseHandler} containing the response document     */    ResponseHandler<Document> getSettingsPage(Long userId) {        return client.get(                HttpRequest.builder()                        .target(SHINDEN_URL)                        .path("/user/" + userId + "/settings")                        .build()        );    }    /**     * Updates the user's page settings with the provided settings.     *     * @param userId       the ID of the user     * @param pageSettings a list of {@link KeyVal} representing the page settings to update     */    void updatePageSettings(Long userId, List<KeyVal> pageSettings) {        client.post(                HttpRequest.builder()                        .target(SHINDEN_URL)                        .path("/user/" + userId + "/edit-skin-and-time")                        .build(),                pageSettings        );    }    /**     * Updates the user's settings with the provided settings.     *     * @param userId   the ID of the user     * @param settings a list of {@link KeyVal} representing the settings to update     */    void updateSettings(Long userId, List<KeyVal> settings) {        client.post(                HttpRequest.builder()                        .target(SHINDEN_URL)                        .path("/user/" + userId + "/settings")                        .build(),                settings        );    }}