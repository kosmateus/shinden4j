package com.github.kosmateus.shinden.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZoneOffset;

/**
 * Utility class for handling common request-related operations.
 *
 * <p>The {@code RequestUtils} class provides static utility methods to assist with request processing,
 * such as converting {@link LocalDate} instances to seconds since the epoch.</p>
 *
 * @version 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestUtils {

    /**
     * Converts a {@link LocalDate} to the number of seconds since the epoch (January 1, 1970, 00:00:00 UTC).
     *
     * @param localDate the {@link LocalDate} to convert
     * @return the number of seconds since the epoch
     */
    public static long convertLocalDateToSeconds(LocalDate localDate) {
        return localDate.atStartOfDay(ZoneOffset.UTC).toEpochSecond();
    }
}
