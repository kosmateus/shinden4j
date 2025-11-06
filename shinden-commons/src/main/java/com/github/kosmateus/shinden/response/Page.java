package com.github.kosmateus.shinden.response;

import javax.validation.constraints.NotNull;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A unified interface for paginated data that combines functionality of Page, Slice, and Streamable.
 * <p>
 * The {@code Page} interface represents a single page of data in a paginated response.
 * It provides various methods to access the page's content, metadata such as total elements
 * and pages, and utility methods for iteration and transformation.
 * </p>
 *
 * @param <T> the type of elements in the page
 * @version 1.0.0
 */
public interface Page<T> extends Iterable<T> {

    /**
     * Returns the total amount of elements.
     *
     * @return the total number of elements across all pages
     */
    long getTotalElements();

    /**
     * Returns the total number of pages.
     *
     * @return the total number of pages in the pagination
     */
    int getTotalPages();

    /**
     * Returns whether the current page is the last one.
     *
     * @return {@code true} if this page is the last one, {@code false} otherwise
     */
    boolean isLast();

    /**
     * Returns whether the current page is the first one.
     *
     * @return {@code true} if this page is the first one, {@code false} otherwise
     */
    boolean isFirst();

    /**
     * Returns the number of the current page.
     *
     * @return the number of the current page (zero-based index)
     */
    int getNumber();

    /**
     * Returns the size of the page.
     *
     * @return the number of elements expected on each page
     */
    int getSize();

    /**
     * Returns the number of elements currently on this page.
     *
     * @return the number of elements on this page
     */
    int getNumberOfElements();

    /**
     * Returns the page content as a list.
     *
     * @return the list of elements on this page
     */
    List<T> getContent();

    /**
     * Returns whether there are any elements on this page.
     *
     * @return {@code true} if the page has content, {@code false} otherwise
     */
    boolean hasContent();

    /**
     * Returns whether there is a next page.
     *
     * @return {@code true} if there is a next page, {@code false} otherwise
     */
    boolean hasNext();

    /**
     * Returns whether there is a previous page.
     *
     * @return {@code true} if there is a previous page, {@code false} otherwise
     */
    boolean hasPrevious();

    /**
     * Returns whether the page is empty.
     *
     * @return {@code true} if the page has no content, {@code false} otherwise
     */
    boolean isEmpty();

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an {@link Iterator} over the elements on this page
     */
    @NotNull
    @Override
    default Iterator<T> iterator() {
        return getContent().iterator();
    }

    /**
     * Returns a sequential {@code Stream} with this collection as its source.
     * <p>
     * This method provides a convenient way to work with the page content using the Stream API.
     * </p>
     *
     * @return a sequential {@link Stream} of elements
     */
    default Stream<T> stream() {
        return getContent().stream();
    }

    /**
     * Applies the given {@link Function} to the content of the page and returns a new Page.
     * <p>
     * This method transforms the content of the page by applying the provided converter function
     * to each element. The resulting elements are collected into a new {@code Page} of type {@code U}.
     * </p>
     *
     * @param <U>       the type of the new page content
     * @param converter the function to apply to each element in the content
     * @return a new {@code Page} of type {@code U} with transformed content
     */
    default <U> Page<U> map(Function<? super T, ? extends U> converter) {
        List<U> convertedContent = this.getContent().stream().map(converter).collect(Collectors.toList());
        return new PageImpl<>(convertedContent, getNumber(), getSize(), getTotalElements());
    }
}
