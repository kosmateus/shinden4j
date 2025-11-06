package com.github.kosmateus.shinden.anime;

import com.github.kosmateus.shinden.anime.mapper.AnimeDetailsMapper;
import com.github.kosmateus.shinden.anime.mapper.AnimeSearchMapper;
import com.github.kosmateus.shinden.auth.SessionManager;
import com.github.kosmateus.shinden.http.HttpModule;
import com.github.kosmateus.shinden.utils.ValidationInvocationHandler;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;

/**
 * Guice module for configuring dependencies related to anime operations.
 *
 * <p>The {@code AnimeModule} class sets up the dependency injection bindings required for anime-related
 * functionalities within the application. This includes binding the {@link SessionManager}, installing
 * the {@link HttpModule}, and configuring singleton instances for the {@link AnimeHttpClient}, {@link AnimeApiImpl},
 * and {@link AnimeSearchMapper}.</p>
 *
 * @version 1.0.0
 */
@RequiredArgsConstructor
public class AnimeModule extends AbstractModule {

    private final SessionManager sessionManager;

    /**
     * Configures the module by setting up the required bindings.
     *
     * <p>This method installs the {@link HttpModule}, binds the {@link SessionManager} instance,
     * and ensures that {@link AnimeHttpClient}, {@link AnimeApiImpl}, and {@link AnimeSearchMapper} are treated as singletons
     * within the dependency injection context.</p>
     */
    @Override
    protected void configure() {
        install(new HttpModule(sessionManager));
        bind(SessionManager.class).toInstance(sessionManager);
        bind(AnimeHttpClient.class).in(Singleton.class);
        bind(AnimeApi.class).toProvider(AnimeApiProvider.class).in(Singleton.class);
        bind(AnimeSearchMapper.class).in(Singleton.class);
        bind(AnimeDetailsMapper.class).in(Singleton.class);
    }

    /**
     * Provider for {@link AnimeApi} that uses a validation invocation handler to create a proxy for {@link AnimeApiImpl}.
     */
    @RequiredArgsConstructor(onConstructor_ = @__(@Inject))
    static class AnimeApiProvider implements Provider<AnimeApi> {

        private final AnimeApiImpl animeApiImpl;

        @Override
        public AnimeApi get() {
            return ValidationInvocationHandler.createProxy(animeApiImpl, AnimeApi.class);
        }
    }
}
