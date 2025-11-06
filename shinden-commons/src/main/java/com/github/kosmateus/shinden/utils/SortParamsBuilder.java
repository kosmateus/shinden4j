package com.github.kosmateus.shinden.utils;

import com.github.kosmateus.shinden.request.Pageable;
import com.github.kosmateus.shinden.request.Sort;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class for building sorting parameters for HTTP requests.
 * <p>
 * The {@code SortParamsBuilder} class provides a static method to generate a string
 * representation of sorting parameters from a {@link Pageable} object. This class is
 * designed to simplify the construction of sorting parameters for HTTP queries.
 * </p>
 *
 * @version 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SortParamsBuilder {

    /**
     * Builds a string representation of sorting parameters from the provided {@link Pageable} object.
     * <p>
     * This method extracts the sorting information from the {@code Pageable} object and constructs
     * a string representation suitable for use in HTTP requests. The generated string consists of
     * property names followed by their respective sort directions (e.g., {@code "nameASC,ageDESC"}).
     * If no sorting is defined, it returns {@code null}.
     * </p>
     *
     * @param pageable the {@link Pageable} object containing pagination and sorting information
     * @return a string representation of the sorting parameters, or {@code null} if no sorting is defined
     */
    public static String build(Pageable<?> pageable) {
        return pageable.getSort()
                .map(Sort::getOrders)
                .flatMap(orders -> orders.stream()
                        .map(order -> order.getProperty().getSortValue() + order.getDirection().name())
                        .reduce((a, b) -> a + "," + b))
                .orElse(null);
    }
}
