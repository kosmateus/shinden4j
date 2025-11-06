package com.github.kosmateus.shinden.enums;

import com.github.kosmateus.shinden.http.request.QueryParam;
import com.github.kosmateus.shinden.i18n.Translatable;
import com.github.kosmateus.shinden.request.SearchQueryParam;

/**
 * Represents a tag that can be used in various requests and filtered queries.
 * <p>
 * The {@code Tag} interface extends {@link Translatable} and {@link QueryParam},
 * providing additional methods to obtain tag-specific details, such as the tag's
 * unique identifier and its type. It is designed to represent a tag that can be
 * translated and used as a query parameter in HTTP requests.
 * </p>
 *
 * <p>
 * Classes implementing this interface should provide a unique identifier for the tag
 * and a specific type of tag (e.g., genre, category, etc.).
 * </p>
 *
 * @version 1.0.0
 */
public interface Tag extends Translatable, QueryParam, SearchQueryParam {

    /**
     * Gets the unique identifier of the tag.
     *
     * @return the unique identifier of the tag
     */
    Integer getId();

    /**
     * Gets the type of the tag.
     * <p>
     * This method should return the specific type of the tag, which could represent
     * categories such as genre, category, etc.
     * </p>
     *
     * @return the type of the tag
     */
    String getTagType();

}
