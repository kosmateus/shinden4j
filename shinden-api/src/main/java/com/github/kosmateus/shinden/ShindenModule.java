package com.github.kosmateus.shinden;

import com.github.kosmateus.shinden.anime.AnimeModule;
import com.github.kosmateus.shinden.auth.SessionManager;
import com.github.kosmateus.shinden.login.LoginModule;
import com.github.kosmateus.shinden.user.UserModule;
import com.google.inject.AbstractModule;
import lombok.RequiredArgsConstructor;

/**
 * The {@code ShindenModule} class is a Guice module that configures the dependency injection
 * setup for the Shinden API components.
 *
 * <p>This module integrates the necessary sub-modules, such as {@link LoginModule} and {@link UserModule},
 * ensuring that all dependencies required by the Shinden API are properly bound and available for injection.</p>
 *
 * <p>This module is typically used internally within the {@link ShindenApi} class to bootstrap the API client.</p>
 *
 * @version 1.0.0
 */
@RequiredArgsConstructor
class ShindenModule extends AbstractModule {

    private final SessionManager sessionManager;

    /**
     * Configures the bindings for the Shinden API.
     *
     * <p>This method installs the {@link LoginModule}, {@link UserModule}, and {@link AnimeModule},
     * which handle the setup of dependencies related to login, user, and anime functionalities.</p>
     */
    @Override
    protected void configure() {
        install(new LoginModule(sessionManager));
        install(new UserModule(sessionManager));
        install(new AnimeModule(sessionManager));
    }
}
