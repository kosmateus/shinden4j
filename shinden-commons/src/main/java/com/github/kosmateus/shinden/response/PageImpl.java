package com.github.kosmateus.shinden.response;

import com.github.kosmateus.shinden.request.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Default implementation of the {@link Page} interface.
 * <p>
 * The {@code PageImpl} class provides a standard implementation of the {@link Page} interface
 * for handling paginated data. It supports common pagination operations such as retrieving the
 * total number of elements, the number of pages, checking if the current page is the first or
 * last, and mapping the content to a different type.
 * </p>
 *
 * @param <T> the type of elements in the page
 * @version 1.0.0
 */
public class PageImpl<T> implements Page<T> {

    private final List<T> content;
    private final int pageNumber;
    private final int pageSize;
    private final long totalElements;

    /**
     * Constructs a new {@code PageImpl} instance with the specified content, page number, page size, and total elements.
     *
     * @param content       the list of content elements on the current page; may be {@code null} or empty
     * @param pageNumber    the current page number
     * @param pageSize      the number of elements per page
     * @param totalElements the total number of elements across all pages
     */
    public PageImpl(List<T> content, int pageNumber, int pageSize, long totalElements) {
        this.content = content == null ? Collections.emptyList() : content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
    }

    /**
     * Constructs a new {@code PageImpl} instance with the specified content, {@link Pageable} object, and total elements.
     *
     * @param content       the list of content elements on the current page; may be {@code null} or empty
     * @param pageable      the {@link Pageable} object containing pagination information
     * @param totalElements the total number of elements across all pages
     */
    public PageImpl(List<T> content, Pageable pageable, long totalElements) {
        this(content, pageable.getPageNumber(), pageable.getPageSize(), totalElements);
    }

    /**
     * Returns the total number of elements across all pages.
     *
     * @return the total number of elements
     */
    @Override
    public long getTotalElements() {
        return totalElements;
    }

    /**
     * Returns the total number of pages.
     *
     * @return the total number of pages
     */
    @Override
    public int getTotalPages() {
        return (int) Math.ceil((double) totalElements / pageSize);
    }

    /**
     * Returns whether the current page is the last one.
     *
     * @return {@code true} if this page is the last one, {@code false} otherwise
     */
    @Override
    public boolean isLast() {
        return !hasNext();
    }

    /**
     * Returns whether the current page is the first one.
     *
     * @return {@code true} if this page is the first one, {@code false} otherwise
     */
    @Override
    public boolean isFirst() {
        return pageNumber == 0;
    }

    /**
     * Returns the number of the current page.
     *
     * @return the current page number (zero-based)
     */
    @Override
    public int getNumber() {
        return pageNumber;
    }

    /**
     * Returns the size of the page.
     *
     * @return the number of elements per page
     */
    @Override
    public int getSize() {
        return pageSize;
    }

    /**
     * Returns the number of elements currently on this page.
     *
     * @return the number of elements on this page
     */
    @Override
    public int getNumberOfElements() {
        return content.size();
    }

    /**
     * Returns the page content as a list.
     *
     * @return an unmodifiable list of elements on this page
     */
    @Override
    public List<T> getContent() {
        return Collections.unmodifiableList(content);
    }

    /**
     * Returns whether there are any elements on this page.
     *
     * @return {@code true} if the page has content, {@code false} otherwise
     */
    @Override
    public boolean hasContent() {
        return !content.isEmpty();
    }

    /**
     * Returns whether there is a next page.
     *
     * @return {@code true} if there is a next page, {@code false} otherwise
     */
    @Override
    public boolean hasNext() {
        return (long) (pageNumber + 1) * pageSize < totalElements;
    }

    /**
     * Returns whether there is a previous page.
     *
     * @return {@code true} if there is a previous page, {@code false} otherwise
     */
    @Override
    public boolean hasPrevious() {
        return pageNumber > 0;
    }

    /**
     * Returns whether the page is empty.
     *
     * @return {@code true} if the page has no content, {@code false} otherwise
     */
    @Override
    public boolean isEmpty() {
        return content.isEmpty();
    }

    /**
     * Applies the given {@link Function} to the content of the page and returns a new {@link Page} with the converted content.
     *
     * @param <U>       the type of the new page content
     * @param converter the function to apply to each element in the content
     * @return a new {@link Page} of type {@code U} with transformed content
     */
    @Override
    public <U> Page<U> map(Function<? super T, ? extends U> converter) {
        List<U> convertedContent = this.getContent().stream().map(converter).collect(Collectors.toList());
        return new PageImpl<>(convertedContent, getNumber(), getSize(), getTotalElements());
    }
}
