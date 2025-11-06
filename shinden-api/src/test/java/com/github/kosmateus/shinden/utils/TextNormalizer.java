package com.github.kosmateus.shinden.utils;

import java.text.Normalizer;

public class TextNormalizer {

    public static String normalizeForKey(String key) {
        return normalizeCommon(key.replaceAll("_", "-")).toLowerCase();
    }

    public static String normalizeForEnumName(String name) {
        String normalized = normalizeCommon(name).replaceAll("-", "_").toUpperCase();
        if (Character.isDigit(normalized.charAt(0))) {
            normalized = "_" + normalized;
        }
        return normalized;
    }

    private static String normalizeCommon(String input) {
        return Normalizer.normalize(input
                        .replaceAll("\\s+", "_")
                        .replaceAll("&", "_and_")
                        .replaceAll("[\"']", "")
                        .replaceAll("\\.", "")
                        .replaceAll("/", "_")
                        .replaceAll("\\+", "_plus_")
                        .replaceAll(";", "")
                        .replaceAll("[\\[\\]()]", "")
                        .replaceAll("!", "")
                        .replaceAll("\\?", "")
                        .replaceAll(":", "")
                        .replaceAll("[<>]", "")
                        .replaceAll("[@#%^*_{}|\\\\=]", "")
                        .replaceAll("_+", "_"), Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
