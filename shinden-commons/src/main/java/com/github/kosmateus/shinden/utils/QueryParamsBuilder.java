package com.github.kosmateus.shinden.utils;

import com.github.kosmateus.shinden.http.request.HttpRequest.KeyValue;
import com.github.kosmateus.shinden.http.request.QueryParam;
import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The {@code QueryParamsBuilder} class is a utility for constructing query parameter maps.
 *
 * <p>This class provides static methods to create a map of query parameters from an array
 * or a set of {@link QueryParam} objects, ensuring that only non-null parameters are included
 * in the final map. Additionally, it supports combining values for duplicate keys with a specified separator.</p>
 *
 * <p>The resulting map is immutable, making it safe to use in contexts where immutability is required.</p>
 *
 * @version 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QueryParamsBuilder {

    /**
     * Constructs an immutable map of query parameters from the provided {@link QueryParam} objects.
     *
     * <p>This method filters out any {@code null} {@link QueryParam} objects and any objects with {@code null}
     * names or values to prevent {@code NullPointerException} during map construction. The resulting map is immutable.</p>
     *
     * @param queryParams an array of {@link QueryParam} objects
     * @return an immutable map of query parameters where each key is a query parameter name and each value is its corresponding value
     */
    public static Map<String, String> build(QueryParam... queryParams) {
        return Arrays.stream(queryParams)
                .filter(Objects::nonNull)
                .filter(queryParam -> queryParam.getQueryParameter() != null && queryParam.getQueryValue() != null)
                .collect(
                        ImmutableMap.Builder<String, String>::new,
                        (builder, queryParam) -> builder.put(queryParam.getQueryParameter(), queryParam.getQueryValue()),
                        (builder1, builder2) -> builder1.putAll(builder2.build())
                )
                .build();
    }

    /**
     * Constructs an immutable map of query parameters from a set of {@link QueryParam} objects,
     * combining values for duplicate keys using a specified separator.
     *
     * <p>This method first filters out any {@code null} {@link QueryParam} objects and any objects with
     * {@code null} names or values. Then, for any duplicate keys, it concatenates their values using the given separator.</p>
     *
     * @param separator   the string to use for separating multiple values associated with the same key
     * @param queryParams a set of {@link QueryParam} objects, may be {@code null}
     * @return an immutable map of query parameters with combined values for duplicate keys
     */
    public static Map<String, String> buildWithSeparator(String separator, @Nullable Set<? extends QueryParam> queryParams) {
        if (queryParams == null) {
            return ImmutableMap.of();
        }

        ImmutableMap.Builder<String, String> builder = new ImmutableMap.Builder<>();
        Map<String, String> tempMap = new HashMap<>();

        queryParams.stream()
                .filter(Objects::nonNull)
                .filter(queryParam -> queryParam.getQueryParameter() != null && queryParam.getQueryValue() != null)
                .forEach(queryParam -> {
                    String key = queryParam.getQueryParameter();
                    String value = queryParam.getQueryValue();

                    if (tempMap.containsKey(key)) {
                        String combinedValue = tempMap.get(key) + separator + value;
                        tempMap.put(key, combinedValue);
                    } else {
                        tempMap.put(key, value);
                    }
                });

        tempMap.forEach(builder::put);
        return builder.build();
    }

    /**
     * Converts a list of maps containing query parameters into a list of {@link KeyValue} objects.
     *
     * <p>Each map is converted by iterating through its entries and transforming each key-value pair
     * into a {@link KeyValue} object.</p>
     *
     * @param queryParams varargs of maps where each map represents a set of query parameters
     * @return a list of {@link KeyValue} objects representing all query parameters
     */
    @SafeVarargs
    public static List<KeyValue> convertToKeyVal(Map<String, String>... queryParams) {
        return Arrays.stream(queryParams)
                .filter(Objects::nonNull)
                .flatMap(queryParam -> queryParam.entrySet()
                        .stream()
                        .map(entry -> KeyValue.of(entry.getKey(), entry.getValue())))
                .collect(Collectors.toList());
    }
}
