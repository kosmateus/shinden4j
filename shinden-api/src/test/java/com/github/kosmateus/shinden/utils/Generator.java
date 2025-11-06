package com.github.kosmateus.shinden.utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Generator {
    public static String generateEnumTagValues(List<TagData> data, String key) {
        StringBuilder enumBuilder = new StringBuilder();

        for (TagData entry : data) {
            Integer id = entry.getId();
            String enumValue = TextNormalizer.normalizeForEnumName(entry.getKey());
            enumBuilder
                    .append(enumValue)
                    .append("(")
                    .append(id)
                    .append(", ")
                    .append("\"")
                    .append(key)
                    .append(".")
                    .append(entry.getKey())
                    .append("\"")
                    .append("),\n");
        }
        return enumBuilder.toString().replace(",\n$", ";");
    }

    public static String generateTranslationYamlValues(List<TagData> data) {
        StringBuilder yamlBuilder = new StringBuilder();

        for (TagData entry : data) {


            yamlBuilder
                    .append(entry.getKey())
                    .append(": ")
                    .append(entry.getTranslation())
                    .append("\n");
        }
        return yamlBuilder.toString().replace("\n$", "");
    }

    @Getter
    @RequiredArgsConstructor(staticName = "of")
    public static class TagData {
        private final Integer id;
        private final String key;
        private final String translation;
    }
}
