package com.github.kosmateus.shinden.login;

import com.github.kosmateus.shinden.auth.SessionManager;
import com.github.kosmateus.shinden.http.jsoup.JsoupModule;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;

/**
 * Guice module for configuring dependencies related to the login process.
 * <p>
 * The {@code LoginModule} class sets up the dependency injection bindings required for the
 * login functionality within the application. This includes binding the {@link SessionManager},
 * installing the {@link JsoupModule}, and setting up singletons for the {@link LoginClient}
 * and {@link LoginApi}.
 * </p>
 *
 * @version 1.0.0
 */
@RequiredArgsConstructor
public class LoginModule extends AbstractModule {

    private final SessionManager sessionManager;

    /**
     * Configures the module by setting up the required bindings.
     * <p>
     * This method installs the {@link JsoupModule}, binds the {@link SessionManager} instance,
     * and ensures that {@link LoginClient} and {@link LoginApi} are treated as singletons
     * within the dependency injection context.
     * </p>
     */
    @Override
    protected void configure() {
        install(new JsoupModule(sessionManager));
        bind(SessionManager.class).toInstance(sessionManager);
        bind(LoginClient.class).in(Singleton.class);
        bind(LoginApi.class).to(LoginApiImpl.class).in(Singleton.class);
    }
}
