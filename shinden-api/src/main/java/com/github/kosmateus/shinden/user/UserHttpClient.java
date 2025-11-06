package com.github.kosmateus.shinden.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.kosmateus.shinden.http.request.FileResource;
import com.github.kosmateus.shinden.http.request.HttpRequest;
import com.github.kosmateus.shinden.http.request.HttpRequest.KeyValue;
import com.github.kosmateus.shinden.http.response.ResponseHandler;
import com.github.kosmateus.shinden.http.rest.HttpClient;
import com.github.kosmateus.shinden.request.Pageable;
import com.github.kosmateus.shinden.user.common.enums.UserTitleStatus;
import com.github.kosmateus.shinden.user.request.AnimeListRequest;
import com.github.kosmateus.shinden.user.request.AnimeListRequest.SortType;
import com.github.kosmateus.shinden.user.response.AnimeListItem;
import com.github.kosmateus.shinden.user.response.ListResponse;
import com.github.kosmateus.shinden.utils.PathParamsBuilder;
import com.github.kosmateus.shinden.utils.SortParamsBuilder;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.github.kosmateus.shinden.constants.ShindenConstants.SHINDEN_URL;
import static com.github.kosmateus.shinden.constants.ShindenConstants.SHINDEN_USER_LIST_URL;

/**
 * Client for handling user-related HTTP requests.
 *
 * <p>The {@code UserHttpClient} class is responsible for making HTTP requests related to user operations,
 * such as updating user avatars and passwords, using the {@link HttpClient} for executing requests.</p>
 *
 * @version 1.0.0
 */
@RequiredArgsConstructor(onConstructor_ = @__(@Inject))
class UserHttpClient {
    private final HttpClient httpClient;

    /**
     * Updates the avatar of a user by uploading a file.
     *
     * <p>This method sends an HTTP POST request to update the user's avatar with a provided image file.
     * It includes form data and the file to be uploaded.</p>
     *
     * @param userId       the ID of the user whose avatar is being updated
     * @param formData     a map containing form fields required for the avatar update
     * @param fileResource the {@link FileResource} representing the file to be uploaded
     * @return a {@link ResponseHandler} containing the response as a string
     */
    ResponseHandler<String> updateUserAvatar(Long userId, Map<String, String> formData, FileResource fileResource) {
        return httpClient.post(HttpRequest.builder()
                .formFields(formData)
                .target(SHINDEN_URL)
                .path("/user/" + userId + "/edit_avatar")
                .fileResources(ImmutableMap.of("avatar-local", fileResource))
                .build(), String.class);
    }

    /**
     * Updates the avatar of a user without uploading a file.
     *
     * <p>This method sends an HTTP POST request to update the user's avatar using only form data.</p>
     *
     * @param userId   the ID of the user whose avatar is being updated
     * @param formData a map containing form fields required for the avatar update
     * @return a {@link ResponseHandler} containing the response as a string
     */
    ResponseHandler<String> updateUserAvatar(Long userId, Map<String, String> formData) {
        return httpClient.post(HttpRequest.builder()
                .target(SHINDEN_URL)
                .formFields(formData)
                .path("/user/" + userId + "/edit_avatar")
                .build(), String.class);
    }

    /**
     * Updates the password of a user.
     *
     * <p>This method sends an HTTP POST request to update the user's password using the provided form data.</p>
     *
     * @param userId   the ID of the user whose password is being updated
     * @param formData a map containing form fields required for the password update
     * @return a {@link ResponseHandler} containing the response as a string
     */
    ResponseHandler<String> updatePassword(Long userId, Map<String, String> formData) {
        return httpClient.post(HttpRequest.builder()
                .target(SHINDEN_URL)
                .formFields(formData)
                .path("/user/" + userId + "/edit_passwd")
                .build(), String.class);
    }

    /**
     * Imports a MAL list for a user.
     *
     * <p>This method imports a MAL list for a user by uploading a file containing the MAL list data.
     * The file is expected to be in a valid format that can be processed by the platform.</p>
     *
     * @param userId       the ID of the user whose MAL list is being imported
     * @param formData     a map containing form fields required for the MAL list import
     * @param fileResource the {@link FileResource} representing the file to be uploaded
     * @return a {@link ResponseHandler} containing the response as a string
     */
    ResponseHandler<String> importMalList(Long userId, Map<String, String> formData, FileResource fileResource) {
        return httpClient.post(HttpRequest.builder()
                .target(SHINDEN_URL)
                .formFields(formData)
                .fileResources(ImmutableMap.of("mal-list", fileResource))
                .path("/user/" + userId + "/import_mal/1")
                .build(), String.class);
    }

    /**
     * Retrieves the anime list for a user based on the provided request and optional pagination.
     *
     * <p>This method fetches a list of anime items from a user's anime list according to the specified
     * request criteria and pagination details. If no pagination is provided, all matching anime items
     * are returned.</p>
     *
     * @param request  the {@link AnimeListRequest} containing the criteria for fetching the user's anime list. Must not be null.
     * @param pageable an optional {@link Pageable} object containing pagination information, such as page number and size. Can be null.
     * @return a {@link ResponseHandler} containing a {@link ListResponse} of {@link AnimeListItem} representing the user's anime list.
     */
    ResponseHandler<ListResponse<AnimeListItem>> getAnimeList(AnimeListRequest request, @Nullable Pageable<SortType> pageable) {
        String path = "/api/userlist/" + request.getUserId() + "/anime" + Optional.ofNullable(request.getStatus())
                .map(UserTitleStatus::getPathValue)
                .map(status -> "/" + status)
                .orElse("");

        List<KeyValue> queryParams = request.toQueryParams();
        if (pageable == null) {
            queryParams.add(KeyValue.of("limit", "100000"));
            return httpClient.get(HttpRequest.builder()
                    .target(SHINDEN_USER_LIST_URL)
                    .path(path)
                    .pathParams(PathParamsBuilder.build(request.getStatus()))
                    .queryParams(queryParams)
                    .build(), new TypeReference<ListResponse<AnimeListItem>>() {
            });
        }

        queryParams.add(KeyValue.of("limit", String.valueOf(pageable.getPageSize())));
        queryParams.add(KeyValue.of("offset", String.valueOf(pageable.getOffset())));
        queryParams.add(KeyValue.of("sort", SortParamsBuilder.build(pageable)));
        return httpClient.get(HttpRequest.builder()
                .target(SHINDEN_USER_LIST_URL)
                .path(path)
                .pathParams(PathParamsBuilder.build(request.getStatus()))
                .queryParams(queryParams)
                .build(), new TypeReference<ListResponse<AnimeListItem>>() {
        });
    }
}
