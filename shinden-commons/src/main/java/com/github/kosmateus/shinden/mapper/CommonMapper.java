package com.github.kosmateus.shinden.mapper;


import com.github.kosmateus.shinden.http.response.ResponseHandler;
import com.github.kosmateus.shinden.response.Result;
import com.github.kosmateus.shinden.response.UpdateResult;

import static com.github.kosmateus.shinden.response.Result.FAILURE;
import static com.github.kosmateus.shinden.response.Result.SUCCESS;


/**
 * Utility class for mapping HTTP response handlers to update results.
 * <p>
 * The {@code CommonMapper} class provides a method to map a {@link ResponseHandler}
 * to an {@link UpdateResult}, translating the response status into a success or failure
 * result.
 * </p>
 *
 * @version 1.0.0
 */
public class CommonMapper {

    /**
     * Maps a {@link ResponseHandler} to an {@link UpdateResult}.
     * <p>
     * This method checks if the response handled by the {@link ResponseHandler} was successful.
     * If the response is successful (status code < 400), the result will be set to {@link Result#SUCCESS}.
     * Otherwise, the result will be set to {@link Result#FAILURE}, and the empty reason will be included
     * in the {@link UpdateResult}.
     * </p>
     *
     * @param responseHandler the {@link ResponseHandler} to be mapped
     * @return an {@link UpdateResult} representing the outcome of the response
     */
    public UpdateResult map(ResponseHandler<?> responseHandler) {
        if (responseHandler.isOk()) {
            return UpdateResult.builder()
                    .result(SUCCESS)
                    .build();
        }
        return UpdateResult.builder()
                .result(FAILURE)
                .emptyReason(responseHandler.getEmptyReason())
                .build();
    }
}
