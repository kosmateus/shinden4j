package com.github.kosmateus.shinden.common.enums;

import com.github.kosmateus.shinden.enums.Tag;
import com.github.kosmateus.shinden.i18n.Translatable;
import com.github.kosmateus.shinden.utils.Generator;
import com.github.kosmateus.shinden.utils.ResourceUtils;
import com.github.kosmateus.shinden.utils.jsoup.BaseDocumentMapper;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.kosmateus.shinden.constants.ShindenConstants.GENERIC_ID_KEY_MATCHER;
import static com.github.kosmateus.shinden.constants.ShindenConstants.GENERIC_ID_MATCHER;
import static com.github.kosmateus.shinden.constants.ShindenConstants.SHINDEN_URL;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Utility class for generating and validating enum values and translations for tags defined in the shinden.pl website.
 * <p>
 * The {@code TagsTestUtils} class provides a set of utility methods to generate Java enum values and translations
 * for various tag types used on the shinden.pl website. This class also provides methods to validate that the
 * enum values defined in the code correspond correctly to the tags available on the website.
 * </p>
 * <p>
 * The generation process involves:
 * <ul>
 *     <li>Fetching tag data from shinden.pl for each tag type, where tags are grouped by their type.</li>
 *     <li>Using the {@link TagsMapper} to parse the HTML data and extract details like tag IDs, keys, and translations.</li>
 *     <li>Generating Java enum values using {@link Generator#generateEnumTagValues(List, String)}.</li>
 *     <li>Generating YAML translation files using {@link Generator#generateTranslationYamlValues(List)}.</li>
 *     <li>Saving the generated content to files in the "target/generated" directory.</li>
 * </ul>
 * This class is designed as a utility helper and should not be instantiated. All methods are statically accessible.
 * </p>
 *
 * @version 1.0.0
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class TagsTestUtils {

    private static final TagsMapper tagsMapper = new TagsMapper();

    /**
     * Main method to generate enum values and translations for all enum classes implementing {@link Tag} based on data from shinden.pl.
     * <p>
     * This method scans the package "com.github.kosmateus.shinden" for all enum classes that implement
     * the {@link Tag} interface. For each identified enum class, it fetches data from the corresponding
     * webpage on shinden.pl, where each tag is grouped by its type. The data is parsed using the {@link TagsMapper}
     * to extract tag details, such as IDs, keys, and translations. The method then calls
     * {@link #generateEnumValuesAndTranslation(Class)} for each enum class to generate the enum values
     * and translations in a structured format.
     * </p>
     * <p>
     * The generated enum values are constructed in Java enum syntax using {@link Generator#generateEnumTagValues(List, String)},
     * and translations are formatted as YAML files using {@link Generator#generateTranslationYamlValues(List)}.
     * These results are saved to files in the "target/generated" directory.
     * </p>
     * <p>
     * Note that not all tag names and keys are consistently in English on shinden.pl, so some of the generated
     * enum names and translation keys might require manual adjustment. However, this process significantly speeds up
     * working with thousands of tags by automating the bulk of the task.
     * </p>
     *
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
        getTagClasses().forEach(TagsTestUtils::generateEnumValuesAndTranslation);
    }

    /**
     * Retrieves the URL suffix for the tag type associated with a given enum class.
     *
     * @param clazz the enum class implementing {@link Tag}
     * @param <T>   the type of the enum
     * @return the URL suffix representing the tag type
     */
    static <T extends Enum<T> & Tag> String tagUrl(Class<T> clazz) {
        return clazz.getEnumConstants()[0].getTagType();
    }

    /**
     * Gets all enum values for a given enum class.
     *
     * @param clazz the enum class implementing {@link Tag}
     * @param <T>   the type of the enum
     * @return an array of all enum values
     */
    static <T extends Enum<T> & Tag> T[] getEnumValues(Class<T> clazz) {
        return clazz.getEnumConstants();
    }

    /**
     * Converts an enum class name to kebab-case format.
     *
     * @param enumValue the enum class
     * @param <T>       the type of the enum
     * @return the kebab-case formatted name of the enum class
     */
    static <T extends Enum<T>> String toKebabCaseEnumName(Class<T> enumValue) {
        return StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(enumValue.getSimpleName()), '-').toLowerCase();
    }

    /**
     * Validates that all enum values defined in the provided class exist and match the corresponding
     * tags defined on the shinden.pl website.
     * <p>
     * This method fetches the tag data from the website and compares it against the enum values to ensure
     * there are no mismatches, missing IDs, or extra IDs.
     * </p>
     *
     * @param tClass the enum class to validate
     * @param <T>    the type of the enum
     */
    static <T extends Enum<T> & Tag & Translatable> void assertAllEnumValueExistAndAreValid(Class<T> tClass) {
        try {
            String tagUrl = tagUrl(tClass);
            T[] values = getEnumValues(tClass);
            Document tags = Jsoup.connect(SHINDEN_URL + "/" + tagUrl).execute().parse();
            List<Generator.TagData> tagData = tagsMapper.mapTags(tags, "/" + tagUrl);

            Map<Integer, String> enumMap = Stream.of(values)
                    .collect(Collectors.toMap(Tag::getId, Translatable::getTranslation));

            Set<String> mismatchedTranslations = tagData.stream()
                    .filter(tag -> !enumMap.containsKey(tag.getId()) || !enumMap.get(tag.getId()).equals(tag.getTranslation()))
                    .map(tag -> "ID: " + tag.getId() + ", Expected: " + enumMap.get(tag.getId()) + ", Found: " + tag.getTranslation())
                    .collect(Collectors.toSet());

            Set<Integer> extraIds = tagData.stream()
                    .map(Generator.TagData::getId)
                    .filter(id -> !enumMap.containsKey(id))
                    .collect(Collectors.toSet());

            Set<Integer> missingIds = enumMap.keySet().stream()
                    .filter(id -> tagData.stream().noneMatch(tag -> tag.getId().equals(id)))
                    .collect(Collectors.toSet());

            assertTrue(mismatchedTranslations.isEmpty() && extraIds.isEmpty() && missingIds.isEmpty(),
                    String.format("Mismatched Translations: %s, Missing IDs: %s, Extra IDs: %s",
                            mismatchedTranslations, missingIds, extraIds));
        } catch (IOException e) {
            log.error("Error asserting enum {}", tClass.getSimpleName(), e);
        }

    }

    /**
     * Generates enum values and translations for a given enum class and saves them to files.
     * <p>
     * This method fetches tag data from the corresponding webpage on shinden.pl, converts the data
     * into enum values and translations, and saves the results in the "target/generated" directory.
     * </p>
     *
     * @param clazz the enum class to generate values and translations for
     * @param <T>   the type of the enum
     */
    static <T extends Enum<T> & Tag & Translatable> void generateEnumValuesAndTranslation(Class<T> clazz) {
        try {
            String tagUrl = tagUrl(clazz);
            String tagName = toKebabCaseEnumName(clazz);
            Document genres = Jsoup.connect(SHINDEN_URL + "/" + tagUrl).execute().parse();
            List<Generator.TagData> tagData = tagsMapper.mapTags(genres, "/" + tagUrl);
            ResourceUtils.saveFile("target/generated/tags-" + tagName + "-enum.txt", Generator.generateEnumTagValues(tagData, "tags." + tagName));
            ResourceUtils.saveFile("target/generated/tags-" + tagName + "-translation.txt", Generator.generateTranslationYamlValues(tagData));
            log.info("Generated enum values and translation for tag {}", tagName);
        } catch (IOException e) {
            log.error("Error generating enum values and translation for tag {}", clazz.getSimpleName(), e);
        }
    }

    /**
     * Scans the package for all enum classes that implement the {@link Tag} interface.
     *
     * @param <T> the type of the enum
     * @return a list of classes that implement the {@link Tag} interface
     */
    @SuppressWarnings("unchecked")
    private static <T extends Enum<T> & Tag> List<Class<T>> getTagClasses() {
        try (ScanResult scanResult = new ClassGraph()
                .acceptPackages("com.github.kosmateus.shinden")
                .enableClassInfo()
                .scan()) {

            return (List<Class<T>>) (List<?>) scanResult.getAllClasses().stream()
                    .filter(ClassInfo::isEnum)
                    .map(ClassInfo::loadClass)
                    .filter(Tag.class::isAssignableFrom)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Mapper class for parsing and extracting tag data from HTML documents.
     */
    static class TagsMapper extends BaseDocumentMapper {

        /**
         * Maps HTML document elements to {@link Generator.TagData} instances.
         *
         * @param document the HTML document containing tag elements
         * @param tagUrl   the URL suffix for the tags
         * @return a list of {@link Generator.TagData} objects containing tag data
         */
        public List<Generator.TagData> mapTags(Document document, String tagUrl) {
            return mapper.with(document)
                    .selectFirst("ul.tags")
                    .select("li")
                    .mapTo(element -> Generator.TagData.of(
                            mapper.with(element).selectFirst("a").attr("href").pattern(GENERIC_ID_MATCHER.apply(tagUrl)).toInteger().orThrowWithCode("id"),
                            mapper.with(element).selectFirst("a").attr("href").pattern(GENERIC_ID_KEY_MATCHER.apply(tagUrl)).orThrowWithCode("key"),
                            mapper.with(element).selectFirst("a").text().orThrowWithCode("translation")
                    ))
                    .orThrowWithCode("tags");
        }

        @Override
        protected String getMapperCode() {
            return "tags";
        }
    }

    /**
     * Arguments provider for JUnit tests that supplies all tag-related enum classes.
     */
    static class TagEnumClassProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return getTagClasses().stream().map(Arguments::of);
        }
    }
}
