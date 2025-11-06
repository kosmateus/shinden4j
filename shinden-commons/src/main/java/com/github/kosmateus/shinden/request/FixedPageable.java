package com.github.kosmateus.shinden.request;

import com.github.kosmateus.shinden.http.request.SortParam;

/**
 * Interface defining pagination information for a paginated request
 * with a fixed page size of 10.
 *
 * <p>The {@code FixedPageable} interface extends {@link Pageable} to provide
 * pagination details for requests that always use a fixed page size. It includes
 * utility methods to create instances, navigate through pages, and manage sorting
 * parameters.</p>
 *
 * @param <T> the enum type representing the properties to sort by
 * @version 1.0.0
 */
public interface FixedPageable<T extends Enum<T> & SortParam<T>> extends Pageable<T> {

    /**
     * The fixed page size for all paginated requests.
     */
    int FIXED_PAGE_SIZE = 10;

    /**
     * Creates a new instance of {@code FixedPageable} with a specified page number and sorting.
     *
     * @param pageNumber the page number, must be greater than 0
     * @param sort       the sorting parameters, may be {@code null} for no sorting
     * @param <T>        the enum type representing the properties to sort by
     * @return a new instance of {@code FixedPageable}
     * @throws IllegalArgumentException if the page number is less than 1
     */
    static <T extends Enum<T> & SortParam<T>> FixedPageable<T> of(int pageNumber, Sort<T> sort) {
        if (pageNumber < 1) {
            throw new IllegalArgumentException("Page number must be greater than 0");
        }
        return new FixedPageableImpl<>(pageNumber, sort);
    }

    /**
     * Creates a new instance of {@code FixedPageable} with a specified page number and no sorting.
     *
     * @param pageNumber the page number, must be greater than 0
     * @param <T>        the enum type representing the properties to sort by
     * @return a new instance of {@code FixedPageable}
     */
    static <T extends Enum<T> & SortParam<T>> FixedPageable<T> of(int pageNumber) {
        return of(pageNumber, Sort.<T>unsorted());
    }

    @Override
    default int getPageSize() {
        return FIXED_PAGE_SIZE;
    }

    @Override
    default Pageable<T> next() {
        return of(getPageNumber() + 1, getSort().orElse(Sort.unsorted()));
    }

    @Override
    default Pageable<T> previousOrFirst() {
        return hasPrevious() ? of(getPageNumber() - 1, getSort().orElse(Sort.unsorted())) : first();
    }

    @Override
    default Pageable<T> first() {
        return of(1, getSort().orElse(Sort.unsorted()));
    }
}
