package com.github.kosmateus.shinden.http.rest;

import com.github.kosmateus.shinden.auth.SessionManager;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestModule extends AbstractModule {

    private final SessionManager sessionManager;

    @Override
    protected void configure() {
        bind(SessionManager.class).toInstance(sessionManager);
        bind(HttpRestClientExecutor.class).in(Singleton.class);
        bind(HttpRestClient.class).in(Singleton.class);
    }

}
