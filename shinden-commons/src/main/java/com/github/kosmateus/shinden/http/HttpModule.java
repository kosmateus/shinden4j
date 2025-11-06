package com.github.kosmateus.shinden.http;

import com.github.kosmateus.shinden.auth.SessionManager;
import com.github.kosmateus.shinden.http.jsoup.JsoupModule;
import com.github.kosmateus.shinden.http.rest.RestModule;
import com.google.inject.AbstractModule;
import lombok.RequiredArgsConstructor;

/**
 * Configures the HTTP module for dependency injection.
 * <p>
 * The {@code HttpModule} class extends {@link AbstractModule} and installs
 * the necessary sub-modules for handling HTTP operations, such as Jsoup and REST modules.
 * </p>
 *
 * @version 1.0.0
 */
@RequiredArgsConstructor
public class HttpModule extends AbstractModule {

    private final SessionManager sessionManager;

    @Override
    protected void configure() {
        install(new JsoupModule(sessionManager));
        install(new RestModule(sessionManager));
    }
}
