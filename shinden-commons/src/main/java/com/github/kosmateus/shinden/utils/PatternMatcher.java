package com.github.kosmateus.shinden.utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@code PatternMatcher} class provides a utility for matching regular expressions
 * against input strings and extracting specific groups from the matched results.
 *
 * <p>This class supports both mandatory and optional matches, allowing users to decide
 * whether missing matches should throw an exception or simply return an empty result.</p>
 *
 * <p>Typical usage involves creating an instance with {@code match()} or {@code nullableMatch()} methods,
 * followed by calling {@code getOrThrow()} or {@code get()} to retrieve the matched group.</p>
 *
 * @version 1.0.0
 */
@RequiredArgsConstructor(staticName = "of", access = AccessLevel.PRIVATE)
public class PatternMatcher {

    @Getter
    private final Pattern pattern;
    @Getter
    private final Integer group;
    private final boolean doNotThrowOnMissing;

    /**
     * Creates a {@code PatternMatcher} that will throw an exception if the pattern does not match the input.
     *
     * @param regex the regular expression to compile
     * @param group the group to extract from the match
     * @return a new instance of {@code PatternMatcher}
     */
    public static PatternMatcher match(String regex, Integer group) {
        return PatternMatcher.of(Pattern.compile(regex), group, false);
    }

    /**
     * Creates a {@code PatternMatcher} that will return an empty {@code Optional} if the pattern does not match the input.
     *
     * @param regex the regular expression to compile
     * @param group the group to extract from the match
     * @return a new instance of {@code PatternMatcher}
     */
    public static PatternMatcher nullableMatch(String regex, Integer group) {
        return PatternMatcher.of(Pattern.compile(regex), group, true);
    }

    /**
     * Attempts to match the pattern against the given input string and return the specified group.
     *
     * <p>If the pattern does not match and {@code doNotThrowOnMissing} is {@code true}, this method
     * returns {@code null}. Otherwise, it throws the provided exception.</p>
     *
     * @param input             the input string to match against
     * @param exceptionSupplier the exception to throw if the pattern does not match
     * @return the matched group as a string, or {@code null} if not found
     */
    public String getOrThrow(String input, Supplier<? extends RuntimeException> exceptionSupplier) {
        if (doNotThrowOnMissing) {
            return get(input).orElse(null);
        }
        return get(input).orElseThrow(exceptionSupplier);
    }

    /**
     * Attempts to match the pattern against the given input string and return the specified group.
     *
     * @param input the input string to match against
     * @return an {@code Optional} containing the matched group, or empty if not found
     */
    public Optional<String> get(String input) {
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return Optional.of(matcher.group(group));
        }
        return Optional.empty();
    }
}
