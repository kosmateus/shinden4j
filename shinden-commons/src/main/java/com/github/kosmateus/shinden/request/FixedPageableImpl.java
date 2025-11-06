package com.github.kosmateus.shinden.request;

import com.github.kosmateus.shinden.http.request.SortParam;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * Implementation of the {@code FixedPageable} interface.
 *
 * <p>The {@code FixedPageableImpl} class provides a concrete implementation of the {@link FixedPageable} interface,
 * representing pagination details with a fixed page size. It manages the current page number and sorting options
 * for paginated requests.</p>
 *
 * @param <T> the enum type representing the properties to sort by
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class FixedPageableImpl<T extends Enum<T> & SortParam<T>> implements FixedPageable<T> {

    /**
     * The current page number in the paginated request.
     */
    private final int pageNumber;

    /**
     * The sorting parameters to be applied to the paginated request.
     */
    private final Sort<T> sort;

    /**
     * Returns the current page number.
     *
     * @return the current page number
     */
    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * Returns the sorting parameters for the paginated request, if any.
     *
     * @return an {@link Optional} containing the sorting parameters, or {@code Optional.empty()} if none are specified
     */
    @Override
    public Optional<Sort<T>> getSort() {
        return Optional.ofNullable(sort);
    }
}
