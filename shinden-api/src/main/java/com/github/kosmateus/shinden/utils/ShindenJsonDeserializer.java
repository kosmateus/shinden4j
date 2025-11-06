package com.github.kosmateus.shinden.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.github.kosmateus.shinden.common.enums.MPAA;
import com.github.kosmateus.shinden.common.enums.TitleStatus;
import com.github.kosmateus.shinden.common.enums.TitleType;
import com.github.kosmateus.shinden.user.common.enums.UserTitleStatus;

import java.io.IOException;

/**
 * Contains custom JSON deserializers for various enumerations and types used within the Shinden application.
 * <p>
 * The {@code ShindenJsonDeserializer} class provides a set of static inner classes that extend
 * {@link JsonDeserializer} to handle the deserialization of specific types, such as enums and other data types
 * that require custom handling during JSON parsing.
 * </p>
 *
 * @version 1.0.0
 */
public class ShindenJsonDeserializer {

    /**
     * Custom deserializer for the {@link TitleType} enum.
     * <p>
     * Converts a JSON string value to the corresponding {@code TitleType} enum constant.
     * </p>
     */
    public static class TitleTypeDeserializer extends JsonDeserializer<TitleType> {
        @Override
        public TitleType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String value = p.getText();
            return TitleType.fromValue(value);
        }
    }

    /**
     * Custom deserializer for the {@link UserTitleStatus} enum.
     * <p>
     * Converts a JSON string value to the corresponding {@code UserTitleStatus} enum constant.
     * </p>
     */
    public static class UserTitleStatusDeserializer extends JsonDeserializer<UserTitleStatus> {
        @Override
        public UserTitleStatus deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String value = p.getText();
            return UserTitleStatus.fromValue(value);
        }
    }

    /**
     * Custom deserializer for the {@link TitleStatus} enum.
     * <p>
     * Converts a JSON string value to the corresponding {@code TitleStatus} enum constant.
     * </p>
     */
    public static class TitleStatusDeserializer extends JsonDeserializer<TitleStatus> {
        @Override
        public TitleStatus deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String value = p.getText();
            return TitleStatus.fromValue(value);
        }
    }

    /**
     * Custom deserializer for the {@link MPAA} enum.
     * <p>
     * Converts a JSON string value to the corresponding {@code MPAA} enum constant.
     * </p>
     */
    public static class MPAADeserializer extends JsonDeserializer<MPAA> {
        @Override
        public MPAA deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String value = p.getText();
            return MPAA.fromValue(value);
        }
    }

    /**
     * Custom deserializer for Boolean values.
     * <p>
     * Handles deserialization of integer-based Boolean values, where {@code 1} is treated as {@code true}
     * and any other value (including {@code null}) is treated as {@code false}.
     * </p>
     */
    public static class BooleanDeserializer extends JsonDeserializer<Boolean> {

        @Override
        public Boolean deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException {
            if (jsonParser.getCurrentToken() == JsonToken.VALUE_NULL) {
                return false;
            }

            int value = jsonParser.getIntValue();
            return value == 1;
        }

        @Override
        public Boolean getNullValue(DeserializationContext ignored) {
            return Boolean.FALSE;
        }
    }
}
