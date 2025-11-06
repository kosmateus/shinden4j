package com.github.kosmateus.shinden.utils;

import com.github.kosmateus.shinden.i18n.Translatable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.github.kosmateus.shinden.i18n.TranslationUtil.getTranslation;
import static org.assertj.core.api.Assertions.assertThat;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestUtils {

    public static <T extends Enum<T> & Translatable> void assertTranslations(Class<T> tClass) {
        for (T value : tClass.getEnumConstants()) {
            String translation = getTranslation(value.getTranslationKey());
            assertThat(translation)
                    .withFailMessage("Translation missing for %s.%s with key %s",
                            value.getDeclaringClass().getSimpleName(), value.name(), value.getTranslationKey())
                    .isNotBlank();
            assertThat(translation)
                    .withFailMessage("Translation for %s.%s is the same as the key: %s",
                            value.getDeclaringClass().getSimpleName(), value.name(), value.getTranslationKey())
                    .isNotEqualTo(value.getTranslationKey());
        }
    }
}
