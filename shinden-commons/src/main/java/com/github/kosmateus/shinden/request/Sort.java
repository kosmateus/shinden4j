package com.github.kosmateus.shinden.request;

import com.github.kosmateus.shinden.http.request.SortParam;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class representing sorting parameters for a paginated query.
 * <p>
 * The {@code Sort} class provides a way to specify sorting options for paginated data queries.
 * It supports multiple sorting orders and can be configured to sort in ascending or descending
 * order based on specified properties.
 * </p>
 *
 * @param <T> the enum type representing the properties to sort by
 * @version 1.0.0
 */
@Getter
@RequiredArgsConstructor
public class Sort<T extends Enum<T> & SortParam<T>> {

    private final List<Order<T>> orders;

    /**
     * Creates a {@code Sort} instance with the specified sorting orders.
     * <p>
     * This method allows you to define one or more sorting orders for a query. Each order specifies
     * a property to sort by and the direction (ascending or descending) of the sort.
     * </p>
     *
     * @param orders one or more {@link Order} objects representing the sorting criteria
     * @param <T>    the enum type representing the properties to sort by
     * @return a new {@code Sort} instance with the specified sorting orders
     */
    @SafeVarargs
    public static <T extends Enum<T> & SortParam<T>> Sort<T> by(Order<T>... orders) {
        return new Sort<>(Arrays.asList(orders));
    }

    /**
     * Creates an unsorted {@code Sort} instance.
     * <p>
     * This method returns a {@code Sort} instance that does not define any sorting criteria,
     * indicating that the query should not apply any specific ordering.
     * </p>
     *
     * @param <T> the enum type representing the properties to sort by
     * @return an unsorted {@code Sort} instance
     */
    public static <T extends Enum<T> & SortParam<T>> Sort<T> unsorted() {
        return new Sort<>(Collections.emptyList());
    }

    /**
     * Checks if the {@code Sort} instance has any sorting orders defined.
     *
     * @return {@code true} if there are sorting orders defined; {@code false} otherwise
     */
    public boolean isSorted() {
        return !orders.isEmpty();
    }

    /**
     * Checks if the {@code Sort} instance has no sorting orders defined.
     *
     * @return {@code true} if there are no sorting orders defined; {@code false} otherwise
     */
    public boolean isUnsorted() {
        return orders.isEmpty();
    }

    /**
     * Enum representing the direction of sorting.
     */
    public enum Direction {
        /**
         * Ascending sort order.
         */
        ASC,
        /**
         * Descending sort order.
         */
        DESC;
    }

    /**
     * Class representing a single sorting order.
     * <p>
     * The {@code Order} class specifies the property to sort by and the direction of the sort
     * (ascending or descending).
     * </p>
     *
     * @param <T> the enum type representing the properties to sort by
     */
    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class Order<T extends Enum<T> & SortParam<T>> {

        /**
         * The property to sort by.
         */
        private final T property;

        /**
         * The direction of the sort (ascending or descending).
         */
        private final Direction direction;
    }
}
