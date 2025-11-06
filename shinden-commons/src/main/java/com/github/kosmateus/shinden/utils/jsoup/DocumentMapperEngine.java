package com.github.kosmateus.shinden.utils.jsoup;

import com.github.kosmateus.shinden.utils.PatternMatcher;
import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Optional.ofNullable;

/**
 * The {@code DocumentMapperEngine} class is responsible for mapping elements from an HTML document
 * to various data types. It provides a flexible API for extracting and transforming data from
 * HTML elements based on selectors, attributes, patterns, and other criteria.
 *
 * <p>This class supports the mapping of various data types including {@link String}, {@link Long},
 * {@link Integer}, {@link Double}, {@link Float}, {@link Boolean}, {@link LocalDate}, and
 * {@link LocalDateTime}. It can also apply transformations like text replacements and pattern matching
 * during the mapping process.</p>
 *
 * <p>Users can extend the engine by providing custom type converters and exception handling mechanisms.</p>
 *
 * @version 1.0.0
 */
@SuppressWarnings("unchecked")
@RequiredArgsConstructor
public class DocumentMapperEngine {

    private static final Function<Pair<DateTimeFormatter, DateTimeFormatter>, Map<Class<?>, Function<String, ?>>> TYPE_MAPPERS = (dateTimeFormatters) -> ImmutableMap.of(
            String.class, Function.identity(),
            Long.class, value -> StringUtils.isNotBlank(value) ? Long.valueOf(clearForNumber(value)) : null,
            Integer.class, value -> StringUtils.isNotBlank(value) ? Integer.valueOf(clearForNumber(value)) : null,
            Double.class, value -> StringUtils.isNotBlank(value) ? Double.valueOf(clearForNumber(value)) : null,
            Float.class, value -> StringUtils.isNotBlank(value) ? Float.valueOf(clearForNumber(value)) : null,
            Boolean.class, Boolean::valueOf,
            LocalDate.class, s -> LocalDate.parse(s, dateTimeFormatters.getRight()),
            LocalDateTime.class, s -> LocalDateTime.parse(s, dateTimeFormatters.getLeft())
    );
    private final Function<Pair<String, Throwable>, Supplier<? extends RuntimeException>> exceptionSupplier;
    private final DocumentTextMapper textMapper;
    private final DocumentAttrMapper attrMapper;
    private final DocumentOwnTextMapper ownMapper;
    private final DocumentLocationMapper locationMapper;

    /**
     * Constructs a {@code DocumentMapperEngine} instance with default type mappers for common data types.
     *
     * @param localDateTimePattern the date-time pattern used for parsing {@link LocalDateTime}
     * @param localDatePattern     the date pattern used for parsing {@link LocalDate}
     * @param exceptionSupplier    function to supply exceptions when a mapping error occurs
     */
    public DocumentMapperEngine(String localDateTimePattern, String localDatePattern,
                                Function<Pair<String, Throwable>, Supplier<? extends RuntimeException>> exceptionSupplier) {
        Map<Class<?>, Function<String, ?>> typeConverters = TYPE_MAPPERS.apply(
                Pair.of(DateTimeFormatter.ofPattern(localDateTimePattern),
                        DateTimeFormatter.ofPattern(localDatePattern)));
        this.exceptionSupplier = exceptionSupplier;
        this.textMapper = new DocumentTextMapper(typeConverters);
        this.attrMapper = new DocumentAttrMapper(typeConverters);
        this.ownMapper = new DocumentOwnTextMapper(typeConverters);
        this.locationMapper = new DocumentLocationMapper(typeConverters);
    }

    /**
     * Constructs a {@code DocumentMapperEngine} instance with custom type mappers.
     *
     * @param localDateTimePattern the date-time pattern used for parsing {@link LocalDateTime}
     * @param localDatePattern     the date pattern used for parsing {@link LocalDate}
     * @param typeMappers          custom type mappers provided as a map
     * @param exceptionSupplier    function to supply exceptions when a mapping error occurs
     */
    public DocumentMapperEngine(String localDateTimePattern, String localDatePattern,
                                Map<Class<?>, Function<String, ?>> typeMappers,
                                Function<Pair<String, Throwable>, Supplier<? extends RuntimeException>> exceptionSupplier) {

        Map<Class<?>, Function<String, ?>> typeConverters = new HashMap<>();
        typeConverters.putAll(TYPE_MAPPERS.apply(
                Pair.of(DateTimeFormatter.ofPattern(localDateTimePattern),
                        DateTimeFormatter.ofPattern(localDatePattern))));
        typeConverters.putAll(typeMappers);
        this.exceptionSupplier = exceptionSupplier;
        this.textMapper = new DocumentTextMapper(typeConverters);
        this.attrMapper = new DocumentAttrMapper(typeConverters);
        this.ownMapper = new DocumentOwnTextMapper(typeConverters);
        this.locationMapper = new DocumentLocationMapper(typeConverters);
    }

    private static Element getElementOnIndex(Element element, String select, Integer selectSkip, Integer index) {
        Elements elements = subList(element.select(select), selectSkip);
        if (elements.size() > index) {
            return elements.get(index);
        }
        return null;
    }

    private static String clearForNumber(String stringNumber) {
        return stringNumber.replaceAll(" ", "").replaceAll(" ", "");
    }

    private static Elements subList(Elements elements, Integer skip) {
        if (skip != null) {
            return elements.size() > skip ? new Elements(elements.subList(skip, elements.size())) : new Elements();
        }
        return elements;
    }

    public WithElementStep with(Element document) {
        return new WithElementStep(document);
    }

    public WithDocumentStep with(Document document) {
        return new WithDocumentStep(document);
    }

    private <T> Optional<Element, T> text(Element document, String select, Integer selectSkip, PatternMatcher patternMatcher, Boolean keepNewLines,
                                          Class<T> tClass, Integer index) {
        return textMapper.parse(document, select, selectSkip, patternMatcher, keepNewLines, Collections.emptyList(), tClass, index);
    }

    private <T> Optional<Element, T> text(Element document, String select, Integer selectSkip, PatternMatcher patternMatcher, Boolean keepNewLines,
                                          List<Replacement> replacements, Class<T> tClass, Integer index) {
        return textMapper.parse(document, select, selectSkip, patternMatcher, keepNewLines, replacements, tClass, index);
    }

    private <T> Optional<Element, T> attr(Element document, String select, Integer selectSkip, String attr, PatternMatcher patternMatcher,
                                          Class<T> tClass, Integer index) {
        return attrMapper.parse(document, select, selectSkip, attr, patternMatcher, Collections.emptyList(), tClass, index);
    }

    private <T> Optional<Element, T> attr(Element document, String select, Integer selectSkip, String attr, PatternMatcher patternMatcher,
                                          List<Replacement> replacements, Class<T> tClass, Integer index) {
        return attrMapper.parse(document, select, selectSkip, attr, patternMatcher, replacements, tClass, index);
    }

    private <T> Optional<Element, T> ownText(Element document, String select, Integer selectSkip, PatternMatcher patternMatcher, Boolean keepNewLines,
                                             Class<T> tClass, Integer index) {
        return ownMapper.parse(document, select, selectSkip, patternMatcher, keepNewLines, Collections.emptyList(), tClass, index);
    }

    private <T> Optional<Element, T> ownText(Element document, String select, Integer selectSkip, PatternMatcher patternMatcher, Boolean keepNewLines,
                                             List<Replacement> replacements, Class<T> tClass, Integer index) {
        return ownMapper.parse(document, select, selectSkip, patternMatcher, keepNewLines, replacements, tClass, index);
    }

    private <T> Optional<Document, T> location(Document document, PatternMatcher patternMatcher,
                                               List<Replacement> replacements, Class<T> tClass) {
        return locationMapper.parse(document, patternMatcher, replacements, tClass);
    }

    private Optional<Element, Boolean> exists(Element document, String select, Integer selectSkip, Integer index) {
        if (index != null) {
            return new Optional<>(getElementOnIndex(document, select, selectSkip, index), Objects::nonNull);
        }
        return new Optional<>(document.selectFirst(select), Objects::nonNull);
    }

    @RequiredArgsConstructor
    private class DocumentAttrMapper {

        private final Map<Class<?>, Function<String, ?>> mappers;

        private <T> Optional<Element, T> parse(Element document, String select, Integer selectSkip, String attr,
                                               PatternMatcher patternMatcher, List<Replacement> replacements, Class<T> tClass, Integer index) {
            return ofNullable(mappers.get(tClass))
                    .map(mapper -> (Optional<Element, T>) getOptionValue(document, select, selectSkip, attr, patternMatcher, replacements, mapper, index))
                    .orElseThrow(() -> new IllegalArgumentException("Unsupported class: " + tClass.getName()));
        }

        private <T> Optional<Element, T> getOptionValue(Element document, String select, Integer selectSkip, String attr,
                                                        PatternMatcher patternMatcher, List<Replacement> replacements, Function<String, ?> converter, Integer index) {
            if (select == null) {
                return new Optional<>(document, element -> (T) getAttr(attr, patternMatcher, element, replacements, converter));
            }
            return new Optional<>(index != null ? getElementOnIndex(document, select, selectSkip, index) : document.selectFirst(select),
                    element -> (T) getAttr(attr, patternMatcher, element, replacements, converter));
        }

        private <T> Optional<String, ?> getAttr(String attr, PatternMatcher patternMatcher, Element element,
                                                List<Replacement> replacements,
                                                Function<String, T> mapper) {
            return new Optional<>(element.attr(attr),
                    attribute ->
                            patternMatcher != null
                                    ? new Optional<>(patternMatcher.getOrThrow(attribute.trim(), exceptionSupplier.apply(Pair.of("pattern." + patternMatcher.getPattern() + ".group." + patternMatcher.getGroup(), null))),
                                    foundText -> mapper.apply(replace(replacements, foundText).trim())
                            )
                                    : mapper.apply(replace(replacements, attribute).trim()));
        }

        private String replace(List<Replacement> replacements, String foundText) {
            return replacements.stream()
                    .reduce(foundText,
                            (s, replacement) -> s.replaceAll(replacement.getRegex(), replacement.getReplacement()),
                            (s1, s2) -> s2);
        }
    }

    @RequiredArgsConstructor
    private class DocumentTextMapper {

        private final Map<Class<?>, Function<String, ?>> mappers;

        private <T> Optional<Element, T> parse(Element document, String select, Integer selectSkip, PatternMatcher patternMatcher, Boolean keepNewLines, List<Replacement> replacements,
                                               Class<T> tClass, Integer index) {
            return ofNullable(mappers.get(tClass))
                    .map(mapper -> (Optional<Element, T>) getOptionalValue(document, select, selectSkip, patternMatcher, keepNewLines,
                            replacements, mapper, index))
                    .orElseThrow(() -> new IllegalArgumentException("Unsupported class: " + tClass.getName()));
        }

        private <T> Optional<Element, T> getOptionalValue(Element document, String select, Integer selectSkip,
                                                          PatternMatcher patternMatcher, Boolean keepNewLines, List<Replacement> replacements, Function<String, T> mapper, Integer index) {
            if (select == null) {
                return new Optional<>(document, text -> getText(text, patternMatcher, keepNewLines, replacements, mapper));
            }
            return new Optional<>(index != null ? getElementOnIndex(document, select, selectSkip, index) : document.selectFirst(select),
                    element -> getText(element, patternMatcher, keepNewLines, replacements, mapper));
        }

        private <T> T getText(Element element, PatternMatcher patternMatcher, Boolean keepNewLines, List<Replacement> replacements,
                              Function<String, T> mapper) {
            String text = getElementText(element, keepNewLines);
            if (StringUtils.isNotBlank(text)) {
                return getText(patternMatcher, replacements, mapper, text);
            }
            return null;
        }

        private String getElementText(Element element, Boolean keepNewLines) {
            if (keepNewLines) {
                element.select("br").forEach(br -> br.append("\\n"));
                return Arrays.stream(element.text().split("\\\\n"))
                        .map(String::trim)
                        .collect(Collectors.joining(System.lineSeparator()));
            }
            return element.text();
        }

        private <T> T getText(PatternMatcher patternMatcher, List<Replacement> replacements, Function<String, T> mapper,
                              String text) {
            if (patternMatcher != null) {
                return (T) new Optional<>(
                        patternMatcher.getOrThrow(text.trim(), exceptionSupplier.apply(Pair.of("pattern." + patternMatcher.getPattern() + ".group." + patternMatcher.getGroup(), null))),
                        foundText -> mapper.apply(replace(replacements, foundText).trim())
                );
            } else {
                return mapper.apply(replace(replacements, text).trim());
            }
        }

        private String replace(List<Replacement> replacements, String foundText) {
            return replacements.stream()
                    .reduce(foundText,
                            (s, replacement) -> s.replaceAll(replacement.getRegex(), replacement.getReplacement()),
                            (s1, s2) -> s2);
        }
    }

    @RequiredArgsConstructor
    private class DocumentOwnTextMapper {

        private final Map<Class<?>, Function<String, ?>> mappers;

        private <T> Optional<Element, T> parse(Element document, String select, Integer selectSkip, PatternMatcher patternMatcher, Boolean keepNewLines,
                                               List<Replacement> replacements,
                                               Class<T> tClass, Integer index) {
            return ofNullable(mappers.get(tClass))
                    .map(mapper -> (Optional<Element, T>) getOptionalValue(document, select, selectSkip, patternMatcher, keepNewLines,
                            replacements, mapper, index))
                    .orElseThrow(() -> new IllegalArgumentException("Unsupported class: " + tClass.getName()));
        }

        private <T> Optional<Element, T> getOptionalValue(Element document, String select, Integer selectSkip,
                                                          PatternMatcher patternMatcher, Boolean keepNewLines, List<Replacement> replacements, Function<String, T> mapper, Integer index) {
            if (select == null) {
                return new Optional<>(document, text -> getText(text, patternMatcher, keepNewLines, replacements, mapper));
            }
            return new Optional<>(index != null ? getElementOnIndex(document, select, selectSkip, index) : document.selectFirst(select),
                    element -> getText(element, patternMatcher, keepNewLines, replacements, mapper));
        }

        private <T> T getText(Element element, PatternMatcher patternMatcher, Boolean keepNewLines, List<Replacement> replacements,
                              Function<String, T> mapper) {
            String text = getElementText(element, keepNewLines);
            if (StringUtils.isNotBlank(text)) {
                return getText(patternMatcher, replacements, mapper, text);
            }
            return null;
        }

        private String getElementText(Element element, Boolean keepNewLines) {
            if (keepNewLines) {
                element.select("br").forEach(br -> br.append("\\n"));
                return Arrays.stream(element.text().split("\\\\n"))
                        .map(String::trim)
                        .collect(Collectors.joining(System.lineSeparator()));
            }
            return element.ownText();
        }


        private <T> T getText(PatternMatcher patternMatcher, List<Replacement> replacements, Function<String, T> mapper,
                              String text) {
            if (patternMatcher != null) {
                return (T) new Optional<>(
                        patternMatcher.getOrThrow(text.trim(), exceptionSupplier.apply(Pair.of("pattern." + patternMatcher.getPattern() + ".group." + patternMatcher.getGroup(), null))),
                        foundText -> mapper.apply(replace(replacements, foundText).trim())
                );
            } else {
                return mapper.apply(replace(replacements, text).trim());
            }
        }

        private String replace(List<Replacement> replacements, String foundText) {
            return replacements.stream()
                    .reduce(foundText,
                            (s, replacement) -> s.replaceAll(replacement.getRegex(), replacement.getReplacement()),
                            (s1, s2) -> s2);
        }
    }

    @RequiredArgsConstructor
    private class DocumentLocationMapper {

        private final Map<Class<?>, Function<String, ?>> mappers;

        private <T> Optional<Document, T> parse(Document document, PatternMatcher patternMatcher,
                                                List<Replacement> replacements, Class<T> tClass) {
            return ofNullable(mappers.get(tClass))
                    .map(mapper -> (Optional<Document, T>) getOptionalValue(document, patternMatcher, replacements,
                            mapper))
                    .orElseThrow(() -> new IllegalArgumentException("Unsupported class: " + tClass.getName()));
        }

        private <T> Optional<Document, T> getOptionalValue(Document document, PatternMatcher patternMatcher,
                                                           List<Replacement> replacements, Function<String, T> mapper) {
            return new Optional<>(document, text -> getText(text, patternMatcher, replacements, mapper));
        }

        private <T> T getText(Document document, PatternMatcher patternMatcher, List<Replacement> replacements,
                              Function<String, T> mapper) {
            String text = document.location();
            if (StringUtils.isNotBlank(text)) {
                return getText(patternMatcher, replacements, mapper, text);
            }
            return null;
        }

        private <T> T getText(PatternMatcher patternMatcher, List<Replacement> replacements, Function<String, T> mapper,
                              String text) {
            if (patternMatcher != null) {
                return (T) new Optional<>(
                        patternMatcher.getOrThrow(text.trim(), exceptionSupplier.apply(Pair.of("pattern." + patternMatcher.getPattern() + ".group." + patternMatcher.getGroup(), null))),
                        foundText -> mapper.apply(replace(replacements, foundText).trim())
                );
            } else {
                return mapper.apply(replace(replacements, text).trim());
            }
        }

        private String replace(List<Replacement> replacements, String foundText) {
            return replacements.stream()
                    .reduce(foundText,
                            (s, replacement) -> s.replaceAll(replacement.getRegex(), replacement.getReplacement()),
                            (s1, s2) -> s2);
        }
    }

    /**
     * Represents a step in the mapping process that operates on a specific HTML element.
     * <p>
     * The {@code WithElementStep} class provides methods to interact with an HTML {@link Element} by
     * allowing selection, extraction, and mapping of its attributes and text content. This class serves as a base
     * for performing various operations on an element within an HTML document.
     * </p>
     *
     * @version 1.0.0
     */
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public class WithElementStep {

        /**
         * The HTML element on which the operations will be performed.
         */
        protected final Element element;

        /**
         * Selects the first matching element based on the provided CSS selector.
         * <p>
         * This method returns a {@link SelectFirstStep} that allows further operations on the first element
         * matching the given CSS selector within the current element.
         * </p>
         *
         * @param select the CSS selector to apply for selecting the first matching element
         * @return a {@link SelectFirstStep} instance for further operations on the selected element
         */
        public SelectFirstStep selectFirst(String select) {
            return new SelectFirstStep(element, select);
        }

        /**
         * Selects all matching elements based on the provided CSS selector.
         * <p>
         * This method returns a {@link SelectStep} that allows further operations on all elements
         * matching the given CSS selector within the current element.
         * </p>
         *
         * @param select the CSS selector to apply for selecting all matching elements
         * @return a {@link SelectStep} instance for further operations on the selected elements
         */
        public SelectStep select(String select) {
            return new SelectStep(element, null, select, 0);
        }

        public SelectStep select(String select, Integer skip) {
            return new SelectStep(element, null, select, skip);
        }

        /**
         * Extracts and maps the text content of the current element.
         * <p>
         * This method returns a {@link TextStep} that allows for mapping the text content of the current element.
         * </p>
         *
         * @return a {@link TextStep} instance for further operations on the text content of the element
         */
        public TextStep text() {
            return new TextStep(element, null, 0, false, null);
        }

        /**
         * Extracts and maps the value of a specified attribute of the current element.
         * <p>
         * This method returns an {@link AttrStep} that allows for mapping the value of the specified attribute
         * of the current element.
         * </p>
         *
         * @param attr the name of the attribute to extract and map
         * @return an {@link AttrStep} instance for further operations on the attribute value
         */
        public AttrStep attr(String attr) {
            return new AttrStep(element, null, 0, attr, null);
        }
    }


    /**
     * Represents a step in the mapping process that operates on an entire HTML document.
     * <p>
     * The {@code WithDocumentStep} class extends {@link WithElementStep} to provide additional functionality
     * specific to handling an entire {@link Document}. It allows for operations such as retrieving the document's
     * location (URL).
     * </p>
     *
     * @version 1.0.0
     */
    public class WithDocumentStep extends WithElementStep {

        /**
         * Constructs a {@code WithDocumentStep} instance for the given HTML element.
         * <p>
         * This constructor is private and is intended to be used internally within the mapping process.
         * It initializes the step with the provided {@link Element}, which is expected to be a {@link Document}.
         * </p>
         *
         * @param element the HTML {@link Element} to be wrapped, typically a {@link Document}
         */
        private WithDocumentStep(Element element) {
            super(element);
        }

        /**
         * Retrieves the document's location (URL) for mapping purposes.
         * <p>
         * This method returns a {@link LocationStep}, allowing the user to map or extract the document's location
         * (i.e., its URL).
         * </p>
         *
         * @return a {@link LocationStep} instance for further operations on the document's location
         */
        public LocationStep location() {
            return new LocationStep((Document) element);
        }
    }


    /**
     * Represents a step in the mapping process focused on selecting the first HTML element that matches a CSS selector.
     * <p>
     * The {@code SelectFirstStep} class provides methods to perform further operations such as selecting additional elements,
     * mapping attributes, text content, or checking the existence of the selected element.
     * </p>
     *
     * @version 1.0.0
     */
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public class SelectFirstStep {

        private final Element document;
        private final String select;

        /**
         * Chains another selection using a CSS selector relative to the current selection.
         * <p>
         * This method allows you to refine or expand your selection by chaining another selector.
         * It returns a {@link SelectStep} instance that represents this new selection.
         * </p>
         *
         * @param select the CSS selector to apply relative to the current selection
         * @return a {@link SelectStep} instance for further operations on the chained selection
         */
        public SelectStep select(String select) {
            return new SelectStep(document, this.select, select, 0);
        }

        public SelectStep select(String select, Integer skip) {
            return new SelectStep(document, this.select, select, skip);
        }

        /**
         * Maps the selected element's attribute to a specified type.
         * <p>
         * This method creates an {@link AttrStep} to map the attribute of the first selected element.
         * </p>
         *
         * @param attr the attribute name to be mapped
         * @return an {@link AttrStep} instance for further mapping and transformation steps
         */
        public AttrStep attr(String attr) {
            return new AttrStep(document, select, 0, attr, null);
        }

        /**
         * Maps the text content of the selected element.
         * <p>
         * This method creates a {@link TextStep} to map the text content of the first selected element.
         * </p>
         *
         * @return a {@link TextStep} instance for further mapping and transformation steps
         */
        public TextStep text() {
            return new TextStep(document, select, 0, false, null);
        }

        /**
         * Maps the own text content (direct text content within an element, excluding child elements) of the selected element.
         * <p>
         * This method creates an {@link OwnTextStep} to map the own text content of the first selected element.
         * </p>
         *
         * @return an {@link OwnTextStep} instance for further mapping and transformation steps
         */
        public OwnTextStep ownText() {
            return new OwnTextStep(document, select, 0, false, null);
        }

        /**
         * Checks if the selected element exists.
         * <p>
         * This method returns an {@link Optional} indicating whether the first element matching the CSS selector exists.
         * </p>
         *
         * @return an {@link Optional} containing {@code true} if the element exists, or {@code false} otherwise
         */
        public boolean exists() {
            return DocumentMapperEngine.this.exists(document, select, 0, null).orElse(false);
        }

    }


    /**
     * Represents a step in the mapping process that allows selecting HTML elements using CSS selectors.
     * <p>
     * The {@code SelectStep} class provides methods to select elements based on a CSS selector,
     * map these selections to a specific data type, or perform further operations like collecting
     * or chaining selections.
     * </p>
     *
     * @version 1.0.0
     */
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public class SelectStep {

        private final Element document;
        private final String selectFirst;
        private final String select;
        private final Integer selectSkip;

        /**
         * Maps the selected elements to a list of a specified type.
         * <p>
         * This method selects elements based on the provided CSS selector and maps them to a list of the specified type.
         * The mapping is done using the provided {@code mapper} function, which processes each selected element.
         * If {@code selectFirst} is specified, it will narrow down the initial selection before applying the final selector.
         * </p>
         *
         * @param <T>    the type to which the elements should be mapped
         * @param mapper a function to map each selected element to the desired type
         * @return an {@link Optional} containing the mapped list, or empty if the selection fails
         */
        public <T> Optional<Element, List<T>> mapTo(Function<Element, T> mapper) {
            if (StringUtils.isNotBlank(selectFirst)) {
                return new Optional<>(document.selectFirst(selectFirst),
                        element -> subList(element.select(select), selectSkip).stream().map(mapper).collect(Collectors.toList()));
            }
            return new Optional<>(document, element -> subList(element.select(select), selectSkip).stream().map(mapper).collect(Collectors.toList()));
        }

        /**
         * Selects an element from the current selection based on its index.
         * <p>
         * This method returns a {@link SelectWithIndexStep} instance, allowing further operations to be performed
         * on the element at the specified index within the selected elements.
         * </p>
         *
         * @param index the index of the element to select
         * @return a {@link SelectWithIndexStep} instance for further operations on the selected element
         */
        public SelectWithIndexStep get(Integer index) {
            return new SelectWithIndexStep(document, selectFirst, select, selectSkip, index);
        }

        /**
         * Collects multiple elements based on the selection and prepares them for further mapping.
         * <p>
         * This method returns a {@link CollectSelectStep} instance, which can be used to collect
         * and map the selected elements to a specific type.
         * </p>
         *
         * @return a {@link CollectSelectStep} instance for collecting and mapping the selected elements
         */
        public CollectSelectStep collect() {
            return new CollectSelectStep(document, selectFirst, select, selectSkip);
        }

        /**
         * Chains the current selection with an additional selector.
         * <p>
         * This method allows chaining additional CSS selectors to refine or expand the selection.
         * It returns a {@link MultipleSelectStep} instance that manages the combined selectors.
         * </p>
         *
         * @return a {@link MultipleSelectStep} instance that includes the chained selectors
         */
        public MultipleSelectStep and() {
            return new MultipleSelectStep(document, selectFirst, Arrays.asList(Pair.of(select, selectSkip)));
        }
    }


    /**
     * Represents a step in the mapping process that allows selecting multiple HTML elements using different CSS selectors.
     * <p>
     * The {@code MultipleSelectStep} class provides methods to select elements based on multiple CSS selectors,
     * map these selections to a specific data type, and chain additional selections.
     * </p>
     *
     * @version 1.0.0
     */
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public class MultipleSelectStep {

        private final Element document;
        private final String selectFirst;
        private final List<Pair<String, Integer>> selections;

        /**
         * Maps the selected elements to a list of a specified type.
         * <p>
         * This method selects elements based on multiple CSS selectors and maps them to a list of the specified type.
         * The mapping is done using the provided {@code mapper} function, which processes the selected elements
         * stored in a map keyed by their respective CSS selectors.
         * </p>
         *
         * @param <T>    the type to which the list of elements should be mapped
         * @param mapper a function to map the selected elements to the desired type
         * @return an {@link Optional} containing the mapped list, or empty if mapping fails
         */
        public <T> Optional<Element, List<T>> mapTo(Function<Map<String, Element>, T> mapper) {
            if (StringUtils.isNotBlank(selectFirst)) {
                return new Optional<>(document.selectFirst(selectFirst), element -> mapToList(mapper, element));
            }
            return new Optional<>(document, element -> mapToList(mapper, element));
        }

        /**
         * Chains additional selections using the same initial selector.
         * <p>
         * This method allows chaining additional selections by creating a new {@code MultipleSelectStep} instance
         * with the same initial selector and the current list of selections.
         * </p>
         *
         * @return a new {@link MultipleSelectStep} instance with the chained selections
         */
        public MultipleSelectStep and() {
            return new MultipleSelectStep(document, selectFirst, selections);
        }

        /**
         * Adds a new CSS selector to the selection list.
         * <p>
         * This method adds a new selector to the list of selectors and returns a new {@code MultipleSelectStep} instance
         * with the updated list.
         * </p>
         *
         * @param select the new CSS selector to add
         * @return a new {@link MultipleSelectStep} instance with the added selector
         */
        public MultipleSelectStep select(String select) {
            List<Pair<String, Integer>> newSelect = new ArrayList<>(this.selections);
            newSelect.add(Pair.of(select, 0));
            return new MultipleSelectStep(document, selectFirst, newSelect);
        }

        public MultipleSelectStep select(String select, Integer skip) {
            List<Pair<String, Integer>> newSelect = new ArrayList<>(this.selections);
            newSelect.add(Pair.of(select, skip));
            return new MultipleSelectStep(document, selectFirst, newSelect);
        }

        /**
         * Maps the selected elements to a list using the provided mapping function.
         * <p>
         * This method processes the selected elements and organizes them into a list of maps,
         * where each map corresponds to one element in the selection. The mapping function is then applied to
         * each map, converting it to the desired output type.
         * </p>
         *
         * @param <T>     the type to which each map of selected elements should be converted
         * @param mapper  a function that converts a map of selected elements to the desired type
         * @param element the root element to apply the selection to
         * @return a list of mapped results
         */
        private <T> List<T> mapToList(Function<Map<String, Element>, T> mapper, Element element) {
            Map<String, Elements> collect = selections.stream()
                    .map(select -> Pair.of(select.getLeft(), subList(element.select(select.getLeft()), select.getRight())))
                    .collect(Collectors.toMap(Pair::getKey, Pair::getValue));

            List<Map<String, Element>> list = IntStream.range(0, collect.values().stream().mapToInt(Elements::size).max().orElse(0))
                    .mapToObj(i -> {
                                Map<String, Element> map = new HashMap<>();
                                collect.forEach((select, elements) -> {
                                    if (elements.size() > i) {
                                        map.put(select, elements.get(i));
                                    } else {
                                        map.put(select, null);
                                    }
                                });
                                return map;
                            }
                    ).collect(Collectors.toList());
            return list.stream().map(mapper).collect(Collectors.toList());
        }
    }


    /**
     * Represents a step in the mapping process that allows collecting a list of HTML elements based on a CSS selector.
     * <p>
     * The {@code CollectSelectStep} class provides a method to collect elements that match a given selector,
     * and then map those elements to a specific data type using a provided function.
     * </p>
     *
     * @version 1.0.0
     */
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public class CollectSelectStep {

        private final Element document;
        private final String selectFirst;
        private final String select;
        private final Integer selectSkip;

        /**
         * Collects a list of HTML elements based on the provided selector and maps them to a specific data type.
         * <p>
         * This method collects elements matching the {@code select} CSS selector. If {@code selectFirst} is specified,
         * it will first narrow down the selection using that selector before applying the final selector.
         * The resulting list of elements is then mapped to the specified type using the provided {@code mapper} function.
         * </p>
         *
         * @param <T>    the type to which the list of elements should be mapped
         * @param mapper a function to map the list of elements to the desired type
         * @return an {@link Optional} containing the mapped value, or empty if mapping fails
         */
        public <T> Optional<List<Element>, T> mapTo(Function<List<Element>, T> mapper) {
            if (StringUtils.isNotBlank(selectFirst)) {
                return new Optional<>(
                        new Optional<>(
                                document.selectFirst(selectFirst),
                                element -> subList(element.select(select), selectSkip)
                        ),
                        mapper
                );
            }
            return new Optional<>(subList(document.select(select), selectSkip), mapper);
        }
    }


    /**
     * Represents a step in the mapping process that allows selection of an HTML element based on a CSS selector and index.
     * <p>
     * The {@code SelectWithIndexStep} class provides methods to further refine the selection by mapping attributes,
     * text content, or determining the existence of the selected element within the HTML structure.
     * </p>
     *
     * @version 1.0.0
     */
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public class SelectWithIndexStep {

        private final Element document;
        private final String selectFirst;
        private final String select;
        private final Integer selectSkip;
        private final Integer index;

        /**
         * Maps the selected element's attribute to the specified type.
         * <p>
         * This method creates an {@link AttrStep} to map the attribute of the selected element.
         * If {@code selectFirst} is specified, it will be used to narrow down the initial selection.
         * </p>
         *
         * @param attr the attribute name to be mapped
         * @return an {@link AttrStep} instance for further mapping and transformation steps
         */
        public AttrStep attr(String attr) {
            if (selectFirst != null) {
                return new AttrStep(document.selectFirst(selectFirst), select, selectSkip, attr, index);
            }
            return new AttrStep(document, select, selectSkip, attr, index);
        }

        /**
         * Maps the text content of the selected element.
         * <p>
         * This method creates a {@link TextStep} to map the text content of the selected element.
         * If {@code selectFirst} is specified, it will be used to narrow down the initial selection.
         * </p>
         *
         * @return a {@link TextStep} instance for further mapping and transformation steps
         */
        public TextStep text() {
            if (selectFirst != null) {
                return new TextStep(document.selectFirst(selectFirst), select, selectSkip, false, index);
            }
            return new TextStep(document, select, selectSkip, false, index);
        }

        /**
         * Maps the own text content (direct text content within an element, excluding child elements) of the selected element.
         * <p>
         * This method creates an {@link OwnTextStep} to map the own text content of the selected element.
         * If {@code selectFirst} is specified, it will be used to narrow down the initial selection.
         * </p>
         *
         * @return an {@link OwnTextStep} instance for further mapping and transformation steps
         */
        public OwnTextStep ownText() {
            if (selectFirst != null) {
                return new OwnTextStep(document.selectFirst(selectFirst), select, selectSkip, false, index);
            }
            return new OwnTextStep(document, select, selectSkip, false, index);
        }

        /**
         * Checks if the selected element exists.
         * <p>
         * This method returns an {@link Optional} indicating whether the selected element exists
         * based on the provided CSS selector and index. If {@code selectFirst} is specified,
         * it will be used to narrow down the initial selection before applying the index.
         * </p>
         *
         * @return an {@link Optional} containing {@code true} if the element exists, or {@code false} otherwise
         */
        public Optional<Element, Boolean> exists() {
            if (selectFirst != null) {
                return DocumentMapperEngine.this.exists(document.selectFirst(selectFirst), select, selectSkip, index);
            }
            return DocumentMapperEngine.this.exists(document, select, selectSkip, index);
        }
    }


    /**
     * Represents a step in the mapping process focused on extracting and mapping the location (URL) of an HTML document.
     * <p>
     * The {@code LocationStep} class extends {@link MapStep} and provides functionality for mapping the URL of an HTML
     * {@link Document} to various data types. It also allows applying pattern matching on the document's location.
     * </p>
     *
     * @version 1.0.0
     */
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public class LocationStep extends MapStep {

        private final Document document;

        /**
         * Applies a pattern matcher to the document's location.
         * <p>
         * This method allows specifying a {@link PatternMatcher} that can be used to extract and match patterns
         * within the document's URL.
         * </p>
         *
         * @param patternMatcher the {@link PatternMatcher} to apply to the document's location
         * @return a {@link LocationPatternStep} instance for further mapping and transformation steps
         */
        public LocationPatternStep pattern(PatternMatcher patternMatcher) {
            return new LocationPatternStep(document, patternMatcher, new ArrayList<>());
        }

        /**
         * Maps the document's location (URL) to the specified data type.
         * <p>
         * This method attempts to map the document's URL to a specified data type using the existing mapping logic.
         * </p>
         *
         * @param <T>    the type to which the location should be mapped
         * @param tClass the {@link Class} representing the type
         * @return an {@link Optional} containing the mapped value, or empty if mapping fails
         */
        public <T> Optional<Document, T> mapTo(Class<T> tClass) {
            return DocumentMapperEngine.this.location(document, null, Collections.emptyList(), tClass);
        }
    }


    /**
     * Abstract class representing a step in the mapping process that allows conversion
     * of HTML elements to various data types.
     * <p>
     * The {@code MapStep} class provides methods to map the text content of an HTML element
     * to common data types such as {@link Long}, {@link Integer}, {@link Double}, {@link Float},
     * {@link Boolean}, {@link LocalDate}, and {@link LocalDateTime}. It also provides methods
     * for retrieving the mapped value with error handling options.
     * </p>
     *
     * @version 1.0.0
     */
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public abstract class MapStep {

        /**
         * Maps the text content of an HTML element to a {@link Long} value.
         *
         * @return an {@link Optional} containing the mapped {@link Long} value, or empty if mapping fails.
         */
        public Optional<? extends Element, Long> toLong() {
            return mapTo(Long.class);
        }

        /**
         * Maps the text content of an HTML element to a specified data type.
         * <p>
         * This method must be implemented by subclasses to define how the mapping is performed.
         * </p>
         *
         * @param <T>    the type to which the content should be mapped
         * @param tClass the {@link Class} representing the type
         * @return an {@link Optional} containing the mapped value, or empty if mapping fails.
         */
        public abstract <T> Optional<? extends Element, T> mapTo(Class<T> tClass);

        /**
         * Maps the text content of an HTML element to an {@link Integer} value.
         *
         * @return an {@link Optional} containing the mapped {@link Integer} value, or empty if mapping fails.
         */
        public Optional<? extends Element, Integer> toInteger() {
            return mapTo(Integer.class);
        }

        /**
         * Maps the text content of an HTML element to a {@link Double} value.
         *
         * @return an {@link Optional} containing the mapped {@link Double} value, or empty if mapping fails.
         */
        public Optional<? extends Element, Double> toDouble() {
            return mapTo(Double.class);
        }

        /**
         * Maps the text content of an HTML element to a {@link Float} value.
         *
         * @return an {@link Optional} containing the mapped {@link Float} value, or empty if mapping fails.
         */
        public Optional<? extends Element, Float> toFloat() {
            return mapTo(Float.class);
        }

        /**
         * Maps the text content of an HTML element to a {@link Boolean} value.
         *
         * @return an {@link Optional} containing the mapped {@link Boolean} value, or empty if mapping fails.
         */
        public Optional<? extends Element, Boolean> toBoolean() {
            return mapTo(Boolean.class);
        }

        /**
         * Maps the text content of an HTML element to a {@link LocalDate} value.
         *
         * @return an {@link Optional} containing the mapped {@link LocalDate} value, or empty if mapping fails.
         */
        public Optional<? extends Element, LocalDate> toLocalDate() {
            return mapTo(LocalDate.class);
        }

        /**
         * Maps the text content of an HTML element to a {@link LocalDateTime} value.
         *
         * @return an {@link Optional} containing the mapped {@link LocalDateTime} value, or empty if mapping fails.
         */
        public Optional<? extends Element, LocalDateTime> toLocalDateTime() {
            return mapTo(LocalDateTime.class);
        }

        /**
         * Retrieves the mapped value as a {@link String} or throws an exception with the specified error code.
         * <p>
         * This method attempts to map the text content to a {@link String}. If mapping fails, it throws an exception
         * with the provided error code.
         * </p>
         *
         * @param errorCode the error code to use when throwing an exception
         * @return the mapped {@link String} value
         * @throws RuntimeException if the value cannot be mapped
         */
        public String orThrowWithCode(String errorCode) {
            return mapTo(String.class).orThrowWithCode(errorCode);
        }

        /**
         * Retrieves the mapped value as a {@link String} or returns a default value if mapping fails.
         * <p>
         * This method attempts to map the text content to a {@link String}. If mapping fails, it returns
         * the provided default value.
         * </p>
         *
         * @param defaultValue the default value to return if mapping fails
         * @return the mapped {@link String} value or the default value
         */
        public String orElse(String defaultValue) {
            return mapTo(String.class).orElse(defaultValue);
        }

        public String orNull() {
            return mapTo(String.class).orElse(null);
        }
    }

    /**
     * Represents a step in the mapping process focused on extracting and manipulating the own text content of an HTML element.
     * <p>
     * The {@code OwnTextStep} class provides methods for replacing text, handling line breaks, applying patterns, and mapping the
     * own text content (direct text content within an element, excluding child elements) to a specific data type.
     * </p>
     *
     * @version 1.0.0
     */
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public class OwnTextStep extends MapStep {

        private final Element document;
        private final String select;
        private final Integer selectSkip;
        private final Boolean keepNewLines;
        private final Integer index;

        /**
         * Replaces text in the selected element's own text content based on the provided regular expression and replacement.
         * <p>
         * This method creates a {@link TextReplaceStep} to perform text replacements in the own text content of the element.
         * </p>
         *
         * @param regex       the regular expression used to find the text to be replaced
         * @param replacement the replacement text to substitute for the matched text
         * @return a {@link TextReplaceStep} instance for further text replacement and mapping operations
         */
        public OwnTextReplaceStep replace(String regex, String replacement) {
            List<Replacement> replacements = new ArrayList<>();
            replacements.add(new Replacement(regex, replacement));
            return new OwnTextReplaceStep(document, select, selectSkip, null, keepNewLines, replacements, index);
        }

        /**
         * Preserves line breaks in the own text content of the selected element.
         * <p>
         * This method returns a new {@link OwnTextStep} instance with the {@code keepNewLines} flag set to {@code true},
         * ensuring that line breaks are retained when extracting the text content.
         * </p>
         *
         * @return a new {@link OwnTextStep} instance with line breaks preserved
         */
        public OwnTextStep keepNewLines() {
            return new OwnTextStep(document, select, selectSkip, true, index);
        }

        /**
         * Applies a pattern matcher to the own text content of the selected element.
         * <p>
         * This method creates an {@link OwnTextPatternStep} that allows pattern-based extraction and transformation of the text content.
         * </p>
         *
         * @param patternMatcher the {@link PatternMatcher} to apply to the text content
         * @return an {@link OwnTextPatternStep} instance for further pattern-based mapping operations
         */
        public OwnTextPatternStep pattern(PatternMatcher patternMatcher) {
            return new OwnTextPatternStep(document, select, selectSkip, patternMatcher, keepNewLines, index);
        }

        /**
         * Maps the own text content of the selected element to the specified data type.
         * <p>
         * This method attempts to map the own text content to the provided type using the mapping logic defined in {@link DocumentMapperEngine}.
         * </p>
         *
         * @param <T>    the type to which the text content should be mapped
         * @param tClass the {@link Class} representing the type
         * @return an {@link Optional} containing the mapped value, or empty if mapping fails
         */
        public <T> Optional<Element, T> mapTo(Class<T> tClass) {
            return DocumentMapperEngine.this.ownText(document, select, selectSkip, null, keepNewLines, tClass, index);
        }
    }


    /**
     * Represents a step in the mapping process that applies a pattern matcher to the text content of an HTML element.
     * <p>
     * The {@code TextPatternStep} class allows for pattern-based extraction, transformation, and mapping of text content
     * within an element. It supports operations such as replacing text based on patterns and mapping the result to a specified data type.
     * </p>
     *
     * @version 1.0.0
     */
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public class TextPatternStep extends MapStep {

        private final Element document;
        private final String select;
        private final Integer selectSkip;
        private final PatternMatcher patternMatcher;
        private final Boolean keepNewLines;
        private final Integer index;

        /**
         * Replaces text in the selected element's content based on a regular expression and a replacement string.
         * <p>
         * This method creates a {@link TextReplaceStep} that performs text replacements in the content of the selected element,
         * applying the specified pattern matcher to refine the replacement process.
         * </p>
         *
         * @param regex       the regular expression used to identify text that should be replaced
         * @param replacement the replacement string to substitute for the matched text
         * @return a {@link TextReplaceStep} instance for further text replacement and mapping operations
         */
        public final TextReplaceStep replace(String regex, String replacement) {
            List<Replacement> replacements = new ArrayList<>();
            replacements.add(new Replacement(regex, replacement));
            return new TextReplaceStep(document, select, selectSkip, patternMatcher, keepNewLines, replacements, index);
        }

        /**
         * Maps the text content of the selected element to the specified data type, applying the defined pattern matcher.
         * <p>
         * This method attempts to map the text content of the element, after applying the pattern matcher, to the provided data type.
         * </p>
         *
         * @param <T>    the type to which the text content should be mapped
         * @param tClass the {@link Class} representing the type
         * @return an {@link Optional} containing the mapped value, or empty if mapping fails
         */
        public <T> Optional<Element, T> mapTo(Class<T> tClass) {
            return DocumentMapperEngine.this.text(document, select, selectSkip, patternMatcher, keepNewLines, tClass, index);
        }
    }

    /**
     * Represents a step in the mapping process that applies a pattern matcher to the own text content of an HTML element.
     * <p>
     * The {@code OwnTextPatternStep} class facilitates pattern-based extraction, transformation, and mapping of the own text content
     * within an element. This class supports operations such as replacing text based on patterns and mapping the result to a specified data type.
     * </p>
     *
     * @version 1.0.0
     */
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public class OwnTextPatternStep extends MapStep {

        private final Element document;
        private final String select;
        private final Integer selectSkip;
        private final PatternMatcher patternMatcher;
        private final Boolean keepNewLines;
        private final Integer index;

        /**
         * Replaces text in the selected element's own text content based on a regular expression and a replacement string.
         * <p>
         * This method creates a {@link OwnTextReplaceStep} that performs text replacements in the own text content of the selected element,
         * applying the specified pattern matcher to refine the replacement process.
         * </p>
         *
         * @param regex       the regular expression used to identify text that should be replaced
         * @param replacement the replacement string to substitute for the matched text
         * @return an {@link OwnTextReplaceStep} instance for further text replacement and mapping operations
         */
        public OwnTextReplaceStep replace(String regex, String replacement) {
            List<Replacement> replacements = new ArrayList<>();
            replacements.add(new Replacement(regex, replacement));
            return new OwnTextReplaceStep(document, select, selectSkip, patternMatcher, keepNewLines, replacements, index);
        }

        /**
         * Maps the own text content of the selected element to the specified data type, applying the defined pattern matcher.
         * <p>
         * This method attempts to map the own text content of the element, after applying the pattern matcher, to the provided data type.
         * </p>
         *
         * @param <T>    the type to which the text content should be mapped
         * @param tClass the {@link Class} representing the type
         * @return an {@link Optional} containing the mapped value, or empty if mapping fails
         */
        public <T> Optional<Element, T> mapTo(Class<T> tClass) {
            return DocumentMapperEngine.this.ownText(document, select, selectSkip, patternMatcher, keepNewLines, tClass, index);
        }
    }


    /**
     * Represents a step in the mapping process that applies a pattern matcher to an attribute of an HTML element.
     * <p>
     * The {@code AttrPatternStep} class allows for pattern-based extraction, transformation, and mapping of an attribute's value
     * within an element. This class supports operations such as replacing parts of the attribute value based on patterns and mapping
     * the result to a specified data type.
     * </p>
     *
     * @version 1.0.0
     */
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public class AttrPatternStep extends MapStep {

        private final Element document;
        private final String select;
        private final Integer selectSkip;
        private final String attr;
        private final PatternMatcher patternMatcher;
        private final Integer index;

        /**
         * Replaces parts of the selected element's attribute value based on a regular expression and a replacement string.
         * <p>
         * This method creates an {@link AttrReplaceStep} that performs replacements in the value of the specified attribute,
         * applying the given pattern matcher to refine the replacement process.
         * </p>
         *
         * @param regex       the regular expression used to identify parts of the attribute value to be replaced
         * @param replacement the replacement string to substitute for the matched text
         * @return an {@link AttrReplaceStep} instance for further text replacement and mapping operations
         */
        public AttrReplaceStep replace(String regex, String replacement) {
            List<Replacement> replacements = new ArrayList<>();
            replacements.add(new Replacement(regex, replacement));
            return new AttrReplaceStep(document, select, selectSkip, attr, patternMatcher, replacements, index);
        }

        /**
         * Maps the value of the selected element's attribute to the specified data type, applying the defined pattern matcher.
         * <p>
         * This method attempts to map the attribute value, after applying the pattern matcher, to the provided data type.
         * </p>
         *
         * @param <T>    the type to which the attribute value should be mapped
         * @param tClass the {@link Class} representing the type
         * @return an {@link Optional} containing the mapped value, or empty if mapping fails
         */
        public <T> Optional<Element, T> mapTo(Class<T> tClass) {
            return DocumentMapperEngine.this.attr(document, select, selectSkip, attr, patternMatcher, tClass, index);
        }
    }


    /**
     * Represents a step in the mapping process that applies a pattern matcher to the location (URL) of an HTML document.
     * <p>
     * The {@code LocationPatternStep} class facilitates pattern-based extraction, transformation, and mapping of the document's location (i.e., its URL).
     * It supports operations such as replacing parts of the URL based on patterns and mapping the result to a specified data type.
     * </p>
     *
     * @version 1.0.0
     */
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public class LocationPatternStep extends MapStep {

        private final Document document;
        private final PatternMatcher patternMatcher;
        private final List<Replacement> replacements;

        /**
         * Replaces parts of the document's location (URL) based on a regular expression and a replacement string.
         * <p>
         * This method allows for modifying the URL of the document by replacing matched parts of the URL
         * with the provided replacement string. It returns a new {@link LocationPatternStep} with the updated replacements.
         * </p>
         *
         * @param regex       the regular expression used to identify parts of the URL to be replaced
         * @param replacement the replacement string to substitute for the matched text
         * @return a new {@link LocationPatternStep} instance with the applied replacements
         */
        public LocationPatternStep replace(String regex, String replacement) {
            replacements.add(new Replacement(regex, replacement));
            return new LocationPatternStep(document, patternMatcher, replacements);
        }

        /**
         * Maps the document's location (URL) to the specified data type, applying the defined pattern matcher and replacements.
         * <p>
         * This method attempts to map the document's URL, after applying the pattern matcher and any replacements,
         * to the provided data type.
         * </p>
         *
         * @param <T>    the type to which the URL should be mapped
         * @param tClass the {@link Class} representing the type
         * @return an {@link Optional} containing the mapped value, or empty if mapping fails
         */
        public <T> Optional<Document, T> mapTo(Class<T> tClass) {
            return DocumentMapperEngine.this.location(document, patternMatcher, replacements, tClass);
        }
    }


    /**
     * Represents a step in the mapping process that operates on the text content of an HTML element.
     * <p>
     * The {@code TextStep} class provides methods for extracting, transforming, and mapping the text content
     * of an HTML element. It supports operations such as replacing parts of the text, preserving line breaks,
     * applying patterns, and mapping the text content to a specified data type.
     * </p>
     *
     * @version 1.0.0
     */
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public class TextStep extends MapStep {

        private final Element document;
        private final String select;
        private final Integer selectSkip;
        private final Boolean keepNewLines;
        private final Integer index;

        /**
         * Replaces parts of the selected element's text content based on a regular expression and a replacement string.
         * <p>
         * This method creates a {@link TextReplaceStep} that performs text replacements in the content of the selected element.
         * </p>
         *
         * @param regex       the regular expression used to identify parts of the text to be replaced
         * @param replacement the replacement string to substitute for the matched text
         * @return a {@link TextReplaceStep} instance for further text replacement and mapping operations
         */
        public TextReplaceStep replace(String regex, String replacement) {
            List<Replacement> replacements = new ArrayList<>();
            replacements.add(new Replacement(regex, replacement));
            return new TextReplaceStep(document, select, selectSkip, null, keepNewLines, replacements, index);
        }

        /**
         * Preserves line breaks in the text content of the selected element.
         * <p>
         * This method returns a new {@link TextStep} instance with the {@code keepNewLines} flag set to {@code true},
         * ensuring that line breaks are retained when extracting the text content.
         * </p>
         *
         * @return a new {@link TextStep} instance with line breaks preserved
         */
        public TextStep keepNewLines() {
            return new TextStep(document, select, selectSkip, true, index);
        }

        /**
         * Applies a pattern matcher to the text content of the selected element.
         * <p>
         * This method creates a {@link TextPatternStep} that allows pattern-based extraction and transformation of the text content.
         * </p>
         *
         * @param patternMatcher the {@link PatternMatcher} to apply to the text content
         * @return a {@link TextPatternStep} instance for further pattern-based mapping operations
         */
        public TextPatternStep pattern(PatternMatcher patternMatcher) {
            return new TextPatternStep(document, select, selectSkip, patternMatcher, keepNewLines, index);
        }

        /**
         * Maps the text content of the selected element to the specified data type.
         * <p>
         * This method attempts to map the text content of the element to the provided type using the mapping logic defined in {@link DocumentMapperEngine}.
         * </p>
         *
         * @param <T>    the type to which the text content should be mapped
         * @param tClass the {@link Class} representing the type
         * @return an {@link Optional} containing the mapped value, or empty if mapping fails
         */
        public <T> Optional<Element, T> mapTo(Class<T> tClass) {
            return DocumentMapperEngine.this.text(document, select, selectSkip, null, keepNewLines, tClass, index);
        }
    }


    /**
     * Represents a step in the mapping process that operates on an attribute of an HTML element.
     * <p>
     * The {@code AttrStep} class provides methods for extracting, transforming, and mapping the value of an attribute
     * from an HTML element. It supports operations such as replacing parts of the attribute value, applying patterns,
     * and mapping the value to a specified data type.
     * </p>
     *
     * @version 1.0.0
     */
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public class AttrStep extends MapStep {

        private final Element document;
        private final String select;
        private final Integer selectSkip;
        private final String attr;
        private final Integer index;

        /**
         * Replaces parts of the selected element's attribute value based on a regular expression and a replacement string.
         * <p>
         * This method creates an {@link AttrReplaceStep} that performs replacements in the value of the specified attribute.
         * </p>
         *
         * @param regex       the regular expression used to identify parts of the attribute value to be replaced
         * @param replacement the replacement string to substitute for the matched text
         * @return an {@link AttrReplaceStep} instance for further attribute value replacement and mapping operations
         */
        public AttrReplaceStep replace(String regex, String replacement) {
            List<Replacement> replacements = new ArrayList<>();
            replacements.add(new Replacement(regex, replacement));
            return new AttrReplaceStep(document, select, selectSkip, attr, null, replacements, index);
        }

        /**
         * Applies a pattern matcher to the selected element's attribute value.
         * <p>
         * This method creates an {@link AttrPatternStep} that allows pattern-based extraction and transformation of the attribute value.
         * </p>
         *
         * @param patternMatcher the {@link PatternMatcher} to apply to the attribute value
         * @return an {@link AttrPatternStep} instance for further pattern-based mapping operations
         */
        public AttrPatternStep pattern(PatternMatcher patternMatcher) {
            return new AttrPatternStep(document, select, selectSkip, attr, patternMatcher, index);
        }

        /**
         * Maps the value of the selected element's attribute to the specified data type.
         * <p>
         * This method attempts to map the attribute value to the provided type using the mapping logic defined in {@link DocumentMapperEngine}.
         * </p>
         *
         * @param <T>    the type to which the attribute value should be mapped
         * @param tClass the {@link Class} representing the type
         * @return an {@link Optional} containing the mapped value, or empty if mapping fails
         */
        public <T> Optional<Element, T> mapTo(Class<T> tClass) {
            return DocumentMapperEngine.this.attr(document, select, selectSkip, attr, null, tClass, index);
        }
    }


    /**
     * Represents a step in the mapping process that handles the replacement of text content within an HTML element.
     * <p>
     * The {@code TextReplaceStep} class allows for performing multiple text replacements on the content of an HTML element.
     * It supports chaining replacement operations and mapping the modified text to a specified data type.
     * </p>
     *
     * @version 1.0.0
     */
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public class TextReplaceStep extends MapStep {

        private final Element document;
        private final String select;
        private final Integer selectSkip;
        private final PatternMatcher patternMatcher;
        private final Boolean keepNewLines;
        private final List<Replacement> replacements;
        private final Integer index;

        /**
         * Adds a new replacement operation to the current list of replacements.
         * <p>
         * This method allows for chaining multiple replacement operations by adding another replacement
         * based on a regular expression and a replacement string. The modified {@code TextReplaceStep} is returned for further chaining.
         * </p>
         *
         * @param regex       the regular expression used to identify parts of the text to be replaced
         * @param replacement the replacement string to substitute for the matched text
         * @return a new {@code TextReplaceStep} instance with the added replacement
         */
        public TextReplaceStep replace(String regex, String replacement) {
            replacements.add(new Replacement(regex, replacement));
            return new TextReplaceStep(document, select, selectSkip, patternMatcher, keepNewLines, replacements, index);
        }

        /**
         * Maps the modified text content of the selected element to the specified data type.
         * <p>
         * This method applies all defined replacements and, optionally, a pattern matcher to the text content
         * before mapping it to the provided data type.
         * </p>
         *
         * @param <T>    the type to which the text content should be mapped
         * @param tClass the {@link Class} representing the type
         * @return an {@link Optional} containing the mapped value, or empty if mapping fails
         */
        public <T> Optional<Element, T> mapTo(Class<T> tClass) {
            return DocumentMapperEngine.this.text(document, select, selectSkip, patternMatcher, keepNewLines, replacements, tClass, index);
        }
    }

    /**
     * Represents a step in the mapping process that handles the replacement of own text content within an HTML element.
     * <p>
     * The {@code OwnTextReplaceStep} class facilitates performing multiple text replacements on the direct text content of an HTML element,
     * excluding its child elements. It supports chaining replacement operations and mapping the modified text to a specified data type.
     * </p>
     *
     * @version 1.0.0
     */
    @RequiredArgsConstructor
    public class OwnTextReplaceStep extends MapStep {

        private final Element document;
        private final String select;
        private final Integer selectSkip;
        private final PatternMatcher patternMatcher;
        private final Boolean keepNewLines;
        private final List<Replacement> replacements;
        private final Integer index;

        /**
         * Adds a new replacement operation to the current list of replacements for the element's own text.
         * <p>
         * This method allows for chaining multiple replacement operations by adding another replacement
         * based on a regular expression and a replacement string. The modified {@code OwnTextReplaceStep} is returned for further chaining.
         * </p>
         *
         * @param regex       the regular expression used to identify parts of the own text to be replaced
         * @param replacement the replacement string to substitute for the matched text
         * @return a new {@code OwnTextReplaceStep} instance with the added replacement
         */
        public OwnTextReplaceStep replace(String regex, String replacement) {
            replacements.add(new Replacement(regex, replacement));
            return new OwnTextReplaceStep(document, select, selectSkip, patternMatcher, keepNewLines, replacements, index);
        }

        /**
         * Maps the modified own text content of the selected element to the specified data type.
         * <p>
         * This method applies all defined replacements and, optionally, a pattern matcher to the own text content
         * before mapping it to the provided data type.
         * </p>
         *
         * @param <T>    the type to which the own text content should be mapped
         * @param tClass the {@link Class} representing the type
         * @return an {@link Optional} containing the mapped value, or empty if mapping fails
         */
        public <T> Optional<Element, T> mapTo(Class<T> tClass) {
            return DocumentMapperEngine.this.ownText(document, select, selectSkip, patternMatcher, keepNewLines, replacements, tClass, index);
        }

    }


    /**
     * Represents a step in the mapping process that handles the replacement of an attribute's value within an HTML element.
     * <p>
     * The {@code AttrReplaceStep} class facilitates performing multiple replacements on the value of an attribute within an HTML element.
     * It supports chaining replacement operations and mapping the modified attribute value to a specified data type.
     * </p>
     *
     * @version 1.0.0
     */
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public class AttrReplaceStep extends MapStep {

        private final Element document;
        private final String select;
        private final Integer selectSkip;
        private final String attr;
        private final PatternMatcher patternMatcher;
        private final List<Replacement> replacements;
        private final Integer index;

        /**
         * Adds a new replacement operation to the current list of replacements for the attribute's value.
         * <p>
         * This method allows for chaining multiple replacement operations by adding another replacement
         * based on a regular expression and a replacement string. The modified {@code AttrReplaceStep} is returned for further chaining.
         * </p>
         *
         * @param regex       the regular expression used to identify parts of the attribute's value to be replaced
         * @param replacement the replacement string to substitute for the matched text
         * @return a new {@code AttrReplaceStep} instance with the added replacement
         */
        public AttrReplaceStep replace(String regex, String replacement) {
            replacements.add(new Replacement(regex, replacement));
            return new AttrReplaceStep(document, select, selectSkip, attr, patternMatcher, replacements, index);
        }

        /**
         * Maps the modified attribute value of the selected element to the specified data type.
         * <p>
         * This method applies all defined replacements and, optionally, a pattern matcher to the attribute value
         * before mapping it to the provided data type.
         * </p>
         *
         * @param <T>    the type to which the attribute value should be mapped
         * @param tClass the {@link Class} representing the type
         * @return an {@link Optional} containing the mapped value, or empty if mapping fails
         */
        public <T> Optional<Element, T> mapTo(Class<T> tClass) {
            return DocumentMapperEngine.this.attr(document, select, selectSkip, attr, patternMatcher, replacements, tClass, index);
        }

    }


    /**
     * A custom optional class that encapsulates a value and a mapping function to transform the value.
     * <p>
     * The {@code Optional<I, O>} class is designed to handle optional values in a mapping process. It provides methods
     * to retrieve the mapped value or a default value if the original value is absent, as well as to throw an exception with a specific
     * error code if the value is absent.
     * </p>
     *
     * @param <I> the input type of the value before mapping
     * @param <O> the output type after applying the mapping function
     * @version 1.0.0
     */
    public class Optional<I, O> {

        private final I value;
        private final Function<I, O> mapper;

        /**
         * Constructs an {@code Optional} with a value and a mapping function.
         *
         * @param value  the input value that may be null
         * @param mapper the function to map the input value to the output type
         */
        public Optional(I value, Function<I, O> mapper) {
            this.value = value;
            this.mapper = mapper;
        }

        /**
         * Constructs an {@code Optional} from another {@code Optional} and a mapping function.
         * <p>
         * The constructor retrieves the value from the input {@code Optional}, applies the mapping function, and stores the result.
         * </p>
         *
         * @param value  the {@code Optional} containing the value to be transformed
         * @param mapper the function to map the input value to the output type
         */
        public Optional(Optional<?, I> value, Function<I, O> mapper) {
            this.value = value.orElse(null);
            this.mapper = mapper;
        }

        /**
         * Returns the mapped value or a default value if the original value is absent.
         * <p>
         * If the value is null, the specified default value is returned. Otherwise, the mapper is applied
         * to the value, and the result is returned. If the result is another {@code Optional}, it is unwrapped recursively.
         * </p>
         *
         * @param other the default value to return if the original value is null
         * @return the mapped value or the default value
         */
        public O orElse(O other) {
            if (value == null) {
                return other;
            }

            O result = mapper.apply(value);
            if (result instanceof Optional) {
                return ((Optional<?, O>) result).orElse(other);
            }
            return result;
        }

        /**
         * Returns the mapped value or throws an exception with a specific error code if the original value is absent.
         * <p>
         * If the value is null, an exception is thrown using the error code provided. Otherwise, the mapper is applied
         * to the value, and the result is returned. If the result is another {@code Optional}, it is unwrapped recursively.
         * </p>
         *
         * @param errorCode the error code to include in the exception if the value is null
         * @return the mapped value
         * @throws RuntimeException if the value is null
         */
        public O orThrowWithCode(String errorCode) {
            if (value == null) {
                throw DocumentMapperEngine.this.exceptionSupplier.apply(Pair.of(errorCode, null)).get();
            }

            try {
                O result = mapper.apply(value);
                if (result instanceof Optional) {
                    return (O) ((Optional<?, ?>) result).orThrowWithCode(errorCode);
                }

                return result;
            } catch (Exception e) {
                throw DocumentMapperEngine.this.exceptionSupplier.apply(Pair.of(errorCode, e)).get();
            }

        }
    }


    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE, staticName = "of")
    private static class Replacement {

        private final String regex;
        private final String replacement;
    }
}
