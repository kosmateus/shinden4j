package com.github.kosmateus.shinden;

import com.github.kosmateus.shinden.anime.AnimeApi;
import com.github.kosmateus.shinden.auth.InMemorySessionManager;
import com.github.kosmateus.shinden.auth.SessionManager;
import com.github.kosmateus.shinden.i18n.TranslationUtil;
import com.github.kosmateus.shinden.login.LoginApi;
import com.github.kosmateus.shinden.user.UserApi;
import com.google.inject.Guice;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;

import java.util.Locale;

/**
 * The {@code ShindenApi} class serves as the main entry point for interacting with the Shinden API.
 *
 * <p>This class provides access to the login and user-related functionalities of the Shinden platform,
 * encapsulating the API's services behind simple, intuitive methods.</p>
 *
 * <p>The API client can be instantiated using the {@link #create(SessionManager)} or
 * {@link #create(SessionManager, Locale)} methods, which initialize the necessary dependencies
 * through Google Guice and set the appropriate locale for translations.</p>
 *
 * @version 1.0.0
 */
@RequiredArgsConstructor(onConstructor_ = @__(@Inject))
public class ShindenApi {

    private final LoginApi loginApi;
    private final UserApi userApi;
    private final AnimeApi animeApi;

    /**
     * Creates an instance of {@code ShindenApi} using the default locale and an in-memory session manager.
     *
     * <p>This method initializes the translation utilities with the system's default locale
     * and sets up the necessary dependencies using Google Guice.</p>
     *
     * @return an instance of {@code ShindenApi}
     */
    public static ShindenApi create() {
        return create(new InMemorySessionManager());
    }

    /**
     * Creates an instance of {@code ShindenApi} using the default locale.
     *
     * <p>This method initializes the translation utilities with the system's default locale
     * and sets up the necessary dependencies using Google Guice.</p>
     *
     * @param sessionManager the session manager to manage authentication sessions
     * @return an instance of {@code ShindenApi}
     */
    public static ShindenApi create(SessionManager sessionManager) {
        return create(sessionManager, Locale.getDefault());
    }

    /**
     * Creates an instance of {@code ShindenApi} using a specified locale.
     *
     * <p>This method initializes the translation utilities with the provided locale
     * and sets up the necessary dependencies using Google Guice.</p>
     *
     * @param sessionManager the session manager to manage authentication sessions
     * @param locale         the locale to be used for translations
     * @return an instance of {@code ShindenApi}
     */
    public static ShindenApi create(SessionManager sessionManager, Locale locale) {
        TranslationUtil.init(locale);
        return Guice.createInjector(new ShindenModule(sessionManager)).getInstance(ShindenApi.class);
    }

    /**
     * Provides access to the {@link LoginApi} for handling login operations.
     *
     * @return an instance of {@link LoginApi}
     */
    public LoginApi login() {
        return loginApi;
    }

    /**
     * Provides access to the {@link UserApi} for handling user-related operations.
     *
     * @return an instance of {@link UserApi}
     */
    public UserApi user() {
        return userApi;
    }

    /**
     * Provides access to the {@link AnimeApi} for handling anime-related operations.
     *
     * @return an instance of {@link AnimeApi}
     */
    public AnimeApi anime() {
        return animeApi;
    }
}
