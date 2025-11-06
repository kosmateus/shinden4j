package com.github.kosmateus.shinden.i18n;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Custom {@link ResourceBundle.Control} that loads property files with UTF-8 encoding.
 * <p>
 * The {@code UTF8Control} class overrides the default behavior of {@link ResourceBundle.Control}
 * to ensure that property files are read using UTF-8 encoding. This is useful for internationalization
 * when the property files contain non-ASCII characters.
 * </p>
 *
 * @version 1.0.0
 */
class UTF8Control extends ResourceBundle.Control {

    @Override
    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IOException {
        String bundleName = toBundleName(baseName, locale);
        String resourceName = toResourceName(bundleName, "properties");
        ResourceBundle bundle = null;
        try (InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(loader.getResourceAsStream(resourceName)), StandardCharsets.UTF_8)) {
            bundle = new PropertyResourceBundle(reader);
        }
        return bundle;
    }
}
