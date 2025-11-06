package com.github.kosmateus.shinden.i18n;

/**
 * Interface for objects that can be translated based on a translation key.
 * <p>
 * The {@code Translatable} interface provides a contract for objects that support internationalization.
 * Implementing classes should provide a translation key, and this interface includes a default method
 * to retrieve the corresponding translation using a utility class.
 * </p>
 *
 * @version 1.0.0
 */
public interface Translatable {

    /**
     * Returns the translation key associated with this object.
     * <p>
     * The translation key is used to look up the translated text in the resource bundles.
     * </p>
     *
     * @return the translation key as a {@link String}.
     */
    String getTranslationKey();

    /**
     * Returns the translated text for this object.
     * <p>
     * This default method uses the translation key provided by {@link #getTranslationKey()}
     * to retrieve the corresponding translation from a resource bundle via {@link TranslationUtil#getTranslation(String)}.
     * </p>
     *
     * @return the translated text as a {@link String}.
     */
    default String getTranslation() {
        return TranslationUtil.getTranslation(getTranslationKey());
    }

}
