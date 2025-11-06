package com.github.kosmateus.shinden.user.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.kosmateus.shinden.http.request.FileResource;
import com.github.kosmateus.shinden.i18n.Translatable;
import com.github.kosmateus.shinden.user.common.UserId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * Represents a request to import a MyAnimeList (MAL) list for a user.
 * <p>
 * The {@code ImportMalListRequest} class encapsulates the data required to import a MAL list,
 * including the user ID, the MAL list file, and the import type.
 * </p>
 *
 * @version 1.0.0
 */
@Getter
@Builder
@ToString
@AllArgsConstructor(onConstructor_ = @JsonCreator)
public class ImportMalListRequest implements UserId {

    /**
     * The ID of the user for whom the MAL list is being imported.
     * <p>
     * This field is mandatory and must be provided as a non-null {@link Long} value.
     * </p>
     */
    @NotNull
    private final Long userId;

    /**
     * The MAL list file to be imported.
     * <p>
     * This field is mandatory and must be provided as a non-null {@link FileResource}.
     * </p>
     */
    @NotNull
    private final FileResource malListFile;

    /**
     * The type of import to be performed.
     * <p>
     * This field is mandatory and must be provided as a non-null {@link ImportType}.
     * </p>
     */
    @NotNull
    private final ImportType importType;

    /**
     * Enum representing the type of import to be performed when importing a MyAnimeList (MAL) list.
     */
    public enum ImportType implements Translatable {
        /**
         * Fill mode. This mode adds new entries from the MAL list to the user's Shinden list
         * without modifying existing entries.
         */
        FILL("fill", "user.import-mal-list.import-type.fill"),

        /**
         * Replace mode. This mode replaces the user's entire Shinden list with the contents
         * of the MAL list, potentially removing existing entries that are not in the MAL list.
         */
        REPLACE("replace", "user.import-mal-list.import-type.replace");

        private final String value;
        private final String translationKey;

        ImportType(String value, String translationKey) {
            this.value = value;
            this.translationKey = translationKey;
        }

        @Override
        public String getTranslationKey() {
            return translationKey;
        }

        /**
         * Returns the string value associated with this import type.
         *
         * @return the string value of the import type
         */
        public String getValue() {
            return value;
        }
    }
}
