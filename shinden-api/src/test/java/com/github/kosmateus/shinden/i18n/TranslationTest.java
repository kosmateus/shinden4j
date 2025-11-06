package com.github.kosmateus.shinden.i18n;

import com.github.kosmateus.shinden.BaseTest;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.kosmateus.shinden.utils.TestUtils.assertTranslations;

@DisplayName("Translation test")
public class TranslationTest extends BaseTest {

    @ParameterizedTest
    @ArgumentsSource(TranslationEnumClassProvider.class)
    @DisplayName("Should match enum values and translations")
    <T extends Enum<T> & Translatable> void validateTranslations(Class<T> tagClass) {
        assertTranslations(tagClass);
    }


    static class TranslationEnumClassProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            try (ScanResult scanResult = new ClassGraph()
                    .acceptPackages("com.github.kosmateus.shinden")
                    .enableClassInfo()
                    .scan()) {

                List<Class<?>> enumClasses = scanResult.getAllClasses().stream()
                        .filter(ClassInfo::isEnum)
                        .map(ClassInfo::loadClass)
                        .filter(Translatable.class::isAssignableFrom)
                        .collect(Collectors.toList());

                return enumClasses.stream().map(Arguments::of);
            }
        }
    }
}
