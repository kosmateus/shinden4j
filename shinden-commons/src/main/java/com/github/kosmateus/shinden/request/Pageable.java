package com.github.kosmateus.shinden.request;

import com.github.kosmateus.shinden.http.request.SortParam;

import java.util.Optional;

/**
 * Interface defining pagination information for a paginated request.
 * <p>
 * The {@code Pageable} interface encapsulates pagination details, such as the page number,
 * page size, sorting parameters, and methods to navigate between pages. It provides a standard
 * way to request a specific page of data in a paginated collection.
 * </p>
 *
 * @param <T> the enum type representing the properties to sort by
 * @version 1.0.1
 */
public interface Pageable<T extends Enum<T> & SortParam<T>> {

    /**
     * Creates a new instance of {@code Pageable}.
     * <p>
     * This static factory method provides a convenient way to create a new {@code Pageable} instance
     * with the specified page number, page size, and sorting parameters.
     * </p>
     *
     * @param pageNumber the page number, must be greater than zero
     * @param pageSize   the size of the page, must be greater than zero
     * @param sort       the sorting parameters, may be {@code null} for no sorting
     * @param <T>        the enum type representing the properties to sort by
     * @return a new instance of {@code Pageable}
     */
    static <T extends Enum<T> & SortParam<T>> Pageable<T> of(int pageNumber, int pageSize, Sort<T> sort) {
        if (pageSize < 1) {
            throw new IllegalArgumentException("Page size must be greater than 0");
        }
        return new PageableImpl<>(pageNumber, pageSize, sort);
    }

    /**
     * Creates a new instance of {@code Pageable} with the specified page number and page size.
     * <p>
     * The sorting parameters will be set to unsorted by default.
     * </p>
     *
     * @param pageNumber the page number, must be greater than zero
     * @param pageSize   the size of the page, must be greater than zero
     * @param <T>        the enum type representing the properties to sort by
     * @return a new instance of {@code Pageable}
     */
    static <T extends Enum<T> & SortParam<T>> Pageable<T> of(int pageNumber, int pageSize) {
        return of(pageNumber, pageSize, Sort.<T>unsorted());
    }

    /**
     * Returns the page number to be returned.
     *
     * @return the page number, zero-based (0 is the first page)
     */
    int getPageNumber();

    /**
     * Returns the number of items to be returned per page.
     *
     * @return the page size
     */
    int getPageSize();

    /**
     * Returns the offset to be taken according to the underlying page and page size.
     * <p>
     * The offset is calculated as {@code pageNumber * pageSize} and represents the index
     * of the first element in the current page relative to the entire collection.
     * </p>
     *
     * @return the offset
     */
    default long getOffset() {
        return (long) getPageNumber() * getPageSize();
    }

    /**
     * Returns the sorting parameters for the page request.
     * <p>
     * Provides an {@link Optional} with sorting parameters that define the order in which
     * elements should be returned in the page. If no sorting is specified, the result will be empty.
     * </p>
     *
     * @return an {@link Optional} with sorting parameters or empty if unsorted
     */
    Optional<Sort<T>> getSort();

    /**
     * Returns a {@code Pageable} instance for the next page.
     * <p>
     * Creates a new {@code Pageable} object that requests the next page in the sequence, maintaining
     * the current page size and sorting parameters.
     * </p>
     *
     * @return a {@code Pageable} for the next page
     */
    default Pageable<T> next() {
        return of(getPageNumber() + 1, getPageSize(), getSort().orElse(Sort.unsorted()));
    }

    /**
     * Returns a {@code Pageable} instance for the previous page or the first page if already at the beginning.
     * <p>
     * Creates a new {@code Pageable} object that requests the previous page in the sequence, or the first page
     * if the current page is already the first page.
     * </p>
     *
     * @return a {@code Pageable} for the previous page or the first page
     */
    default Pageable<T> previousOrFirst() {
        return hasPrevious() ? of(getPageNumber() - 1, getPageSize(), getSort().orElse(Sort.unsorted())) : first();
    }

    /**
     * Returns a {@code Pageable} instance for the first page.
     * <p>
     * Creates a new {@code Pageable} object that requests the first page, maintaining the current page size
     * and sorting parameters.
     * </p>
     *
     * @return a {@code Pageable} for the first page
     */
    default Pageable<T> first() {
        return of(0, getPageSize(), getSort().orElse(Sort.unsorted()));
    }

    /**
     * Checks if there is a previous page.
     *
     * @return {@code true} if there is a previous page, {@code false} otherwise
     */
    default boolean hasPrevious() {
        return getPageNumber() > 0;
    }
}
