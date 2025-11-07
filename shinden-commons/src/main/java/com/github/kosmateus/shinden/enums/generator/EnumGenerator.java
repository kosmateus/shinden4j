package com.github.kosmateus.shinden.enums.generator;

import com.github.kosmateus.shinden.enums.Tag;
import com.github.kosmateus.shinden.utils.jsoup.BaseDocumentMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static com.github.kosmateus.shinden.constants.ShindenConstants.GENERIC_ID_KEY_MATCHER;
import static com.github.kosmateus.shinden.constants.ShindenConstants.GENERIC_ID_MATCHER;
import static com.github.kosmateus.shinden.constants.ShindenConstants.SHINDEN_URL;

@Slf4j
public class EnumGenerator {
    private static final TagsMapper tagsMapper = new TagsMapper();

    public static void main(String[] args) throws IOException {
        EnumGeneratorParams params = parseArgsToParams(args);
        if (!params.isValid()) {
            log.error("Missing required arguments: {}", params.getMissingParameters());
            System.exit(1);
        }
        log.info("Starting generation for enum {}", params.className);

        Document document = Jsoup.connect(SHINDEN_URL + "/" + params.path).execute().parse();
        List<TagData> tagData = tagsMapper.mapTags(document, "/" + params.path);

        generateEnum(tagData, params);
        appendYamlTranslations(tagData, params.translationKeyPrefix, params.translationFile);
    }

    private static EnumGeneratorParams parseArgsToParams(String[] args) {
        EnumGeneratorParams p = new EnumGeneratorParams();
        for (String arg : args) {
            int idx = arg.indexOf('=');
            if (idx == -1) {
                continue;
            }
            String key = arg.substring(0, idx).trim();
            String value = arg.substring(idx + 1).trim();
            switch (key) {
                case "path":
                    p.path = value;
                    break;
                case "classPackage":
                    p.classPackage = value;
                    break;
                case "className":
                    p.className = value;
                    break;
                case "translationKeyPrefix":
                    p.translationKeyPrefix = value;
                    break;
                case "generatedSourcesPath":
                    p.generatedSourcesPath = value;
                    break;
                case "tagType":
                    p.tagType = value;
                    break;
                case "queryParameter":
                    p.queryParameter = value;
                    break;
                case "animeSearchQueryParameter":
                    p.animeSearchQueryParameter = value;
                    break;
                case "translationFile":
                    p.translationFile = value;
                    break;
                default:
                    log.warn("Unknown parameter: {}", key);
            }
        }
        return p;
    }

    private static void generateEnum(List<TagData> data, EnumGeneratorParams p) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(p.classPackage).append(";\n\n");
        sb.append("import java.util.stream.Stream;\n");
        sb.append("import lombok.Getter;\n");
        sb.append("import ").append(Tag.class.getCanonicalName()).append(";\n\n");
        sb.append("/**\n");
        sb.append(" * WARNING: This is an auto-generated class!\n");
        sb.append(" *\n");
        sb.append(" * DO NOT MODIFY this file manually. Any changes will be overwritten.\n");
        sb.append(" *\n");
        sb.append(" * To regenerate this class, run:\n");
        sb.append(" *   mvn clean compile -pl shinden-enums -am -Pgenerate-enums\n");
        sb.append(" *\n");
        sb.append(" * Generated: ").append(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME)).append("\n");
        sb.append(" */\n");
        sb.append("@Getter\n");
        sb.append("public enum ").append(p.className).append(" implements Tag {\n");

        Map<String, Integer> enumNameCount = new HashMap<>();

        boolean first = true;
        for (TagData td : data) {
            String baseEnumName = toValidEnumConstantName(td.getKey());
            String enumConstantName = baseEnumName;
            String baseTranslationKey = toTranslationKey(td.getTranslationKey());
            String fullTranslationKey = p.translationKeyPrefix + "." + baseTranslationKey;

            // Sprawdzenie i obsługa duplikatów
            if (enumNameCount.containsKey(baseEnumName)) {
                int count = enumNameCount.get(baseEnumName) + 1;
                enumNameCount.put(baseEnumName, count);
                enumConstantName = baseEnumName + "_" + count;
                fullTranslationKey = fullTranslationKey + "_" + count;
            } else {
                enumNameCount.put(baseEnumName, 1);
            }

            if (!first) {
                sb.append(",\n");
            }
            first = false;

            sb.append("    ").append(enumConstantName).append("(")
                    .append(td.getId()).append(", ")
                    .append("\"").append(fullTranslationKey).append("\"")
                    .append(")");
        }
        sb.append(";\n\n");
        sb.append("    private final Integer id;\n");
        sb.append("    private final String translationKey;\n");
        sb.append("    private final String tagType;\n");
        sb.append("    private final String queryParameter;\n");
        sb.append("    private final String animeSearchQueryParameter;\n\n");

        sb.append("    ").append(p.className).append("(Integer id, String translationKey) {\n");
        sb.append("        this.id = id;\n");
        sb.append("        this.translationKey = translationKey;\n");
        sb.append("        this.tagType = \"").append(p.tagType).append("\";\n");
        sb.append("        this.queryParameter = \"").append(p.queryParameter).append("\";\n");
        sb.append("        this.animeSearchQueryParameter = \"").append(p.animeSearchQueryParameter).append("\";\n");
        sb.append("    }\n\n");

        sb.append("    public static ").append(p.className).append(" fromValue(Integer value) {\n");
        sb.append("        return Stream.of(").append(p.className).append(".values())\n");
        sb.append("            .filter(e -> e.id.equals(value))\n");
        sb.append("            .findFirst()\n");
        sb.append("            .orElseThrow(() -> new IllegalArgumentException(\"No ")
                .append(p.className).append(" with id \" + value));\n");
        sb.append("    }\n\n");

        sb.append("    public static ").append(p.className).append(" fromValueOrNull(Integer value) {\n");
        sb.append("        return Stream.of(").append(p.className).append(".values())\n");
        sb.append("            .filter(e -> e.id.equals(value))\n");
        sb.append("            .findFirst()\n");
        sb.append("            .orElse(null);\n");
        sb.append("    }\n\n");

        sb.append("    public static ").append(p.className).append(" fromValueOrNull(String value) {\n");
        sb.append("        return fromValueOrNull(Integer.parseInt(value));\n");
        sb.append("    }\n\n");

        sb.append("    public static ").append(p.className).append(" fromValue(String value) {\n");
        sb.append("        return fromValue(Integer.parseInt(value));\n");
        sb.append("    }\n\n");

        sb.append("    @Override\n");
        sb.append("    public String getTagType() {\n");
        sb.append("        return tagType;\n");
        sb.append("    }\n\n");

        sb.append("    @Override\n");
        sb.append("    public String getQueryValue() {\n");
        sb.append("        return String.valueOf(id);\n");
        sb.append("    }\n");

        sb.append("}\n");

        File dir = new File(p.generatedSourcesPath, p.classPackage.replace('.', '/'));
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, p.className + ".java");
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write(sb.toString());
        }
        log.info("Generated enum at {}", file.getAbsolutePath());
    }

    private static void appendYamlTranslations(List<TagData> data, String prefix, String translationFile) throws IOException {
        File yamlFile = new File(translationFile);
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        options.setWidth(120);
        Yaml yaml = new Yaml(options);

        Map<String, Object> root;
        if (yamlFile.exists()) {
            try (FileInputStream fis = new FileInputStream(yamlFile);
                 InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
                Object loaded = yaml.load(isr);
                if (loaded instanceof Map) {
                    //noinspection unchecked
                    root = (Map<String, Object>) loaded;
                } else {
                    root = new LinkedHashMap<>();
                }
            }
        } else {
            yamlFile.getParentFile().mkdirs();
            yamlFile.createNewFile();
            root = new LinkedHashMap<>();
        }

        // Śledzenie duplikatów kluczy tłumaczeń
        Set<String> existingKeys = new HashSet<>();
        collectKeys(root, prefix, existingKeys);

        for (TagData td : data) {
            String baseTranslationKey = toTranslationKey(td.getTranslationKey());
            String dottedKey = prefix + "." + baseTranslationKey;
            String uniqueDottedKey = dottedKey;

            int suffix = 2;
            while (existingKeys.contains(uniqueDottedKey)) {
                uniqueDottedKey = dottedKey + "_" + suffix;
                suffix++;
            }
            existingKeys.add(uniqueDottedKey);

            String value = td.getTranslation();
            putNestedValue(root, uniqueDottedKey, value);
        }

        try (FileOutputStream fos = new FileOutputStream(yamlFile);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {
            osw.write("# WARNING: This is an auto-generated file!\n");
            osw.write("#\n");
            osw.write("# DO NOT MODIFY this file manually. Any changes will be overwritten.\n");
            osw.write("#\n");
            osw.write("# To regenerate this file, run:\n");
            osw.write("#   mvn clean compile -pl shinden-enums -am -Pgenerate-enums\n");
            osw.write("#\n");
            osw.write("# Generated: " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "\n\n");
            yaml.dump(root, osw);
        }
        log.info("Updated translations in {}", yamlFile.getAbsolutePath());
    }

    private static void collectKeys(Map<String, Object> map, String prefix, Set<String> keys) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String currentKey = prefix + "." + entry.getKey();
            if (entry.getValue() instanceof Map) {
                //noinspection unchecked
                collectKeys((Map<String, Object>) entry.getValue(), currentKey, keys);
            } else {
                keys.add(currentKey);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void putNestedValue(Map<String, Object> map, String dottedKey, String value) {
        String[] parts = dottedKey.split("\\.");
        Map<String, Object> current = map;
        for (int i = 0; i < parts.length - 1; i++) {
            if (!current.containsKey(parts[i]) || !(current.get(parts[i]) instanceof Map)) {
                current.put(parts[i], new LinkedHashMap<>());
            }
            current = (Map<String, Object>) current.get(parts[i]);
        }
        current.put(parts[parts.length - 1], value);
    }

    private static String toValidEnumConstantName(String input) {
        String tmp = input.replaceAll("[^A-Za-z0-9]+", "_").toUpperCase(Locale.ROOT);
        tmp = tmp.replaceAll("^_+", "").replaceAll("_+$", "");
        if (!tmp.isEmpty() && Character.isDigit(tmp.charAt(0))) {
            tmp = "_" + tmp;
        }
        if (tmp.isEmpty()) {
            tmp = "_UNNAMED";
        }
        return tmp;
    }

    private static String toTranslationKey(String input) {
        String tmp = input.toLowerCase(Locale.ROOT);
        tmp = tmp.replaceAll("[^a-z0-9]+", "-");
        tmp = tmp.replaceAll("-+", "-").replaceAll("^-", "").replaceAll("-$", "");
        if (tmp.isEmpty()) {
            tmp = "unnamed";
        }
        return tmp;
    }

    @ToString
    @AllArgsConstructor
    static final class EnumGeneratorParams {
        private String path;
        private String classPackage;
        private String className;
        private String translationKeyPrefix;
        private String generatedSourcesPath;
        private String tagType;
        private String queryParameter;
        private String animeSearchQueryParameter;
        private String translationFile;

        EnumGeneratorParams() {
        }

        boolean isValid() {
            return path != null && !path.isEmpty()
                    && classPackage != null && !classPackage.isEmpty()
                    && className != null && !className.isEmpty()
                    && translationKeyPrefix != null && !translationKeyPrefix.isEmpty()
                    && generatedSourcesPath != null && !generatedSourcesPath.isEmpty()
                    && tagType != null && !tagType.isEmpty()
                    && queryParameter != null && !queryParameter.isEmpty()
                    && animeSearchQueryParameter != null && !animeSearchQueryParameter.isEmpty()
                    && translationFile != null && !translationFile.isEmpty();
        }

        List<String> getMissingParameters() {
            List<String> missingParameters = new ArrayList<>();
            if (path == null || path.isEmpty()) {
                missingParameters.add("path");
            }
            if (classPackage == null || classPackage.isEmpty()) {
                missingParameters.add("classPackage");
            }
            if (className == null || className.isEmpty()) {
                missingParameters.add("className");
            }
            if (translationKeyPrefix == null || translationKeyPrefix.isEmpty()) {
                missingParameters.add("translationKeyPrefix");
            }
            if (generatedSourcesPath == null || generatedSourcesPath.isEmpty()) {
                missingParameters.add("generatedSourcesPath");
            }
            if (tagType == null || tagType.isEmpty()) {
                missingParameters.add("tagType");
            }
            if (queryParameter == null || queryParameter.isEmpty()) {
                missingParameters.add("queryParameter");
            }
            if (animeSearchQueryParameter == null || animeSearchQueryParameter.isEmpty()) {
                missingParameters.add("animeSearchQueryParameter");
            }
            if (translationFile == null || translationFile.isEmpty()) {
                missingParameters.add("translationFile");
            }
            return missingParameters;
        }
    }

    static class TagsMapper extends BaseDocumentMapper {

        public List<TagData> mapTags(Document document, String tagUrl) {
            return mapper.with(document)
                    .selectFirst("ul.tags")
                    .select("li")
                    .mapTo(element -> TagData.of(
                            mapper.with(element).selectFirst("a").attr("href").pattern(GENERIC_ID_MATCHER.apply(tagUrl)).toInteger().orThrowWithCode("id"),
                            mapper.with(element).selectFirst("a").attr("href").pattern(GENERIC_ID_KEY_MATCHER.apply(tagUrl)).orThrowWithCode("key"),
                            mapper.with(element).selectFirst("a").text().orThrowWithCode("translation"),
                            mapper.with(element).selectFirst("a").attr("href").pattern(GENERIC_ID_KEY_MATCHER.apply(tagUrl)).orThrowWithCode("translationKey")
                    ))
                    .orThrowWithCode("tags");
        }

        @Override
        protected String getMapperCode() {
            return "tags";
        }

    }

    @Getter
    @RequiredArgsConstructor(staticName = "of")
    static class TagData {
        private final Integer id;
        private final String key;
        private final String translation;
        private final String translationKey;
    }
}
