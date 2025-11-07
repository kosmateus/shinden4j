package com.github.kosmateus.shinden.user;

import com.github.kosmateus.shinden.exception.NotFoundException;
import com.github.kosmateus.shinden.http.response.HttpStatus;
import com.github.kosmateus.shinden.http.response.ResponseHandler;
import com.github.kosmateus.shinden.mapper.CommonMapper;
import com.github.kosmateus.shinden.request.Pageable;
import com.github.kosmateus.shinden.response.Page;
import com.github.kosmateus.shinden.response.PageImpl;
import com.github.kosmateus.shinden.response.UpdateResult;
import com.github.kosmateus.shinden.user.common.UserId;
import com.github.kosmateus.shinden.user.mapper.UserAccountMapper;
import com.github.kosmateus.shinden.user.mapper.UserAchievementsMapper;
import com.github.kosmateus.shinden.user.mapper.UserFavouriteTagsMapper;
import com.github.kosmateus.shinden.user.mapper.UserImportMalListMapper;
import com.github.kosmateus.shinden.user.mapper.UserInformationMapper;
import com.github.kosmateus.shinden.user.mapper.UserOverviewMapper;
import com.github.kosmateus.shinden.user.mapper.UserRecommendationMapper;
import com.github.kosmateus.shinden.user.mapper.UserReviewsMapper;
import com.github.kosmateus.shinden.user.mapper.UserSettingsMapper;
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
import com.github.kosmateus.shinden.user.response.ListResponse;
import com.github.kosmateus.shinden.user.response.Recommendation;
import com.github.kosmateus.shinden.user.response.Review;
import com.github.kosmateus.shinden.user.response.UserInformation;
import com.github.kosmateus.shinden.user.response.UserOverview;
import com.github.kosmateus.shinden.user.response.UserSettings;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.jsoup.Connection.KeyVal;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.Map;

import static com.github.kosmateus.shinden.utils.ResponseHandlerValidator.validateResponse;

/**
 * Implementation of the {@link UserApi} interface for managing user-related operations.
 * <p>
 * The {@code UserApiImpl} class provides concrete implementations of the methods defined
 * in the {@link UserApi} interface, allowing the application to interact with user data
 * such as profiles, achievements, favourite tags, reviews, and recommendations. It also
 * supports updating user information.
 * </p>
 *
 * @version 1.0.0
 */
@RequiredArgsConstructor(onConstructor_ = @__(@Inject))
class UserApiImpl implements UserApi {

    private final UserJsoupClient jsoupClient;
    private final UserHttpClient httpClient;
    private final CommonMapper commonMapper;
    private final UserOverviewMapper overviewMapper;
    private final UserAchievementsMapper achievementsMapper;
    private final UserFavouriteTagsMapper favouriteTagsMapper;
    private final UserReviewsMapper reviewsMapper;
    private final UserRecommendationMapper recommendationsMapper;
    private final UserInformationMapper informationMapper;
    private final UserSettingsMapper settingsMapper;
    private final UserAccountMapper accountMapper;
    private final UserImportMalListMapper userImportMalListMapper;

    @Override
    public UserOverview getOverview(Long userId) {
        ResponseHandler<Document> userPage = jsoupClient.getUserPage(userId);
        validateResponse(userPage);
        return overviewMapper.map(userPage.getEntity());
    }

    @Override
    public Achievements getAchievements(Long userId) {
        ResponseHandler<Document> achievementsPage = jsoupClient.getAchievementsPage(userId);
        validateResponse(achievementsPage);
        return achievementsMapper.map(achievementsPage.getEntity());
    }

    @Override
    public List<FavouriteTag> getFavouriteTags(FavouriteTagsRequest request) {
        ResponseHandler<Document> favouriteTagsPage = jsoupClient.getFavouriteTagsPage(request);
        validateResponse(favouriteTagsPage);
        return favouriteTagsMapper.map(favouriteTagsPage.getEntity());
    }

    @Override
    public List<Review> getReviews(Long userId) {
        ResponseHandler<Document> reviewsPage = jsoupClient.getReviewsPage(userId);
        validateResponse(reviewsPage);
        return reviewsMapper.map(reviewsPage.getEntity());
    }

    @Override
    public List<Recommendation> getRecommendations(Long userId) {
        ResponseHandler<Document> recommendationsPage = jsoupClient.getRecommendationsPage(userId);
        validateResponse(recommendationsPage);
        return recommendationsMapper.map(recommendationsPage.getEntity());
    }

    @Override
    public UserInformation getInformation(Long userId) {
        ResponseHandler<Document> informationPage = jsoupClient.getInformationEditPage(userId);
        validateResponse(informationPage);
        return informationMapper.map(informationPage.getEntity());
    }

    @Override
    public UpdateResult updateInformation(UserInformationRequest request) {
        ResponseHandler<Document> informationEditPage = jsoupClient.getInformationEditPage(request.getUserId());
        validateResponse(informationEditPage);
        Map<String, String> updateUserInformationFormData = informationMapper.map(informationEditPage.getEntity(), request);
        return commonMapper.map(jsoupClient.updateInformation(request.getUserId(), updateUserInformationFormData));
    }

    @Override
    public UserSettings getSettings(Long userId) {
        ResponseHandler<Document> settingsPage = jsoupClient.getSettingsPage(userId);
        validateResponse(settingsPage);
        return settingsMapper.map(settingsPage.getEntity());
    }

    @Override
    public UpdateResult updateBaseSettings(BaseSettingsRequest request) {
        ResponseHandler<Document> settingsPage = getSettingsPage(request);
        validateResponse(settingsPage);
        List<KeyVal> formData = settingsMapper.map(settingsPage.getEntity(), request);
        return commonMapper.map(jsoupClient.updatePageSettings(request.getUserId(), formData));
    }

    @Override
    public UpdateResult updateListsSettings(ListsSettingsRequest request) {
        ResponseHandler<Document> settingsPage = getSettingsPage(request);
        validateResponse(settingsPage);
        List<KeyVal> formData = settingsMapper.map(settingsPage.getEntity(), request);
        return commonMapper.map(jsoupClient.updateSettings(request.getUserId(), formData));
    }

    @Override
    public UpdateResult updateAddToListSettings(AddToListSettingsRequest request) {
        ResponseHandler<Document> settingsPage = getSettingsPage(request);
        validateResponse(settingsPage);
        List<KeyVal> formData = settingsMapper.map(settingsPage.getEntity(), request);
        return commonMapper.map(jsoupClient.updateSettings(request.getUserId(), formData));
    }

    @Override
    public UpdateResult updateAvatar(AvatarFileUpdateRequest request) {
        ResponseHandler<Document> editAvatarPage = jsoupClient.getEditAvatarPage(request.getUserId());
        validateResponse(editAvatarPage);
        Map<String, String> formData = accountMapper.mapToUpdateAvatar(editAvatarPage.getEntity());
        return commonMapper.map(httpClient.updateUserAvatar(request.getUserId(), formData, request.getAvatar()));

    }

    @Override
    public UpdateResult updateAvatar(AvatarUrlUpdateRequest request) {
        ResponseHandler<Document> editAvatarPage = jsoupClient.getEditAvatarPage(request.getUserId());
        validateResponse(editAvatarPage);
        Map<String, String> formData = accountMapper.mapToUpdateAvatar(editAvatarPage.getEntity(), request);
        return commonMapper.map(httpClient.updateUserAvatar(request.getUserId(), formData));
    }

    @Override
    public UpdateResult deleteAvatar(Long userId) {
        ResponseHandler<Document> editAvatarPage = jsoupClient.getEditAvatarPage(userId);
        validateResponse(editAvatarPage);
        Map<String, String> formData = accountMapper.mapToDeleteAvatar(editAvatarPage.getEntity());
        return commonMapper.map(httpClient.updateUserAvatar(userId, formData));
    }

    @Override
    public UpdateResult updatePassword(UpdatePasswordRequest request) {
        ResponseHandler<Document> settingsPage = jsoupClient.getEditPasswordPage(request.getUserId());
        Map<String, String> formData = accountMapper.mapToUpdatePassword(settingsPage.getEntity(), request);
        return commonMapper.map(httpClient.updatePassword(request.getUserId(), formData));
    }

    @Override
    public UpdateResult importMalList(ImportMalListRequest request) {
        ResponseHandler<Document> importPage = jsoupClient.getImportMalListPage(request.getUserId());
        validateResponse(importPage);
        Map<String, String> formData = userImportMalListMapper.map(importPage.getEntity(), request);
        return commonMapper.map(httpClient.importMalList(request.getUserId(), formData, request.getMalListFile()));
    }

    @Override
    public Page<AnimeListItem> getAnimeList(AnimeListRequest request, Pageable<SortType> pageable) {
        ResponseHandler<ListResponse<AnimeListItem>> animeList = httpClient.getAnimeList(request, pageable);
        validateResponse(animeList);
        return new PageImpl<>(animeList.getEntity().getResult().getItems(), pageable, animeList.getEntity().getResult().getCount());
    }

    @Override
    public List<AnimeListItem> getAnimeList(AnimeListRequest request) {
        ResponseHandler<ListResponse<AnimeListItem>> animeList = httpClient.getAnimeList(request, null);
        validateResponse(animeList);
        return animeList.getEntity().getResult().getItems();
    }


    private ResponseHandler<Document> getSettingsPage(UserId request) {
        ResponseHandler<Document> settingsPage = jsoupClient.getSettingsPage(request.getUserId());
        if (settingsPage.hasStatus(HttpStatus.NOT_FOUND)) {
            throw new NotFoundException("Settings page not found");
        }
        return settingsPage;
    }
}
