package com.github.kosmateus.shinden.user;

import com.github.kosmateus.shinden.exception.ForbiddenException;
import com.github.kosmateus.shinden.exception.JsoupParserException;
import com.github.kosmateus.shinden.exception.NotFoundException;
import com.github.kosmateus.shinden.request.Pageable;
import com.github.kosmateus.shinden.response.Page;
import com.github.kosmateus.shinden.response.UpdateResult;
import com.github.kosmateus.shinden.user.request.AddToListSettingsRequest;
import com.github.kosmateus.shinden.user.request.AnimeListRequest;
import com.github.kosmateus.shinden.user.request.AnimeListRequest.SortType;
import com.github.kosmateus.shinden.user.request.AvatarFileUpdateRequest;
import com.github.kosmateus.shinden.user.request.AvatarUrlUpdateRequest;
import com.github.kosmateus.shinden.user.request.BaseSettingsRequest;
import com.github.kosmateus.shinden.user.request.FavouriteTagsRequest;
import com.github.kosmateus.shinden.user.request.ImportMalListRequest;
import com.github.kosmateus.shinden.user.request.ListsSettingsRequest;
import com.github.kosmateus.shinden.user.request.UpdatePasswordRequest;
import com.github.kosmateus.shinden.user.request.UserInformationRequest;
import com.github.kosmateus.shinden.user.response.Achievements;
import com.github.kosmateus.shinden.user.response.AnimeListItem;
import com.github.kosmateus.shinden.user.response.FavouriteTag;
import com.github.kosmateus.shinden.user.response.Recommendation;
import com.github.kosmateus.shinden.user.response.Review;
import com.github.kosmateus.shinden.user.response.UserInformation;
import com.github.kosmateus.shinden.user.response.UserOverview;
import com.github.kosmateus.shinden.user.response.UserSettings;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Interface defining user-related API operations.
 * <p>
 * The {@code UserApi} interface provides methods to interact with user-related data on the platform.
 * It includes operations for retrieving and updating user information, fetching user achievements,
 * favourite tags, reviews, and recommendations.
 * </p>
 *
 * @version 1.0.0
 */
public interface UserApi {

    /**
     * Retrieves the overview of a user by their ID.
     * <p>
     * This method fetches a general overview of the user, including their profile information and other relevant details.
     * </p>
     *
     * @param userId the ID of the user
     * @return the {@link UserOverview} containing the user's overview information
     * @throws NotFoundException    if the user page is not found
     * @throws JsoupParserException if there is an error parsing the web page
     */
    UserOverview getOverview(@NotNull Long userId);

    /**
     * Retrieves the achievements of a user by their ID.
     * <p>
     * This method fetches the achievements earned by the user, such as badges, awards, and other recognitions.
     * </p>
     *
     * @param userId the ID of the user
     * @return the {@link Achievements} containing the user's achievements
     * @throws NotFoundException    if the achievements page is not found
     * @throws JsoupParserException if there is an error parsing the web page
     */
    Achievements getAchievements(@NotNull Long userId);

    /**
     * Retrieves the favourite tags of a user based on the request.
     * <p>
     * This method returns a list of tags that the user has marked as favourites, based on the details provided in the request.
     * </p>
     *
     * @param request the {@link FavouriteTagsRequest} containing user and tag details
     * @return a list of {@link FavouriteTag} representing the user's favourite tags
     * @throws NotFoundException       if the favourite tags page is not found
     * @throws JsoupParserException    if there is an error parsing the web page
     * @throws IllegalArgumentException if the request is null or contains invalid data
     */
    List<FavouriteTag> getFavouriteTags(@Valid @NotNull FavouriteTagsRequest request);

    /**
     * Retrieves the reviews of a user by their ID.
     * <p>
     * This method returns a list of reviews written by the user, providing insights into their opinions and experiences.
     * </p>
     *
     * @param userId the ID of the user
     * @return a list of {@link Review} representing the user's reviews
     * @throws NotFoundException    if the reviews page is not found
     * @throws JsoupParserException if there is an error parsing the web page
     */
    List<Review> getReviews(@NotNull Long userId);

    /**
     * Retrieves the recommendations of a user by their ID.
     * <p>
     * This method returns a list of recommendations provided by the user, offering suggestions based on their preferences.
     * </p>
     *
     * @param userId the ID of the user
     * @return a list of {@link Recommendation} representing the user's recommendations
     * @throws NotFoundException    if the recommendations page is not found
     * @throws JsoupParserException if there is an error parsing the web page
     */
    List<Recommendation> getRecommendations(@NotNull Long userId);

    /**
     * Retrieves the information of a user by their ID.
     * <p>
     * This method fetches detailed information about the user, such as personal details, settings, and preferences.
     * </p>
     *
     * @param userId the ID of the user
     * @return the {@link UserInformation} containing the user's detailed information
     * @throws NotFoundException    if the information page is not found
     * @throws ForbiddenException   if access to the information page is forbidden
     * @throws JsoupParserException if there is an error parsing the web page
     */
    UserInformation getInformation(@NotNull Long userId);

    /**
     * Updates the information of a user.
     * <p>
     * This method updates the user's information based on the data provided in the request, such as profile settings and preferences.
     * </p>
     *
     * @param request the {@link UserInformationRequest} containing updated user information
     * @return an {@link UpdateResult} containing the result of the update operation.
     * @throws NotFoundException       if the information page is not found
     * @throws ForbiddenException      if the user is not authorized to update the information
     * @throws JsoupParserException    if there is an error parsing the web page
     * @throws IllegalArgumentException if the request is null or contains invalid data
     */
    UpdateResult updateInformation(@Valid @NotNull UserInformationRequest request);

    /**
     * Retrieves the settings of a user by their ID.
     * <p>
     * This method fetches the user's settings, such as preferences and configurations.
     * </p>
     *
     * @param userId the ID of the user
     * @return the {@link UserSettings} containing the user's settings
     * @throws NotFoundException    if the settings page is not found
     * @throws ForbiddenException   if access to the settings page is forbidden
     * @throws JsoupParserException if there is an error parsing the web page
     */
    UserSettings getSettings(@NotNull Long userId);

    /**
     * Updates the base settings of a user.
     * <p>
     * This method updates the user's base settings, such as account-level configurations, based on the data provided in the request.
     * </p>
     *
     * @param request the {@link BaseSettingsRequest} containing the updated base settings
     * @return an {@link UpdateResult} containing the result of the update operation.
     * @throws NotFoundException       if the settings page is not found
     * @throws ForbiddenException      if the user is not authorized to update the settings
     * @throws JsoupParserException    if there is an error parsing the web page
     * @throws IllegalArgumentException if the request is null or contains invalid data
     */
    UpdateResult updateBaseSettings(@Valid @NotNull BaseSettingsRequest request);

    /**
     * Updates the list settings of a user.
     * <p>
     * This method updates the user's list settings, which may include settings for anime and manga lists, based on the data provided in the request.
     * </p>
     *
     * @param request the {@link ListsSettingsRequest} containing the updated list settings
     * @return an {@link UpdateResult} containing the result of the update operation.
     * @throws NotFoundException       if the settings page is not found
     * @throws ForbiddenException      if the user is not authorized to update the settings
     * @throws JsoupParserException    if there is an error parsing the web page
     * @throws IllegalArgumentException if the request is null or contains invalid data
     */
    UpdateResult updateListsSettings(@Valid @NotNull ListsSettingsRequest request);

    /**
     * Updates the "Add to List" settings of a user.
     * <p>
     * This method updates the user's "Add to List" settings, such as how items are added to their lists, based on the data provided in the request.
     * </p>
     *
     * @param request the {@link AddToListSettingsRequest} containing the updated "Add to List" settings
     * @return an {@link UpdateResult} containing the result of the update operation.
     * @throws NotFoundException       if the settings page is not found
     * @throws ForbiddenException      if the user is not authorized to update the settings
     * @throws JsoupParserException    if there is an error parsing the web page
     * @throws IllegalArgumentException if the request is null or contains invalid data
     */
    UpdateResult updateAddToListSettings(@Valid @NotNull AddToListSettingsRequest request);

    /**
     * Updates the avatar of a user using a file.
     * <p>
     * This method updates the avatar for a user by uploading a new image file provided in the
     * {@link AvatarFileUpdateRequest}. The file is expected to be a valid image that will replace
     * the user's current avatar.
     * </p>
     *
     * @param request the {@link AvatarFileUpdateRequest} containing the user ID and the image file
     *                to be uploaded as the new avatar. Must not be null.
     * @return an {@link UpdateResult} containing the result of the update operation.
     * @throws NotFoundException        if the user is not found
     * @throws ForbiddenException       if the user is not authorized to update the avatar
     * @throws IllegalArgumentException if the request is null or contains an invalid file
     */
    UpdateResult updateAvatar(@Valid @NotNull AvatarFileUpdateRequest request);

    /**
     * Updates the avatar of a user using a URL.
     * <p>
     * This method updates the avatar for a user by fetching the image from the URL provided in the
     * {@link AvatarUrlUpdateRequest}. The URL is expected to point to a valid image that will replace
     * the user's current avatar.
     * </p>
     *
     * @param request the {@link AvatarUrlUpdateRequest} containing the user ID and the URL of the new
     *                avatar image. Must not be null.
     * @return an {@link UpdateResult} containing the result of the update operation.
     * @throws NotFoundException        if the user is not found
     * @throws ForbiddenException       if the user is not authorized to update the avatar
     * @throws JsoupParserException     if there is an error parsing the web page
     * @throws IllegalArgumentException if the request is null or contains an invalid URL
     */
    UpdateResult updateAvatar(@Valid @NotNull AvatarUrlUpdateRequest request);

    /**
     * Deletes the avatar of a user.
     * <p>
     * This method removes the avatar associated with the specified user ID. After deletion,
     * the user will no longer have an avatar associated with their profile.
     * </p>
     *
     * @param userId the unique identifier of the user whose avatar is to be deleted. Must not be null.
     * @return an {@link UpdateResult} containing the result of the delete operation.
     * @throws NotFoundException       if the user is not found
     * @throws ForbiddenException      if the user is not authorized to delete the avatar
     * @throws IllegalArgumentException if the userId is null
     */
    UpdateResult deleteAvatar(@NotNull Long userId);

    /**
     * Updates the user's password.
     * <p>
     * This method attempts to update the password for the user specified in the {@link UpdatePasswordRequest}.
     * It validates the current password, and if valid, updates it to the new password provided in the request.
     * </p>
     *
     * @param request the {@link UpdatePasswordRequest} containing the user ID, current password, and new password.
     *                Must not be null.
     * @return an {@link UpdateResult} containing the result of the password update operation, including
     * whether it was successful and any reasons for failure if applicable.
     * @throws IllegalArgumentException if the request is null or contains invalid data.
     * @throws NotFoundException        if the user is not found.
     * @throws ForbiddenException       if the current password is incorrect or the user is not authorized to update the password.
     */
    UpdateResult updatePassword(@Valid @NotNull UpdatePasswordRequest request);

    /**
     * Imports a MAL list for a user.
     * <p>
     * This method imports a MAL list for a user by uploading a file containing the MAL list data.
     * The file is expected to be in a valid format that can be processed by the platform.
     * </p>
     *
     * @param request the {@link ImportMalListRequest} containing the user ID, MAL list file, and import type.
     *                Must not be null.
     * @return an {@link UpdateResult} containing the result of the MAL list import operation, including
     * whether it was successful and any reasons for failure if applicable.
     * @throws IllegalArgumentException if the request is null or contains invalid data.
     * @throws NotFoundException        if the user is not found.
     * @throws ForbiddenException       if the user is not authorized to import the MAL list.
     */
    UpdateResult importMalList(@Valid @NotNull ImportMalListRequest request);


    /**
     * Retrieves a paginated list of anime items from a user's anime list.
     * <p>
     * This method fetches a paginated list of anime items based on the provided request criteria
     * and pagination information. The resulting list will contain only the anime items that match
     * the specified filters and sorting parameters.
     * </p>
     *
     * @param request  the {@link AnimeListRequest} containing the criteria for fetching the user's anime list.
     *                 Must not be null.
     * @param pageable the {@link Pageable} object containing pagination information, such as page number and size.
     *                 Must not be null.
     * @return a {@link Page} of {@link AnimeListItem} containing the user's anime list items matching the criteria.
     * @throws IllegalArgumentException if the request or pageable is null or contains invalid data.
     * @throws NotFoundException        if the user is not found.
     * @throws ForbiddenException       if the user is not authorized to access the anime list.
     */
    Page<AnimeListItem> getAnimeList(@Valid @NotNull AnimeListRequest request, @NotNull Pageable<SortType> pageable);

    /**
     * Retrieves a list of all anime items from a user's anime list.
     * <p>
     * This method fetches all anime items from the user's anime list based on the provided request criteria,
     * without applying pagination. The resulting list will contain all the anime items that match the specified
     * filters and sorting parameters.
     * </p>
     *
     * @param request the {@link AnimeListRequest} containing the criteria for fetching the user's anime list.
     *                Must not be null.
     * @return a {@link List} of {@link AnimeListItem} containing all the user's anime list items matching the criteria.
     * @throws IllegalArgumentException if the request is null or contains invalid data.
     * @throws NotFoundException        if the user is not found.
     * @throws ForbiddenException       if the user is not authorized to access the anime list.
     */
    List<AnimeListItem> getAnimeList(@Valid @NotNull AnimeListRequest request);


}
