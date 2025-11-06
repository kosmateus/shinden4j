package com.github.kosmateus.shinden.http.rest;

import com.github.kosmateus.shinden.auth.SessionManager;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;

/**
 * Guice module for configuring HTTP-related dependencies.
 * <p>
 * The {@code RestModule} class is responsible for setting up the necessary bindings
 * for making HTTP requests in the application. It ensures that the {@link SessionManager},
 * {@link HttpRestClientExecutor}, and {@link HttpClient} are properly configured and
 * managed as singletons within the dependency injection context provided by Guice.
 * </p>
 *
 * @version 1.0.0
 */
@RequiredArgsConstructor
public class RestModule extends AbstractModule {

    private final SessionManager sessionManager;

    /**
     * Configures the bindings for the module.
     * <p>
     * This method binds the {@link SessionManager} to the provided instance, ensuring that it is
     * available for injection throughout the application. Additionally, it binds
     * the {@link HttpRestClientExecutor} and {@link HttpClient} as singletons, meaning that the same
     * instance of these classes will be reused across the entire application, promoting efficient
     * resource usage and consistent behavior.
     * </p>
     */
    @Override
    protected void configure() {
        bind(SessionManager.class).toInstance(sessionManager);
        bind(HttpRestClientExecutor.class).in(Singleton.class);
        bind(HttpClient.class).in(Singleton.class);
    }
}
