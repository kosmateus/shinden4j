package com.github.kosmateus.shinden.user;

import com.github.kosmateus.shinden.exception.ForbiddenException;
import com.github.kosmateus.shinden.exception.NotFoundException;
import com.github.kosmateus.shinden.http.response.HttpStatus;
import com.github.kosmateus.shinden.http.response.ResponseHandler;
import com.github.kosmateus.shinden.user.common.UserId;
import com.github.kosmateus.shinden.user.mapper.UserAchievementsMapper;
import com.github.kosmateus.shinden.user.mapper.UserFavouriteTagsMapper;
import com.github.kosmateus.shinden.user.mapper.UserInformationMapper;
import com.github.kosmateus.shinden.user.mapper.UserOverviewMapper;
import com.github.kosmateus.shinden.user.mapper.UserRecommendationMapper;
import com.github.kosmateus.shinden.user.mapper.UserReviewsMapper;
import com.github.kosmateus.shinden.user.mapper.UserSettingsMapper;
import com.github.kosmateus.shinden.user.request.AddToListSettingsRequest;
import com.github.kosmateus.shinden.user.request.BaseSettingsRequest;
import com.github.kosmateus.shinden.user.request.FavouriteTagsRequest;
import com.github.kosmateus.shinden.user.request.ListsSettingsRequest;
import com.github.kosmateus.shinden.user.request.UserInformationRequest;
import com.github.kosmateus.shinden.user.response.Achievements;
import com.github.kosmateus.shinden.user.response.FavouriteTag;
import com.github.kosmateus.shinden.user.response.Recommendation;
import com.github.kosmateus.shinden.user.response.Review;
import com.github.kosmateus.shinden.user.response.UserInformation;
import com.github.kosmateus.shinden.user.response.UserOverview;
import com.github.kosmateus.shinden.user.response.UserSettings;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection.KeyVal;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.Map;

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
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Inject))
class UserApiImpl implements UserApi {

    private final UserClient client;
    private final UserOverviewMapper overviewMapper;
    private final UserAchievementsMapper achievementsMapper;
    private final UserFavouriteTagsMapper favouriteTagsMapper;
    private final UserReviewsMapper reviewsMapper;
    private final UserRecommendationMapper recommendationsMapper;
    private final UserInformationMapper informationMapper;
    private final UserSettingsMapper settingsMapper;

    @Override
    public UserOverview getOverview(Long userId) {
        ResponseHandler<Document> userPage = client.getUserPage(userId);

        if (userPage.hasStatus(HttpStatus.NOT_FOUND)) {
            throw new NotFoundException("User page not found");
        }

        return overviewMapper.map(userPage.getEntity());
    }

    @Override
    public Achievements getAchievements(Long userId) {
        ResponseHandler<Document> achievementsPage = client.getAchievementsPage(userId);

        if (achievementsPage.hasStatus(HttpStatus.NOT_FOUND)) {
            throw new NotFoundException("Achievements page not found");
        }

        return achievementsMapper.map(achievementsPage.getEntity());
    }

    @Override
    public List<FavouriteTag> getFavouriteTags(FavouriteTagsRequest request) {

        ResponseHandler<Document> favouriteTagsPage = client.getFavouriteTagsPage(request);

        if (favouriteTagsPage.hasStatus(HttpStatus.NOT_FOUND)) {
            throw new NotFoundException("Favourite tags page not found");
        }

        return favouriteTagsMapper.map(favouriteTagsPage.getEntity());
    }

    @Override
    public List<Review> getReviews(Long userId) {
        ResponseHandler<Document> reviewsPage = client.getReviewsPage(userId);

        if (reviewsPage.hasStatus(HttpStatus.NOT_FOUND)) {
            throw new NotFoundException("Reviews page not found");
        }

        return reviewsMapper.map(reviewsPage.getEntity());
    }

    @Override
    public List<Recommendation> getRecommendations(Long userId) {
        ResponseHandler<Document> recommendationsPage = client.getRecommendationsPage(userId);

        if (recommendationsPage.hasStatus(HttpStatus.NOT_FOUND)) {
            throw new NotFoundException("Recommendations page not found");
        }

        return recommendationsMapper.map(recommendationsPage.getEntity());
    }

    @Override
    public UserInformation getInformation(Long userId) {
        ResponseHandler<Document> informationPage = client.getInformationEditPage(userId);

        if (informationPage.hasStatus(HttpStatus.NOT_FOUND)) {
            throw new NotFoundException("Information page not found");
        }
        if (informationPage.hasStatus(HttpStatus.FORBIDDEN)) {
            throw new ForbiddenException("Information page is forbidden");
        }

        return informationMapper.map(informationPage.getEntity());
    }

    @Override
    public void updateInformation(UserInformationRequest request) {
        ResponseHandler<Document> informationEditPage = client.getInformationEditPage(request.getUserId());
        if (informationEditPage.hasStatus(HttpStatus.NOT_FOUND)) {
            throw new NotFoundException("Information page not found");
        }
        Map<String, String> updateUserInformationFormData = informationMapper.map(informationEditPage.getEntity(), request);
        client.updateInformation(request.getUserId(), updateUserInformationFormData);
    }

    @Override
    public UserSettings getSettings(Long userId) {
        ResponseHandler<Document> settingsPage = client.getSettingsPage(userId);

        if (settingsPage.hasStatus(HttpStatus.NOT_FOUND)) {
            throw new NotFoundException("Settings page not found");
        }

        return settingsMapper.map(settingsPage.getEntity());
    }

    @Override
    public void updateBaseSettings(BaseSettingsRequest request) {
        ResponseHandler<Document> settingsPage = getSettingsPage(request);
        List<KeyVal> formData = settingsMapper.map(settingsPage.getEntity(), request);
        client.updatePageSettings(request.getUserId(), formData);
    }

    @Override
    public void updateListsSettings(ListsSettingsRequest request) {
        ResponseHandler<Document> settingsPage = getSettingsPage(request);
        List<KeyVal> formData = settingsMapper.map(settingsPage.getEntity(), request);
        client.updateSettings(request.getUserId(), formData);
    }

    @Override
    public void updateAddToListSettings(AddToListSettingsRequest request) {
        ResponseHandler<Document> settingsPage = getSettingsPage(request);
        List<KeyVal> formData = settingsMapper.map(settingsPage.getEntity(), request);
        client.updateSettings(request.getUserId(), formData);
    }

    private ResponseHandler<Document> getSettingsPage(UserId request) {
        ResponseHandler<Document> settingsPage = client.getSettingsPage(request.getUserId());
        if (settingsPage.hasStatus(HttpStatus.NOT_FOUND)) {
            throw new NotFoundException("Settings page not found");
        }
        return settingsPage;
    }
}
