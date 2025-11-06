package com.github.kosmateus.shinden.i18n;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class for handling translations and internationalization.
 * <p>
 * The {@code TranslationUtil} class provides methods to initialize translation resources,
 * retrieve individual translations by key, and fetch groups of translations based on a prefix.
 * It supports localization by allowing the application to set a specific {@link Locale} and loads
 * translations from resource bundles using UTF-8 encoding.
 * </p>
 *
 * @version 1.0.0
 */
public final class TranslationUtil {

    private static Locale locale = Locale.getDefault();
    private static boolean initialized = false;
    private static Map<String, String> TRANSLATIONS;

    /**
     * Initializes the translation utility with the specified locale.
     * <p>
     * This method sets the locale and loads the corresponding translations.
     * It must be called before attempting to retrieve translations. If initialization
     * is attempted more than once, an {@link IllegalStateException} is thrown.
     * </p>
     *
     * @param locale the {@link Locale} to use for loading translations.
     * @throws IllegalStateException if the utility is already initialized.
     */
    public static void init(Locale locale) {
        if (initialized) {
            throw new IllegalStateException("TranslationUtil already initialized");
        }
        TranslationUtil.locale = locale;
        loadTranslations();
        initialized = true;
    }

    /**
     * Retrieves the translation associated with the given key.
     * <p>
     * If the utility has not been initialized, it will initialize with the default locale
     * before retrieving the translation.
     * </p>
     *
     * @param key the translation key.
     * @return the translated string associated with the key.
     */
    public static String getTranslation(String key) {
        if (!initialized) {
            init(Locale.getDefault());
        }
        return TRANSLATIONS.get(key);
    }

    /**
     * Retrieves a group of translations that share a common prefix.
     * <p>
     * This method filters the loaded translations by the specified prefix and returns
     * a map of translations with the prefix removed from the keys.
     * </p>
     *
     * @param prefix the prefix used to filter the translation keys.
     * @return a map of translations with keys stripped of the prefix.
     */
    public static Map<String, String> getTranslationGroup(String prefix) {
        if (!initialized) {
            init(Locale.getDefault());
        }
        return TRANSLATIONS.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(prefix))
                .collect(Collectors.toMap(
                        entry -> entry.getKey().substring(prefix.length() + 1),
                        Map.Entry::getValue
                ));
    }

    /**
     * Loads the translations for the current locale.
     * <p>
     * This method loads translations from a resource bundle named "translations"
     * using the current locale and UTF-8 encoding.
     * </p>
     */
    private static void loadTranslations() {
        Constructor constructor = new Constructor(new LoaderOptions()) {
            @Override
            protected Object constructObject(Node node) {
                if (node instanceof ScalarNode) {
                    return constructScalarAsString((ScalarNode) node);
                }
                return super.constructObject(node);
            }

            private String constructScalarAsString(ScalarNode node) {
                return node.getValue();
            }
        };
        Yaml yaml = new Yaml(constructor);
        List<String> fileNames = Arrays.asList("translation", "tags-translation").stream()
                .map(TranslationUtil::getTranslationFileName)
                .collect(Collectors.toList());

        TRANSLATIONS = new HashMap<>();

        for (String fileName : fileNames) {
            try (InputStream inputStream = TranslationUtil.class.getClassLoader().getResourceAsStream(fileName)) {
                if (inputStream == null) {
                    throw new RuntimeException("Translation file not found: " + fileName);
                }
                Map<String, Object> yamlMap = yaml.load(inputStream);
                TRANSLATIONS.putAll(flattenYamlMap(yamlMap));
            } catch (Exception e) {
                throw new RuntimeException("Failed to load translations from file: " + fileName, e);
            }
        }
    }


    private static String getTranslationFileName(String baseName) {
        String language = locale.getLanguage();
        String country = locale.getCountry();

        String[] fileNames = {
                baseName + "-" + language + "-" + country + ".yaml",
                baseName + "-" + language + ".yaml",
                baseName + ".yaml"
        };

        for (String fileName : fileNames) {
            if (TranslationUtil.class.getClassLoader().getResource(fileName) != null) {
                return fileName;
            }
        }

        return baseName + ".yaml";
    }

    private static Map<String, String> flattenYamlMap(Map<String, Object> yamlMap) {
        Map<String, String> flatMap = new HashMap<>();
        flattenYamlMapRecursive(yamlMap, "", flatMap);
        return flatMap;
    }

    private static void flattenYamlMapRecursive(Map<String, Object> yamlMap, String prefix, Map<String, String> flatMap) {
        for (Map.Entry<String, Object> entry : yamlMap.entrySet()) {
            String key = prefix.isEmpty() ? String.valueOf(entry.getKey()) : prefix + "." + String.valueOf(entry.getKey());
            Object value = entry.getValue();

            if (value instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> nestedMap = (Map<String, Object>) value;
                flattenYamlMapRecursive(nestedMap, key, flatMap);
            } else {
                flatMap.put(key, convertToString(value));
            }
        }
    }

    private static String convertToString(Object value) {
        if (value == null) {
            return "";
        } else if (value instanceof Boolean) {
            return ((Boolean) value) ? "true" : "false";
        } else {
            return value.toString();
        }
    }
}
