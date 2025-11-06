package com.github.kosmateus.shinden.user;

import com.github.kosmateus.shinden.auth.SessionManager;
import com.github.kosmateus.shinden.http.HttpModule;
import com.github.kosmateus.shinden.user.mapper.UserAccountMapper;
import com.github.kosmateus.shinden.user.mapper.UserAchievementsMapper;
import com.github.kosmateus.shinden.user.mapper.UserFavouriteTagsMapper;
import com.github.kosmateus.shinden.user.mapper.UserInformationMapper;
import com.github.kosmateus.shinden.user.mapper.UserOverviewMapper;
import com.github.kosmateus.shinden.user.mapper.UserRecommendationMapper;
import com.github.kosmateus.shinden.user.mapper.UserReviewsMapper;
import com.github.kosmateus.shinden.user.mapper.UserSettingsMapper;
import com.github.kosmateus.shinden.utils.ValidationInvocationHandler;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;

/**
 * Guice module for configuring dependencies related to user operations.
 * <p>
 * The {@code UserModule} class sets up the dependency injection bindings required for user-related
 * functionalities within the application. This includes binding the {@link SessionManager}, installing
 * the {@link HttpModule}, and configuring singleton instances for user mappers and the {@link UserApiImpl}.
 * </p>
 *
 * @version 1.0.0
 */
@RequiredArgsConstructor
public class UserModule extends AbstractModule {

    private final SessionManager sessionManager;

    /**
     * Configures the module by setting up the required bindings.
     * <p>
     * This method installs the {@link HttpModule}, binds the {@link SessionManager} instance,
     * and ensures that {@link UserJsoupClient}, user mappers, and {@link UserApiImpl} are treated as singletons
     * within the dependency injection context.
     * </p>
     */
    @Override
    protected void configure() {
        install(new HttpModule(sessionManager));
        bind(SessionManager.class).toInstance(sessionManager);
        bind(UserApi.class).toProvider(UserApiProvider.class).in(Singleton.class);
        bind(UserJsoupClient.class).in(Singleton.class);
        bind(UserHttpClient.class).in(Singleton.class);
        bind(UserAccountMapper.class).in(Singleton.class);
        bind(UserAchievementsMapper.class).in(Singleton.class);
        bind(UserFavouriteTagsMapper.class).in(Singleton.class);
        bind(UserInformationMapper.class).in(Singleton.class);
        bind(UserOverviewMapper.class).in(Singleton.class);
        bind(UserRecommendationMapper.class).in(Singleton.class);
        bind(UserReviewsMapper.class).in(Singleton.class);
        bind(UserSettingsMapper.class).in(Singleton.class);
    }

    @RequiredArgsConstructor(onConstructor_ = @__(@Inject))
    static class UserApiProvider implements Provider<UserApi> {

        private final UserApiImpl userApiImpl;

        @Override
        public UserApi get() {
            return ValidationInvocationHandler.createProxy(userApiImpl, UserApi.class);
        }
    }
}
