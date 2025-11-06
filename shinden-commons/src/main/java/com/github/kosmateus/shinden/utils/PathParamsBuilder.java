package com.github.kosmateus.shinden.utils;

import com.github.kosmateus.shinden.http.request.PathParam;
import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * Utility class for building a map of path parameters.
 * <p>
 * The {@code PathParamsBuilder} class provides a static method to construct a map of
 * path parameters from a variable number of {@link PathParam} objects. This class is
 * designed to simplify the creation and handling of path parameters in HTTP requests.
 * </p>
 *
 * @version 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PathParamsBuilder {

    /**
     * Builds a map of path parameters from the provided {@link PathParam} objects.
     * <p>
     * This method takes a variable number of {@code PathParam} objects, filters out any
     * {@code null} values, and collects the non-null path parameters into an immutable map.
     * Each path parameter is represented as a key-value pair in the resulting map, where
     * the key is the path parameter name and the value is the corresponding path parameter value.
     * </p>
     *
     * @param pathParams a variable number of {@link PathParam} objects representing the path parameters
     * @return an immutable {@link Map} containing the path parameters as key-value pairs
     */
    public static Map<String, String> build(PathParam... pathParams) {
        return Arrays.stream(pathParams)
                .filter(Objects::nonNull)
                .collect(
                        ImmutableMap.Builder<String, String>::new,
                        (builder, queryParam) -> builder.put(queryParam.getPathParameter(), queryParam.getPathValue()),
                        (builder1, builder2) -> builder1.putAll(builder2.build())
                )
                .build();
    }
}
