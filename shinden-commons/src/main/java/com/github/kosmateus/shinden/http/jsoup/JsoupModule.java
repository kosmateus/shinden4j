package com.github.kosmateus.shinden.http.jsoup;

import com.github.kosmateus.shinden.auth.SessionManager;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;

/**
 * Guice module for configuring dependencies related to Jsoup-based HTTP clients.
 * <p>
 * The {@code JsoupModule} sets up the dependency injection bindings for the session management
 * and HTTP client classes, ensuring that they are available as singletons within the application context.
 * </p>
 *
 * @version 1.0.0
 */
@RequiredArgsConstructor
public class JsoupModule extends AbstractModule {

    private final SessionManager sessionManager;

    /**
     * Configures the bindings for the dependencies used by Jsoup HTTP clients.
     * <p>
     * This method binds the {@link SessionManager} to an instance provided at module creation
     * and configures the {@link JsoupCallExecutor} and {@link JsoupClient} to be singletons
     * within the Guice injector.
     * </p>
     */
    @Override
    protected void configure() {
        bind(SessionManager.class).toInstance(sessionManager);
        bind(JsoupCallExecutor.class).in(Singleton.class);
        bind(JsoupClient.class).in(Singleton.class);
    }

}
