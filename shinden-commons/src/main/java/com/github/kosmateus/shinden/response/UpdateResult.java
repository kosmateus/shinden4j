package com.github.kosmateus.shinden.response;

import com.github.kosmateus.shinden.http.response.EmptyReason;
import lombok.Builder;
import lombok.Getter;

/**
 * Represents the result of an update operation.
 * <p>
 * The {@code UpdateResult} class encapsulates the outcome of an update operation,
 * providing information about whether the operation was successful or not, as well
 * as any reasons for failure if applicable.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@Builder
public class UpdateResult {

    /**
     * The result of the update operation.
     * <p>
     * This field indicates whether the update operation was a success or a failure.
     * It is represented by the {@link Result} enum, which can have values {@code SUCCESS} or {@code FAILURE}.
     * </p>
     */
    private final Result result;

    /**
     * The reason for the failure of the update operation, if any.
     * <p>
     * This field is populated if the update operation failed, providing additional
     * information about why the operation did not succeed. It is represented by the {@link EmptyReason} class.
     * If the operation was successful, this field may be {@code null}.
     * </p>
     */
    private final EmptyReason emptyReason;
}
