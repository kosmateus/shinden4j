package com.github.kosmateus.shinden.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourceUtils {

    public static File loadFile(String fileName) throws IOException {
        URL resourceUrl = getResourceAsURL(fileName);
        if (resourceUrl == null) {
            throw new IOException("File not found: " + fileName);
        }
        return new File(resourceUrl.getFile());
    }

    public static void saveFile(String fileName, String content) {
        try {
            Path resourceDirectory = getResourceDirectoryPath();
            Path filePath = resourceDirectory.resolve(fileName);
            if (filePath.getParent() != null) {
                Files.createDirectories(filePath.getParent());
            }
            Files.write(filePath, content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file: " + fileName, e);
        }

    }

    private static Path getResourceDirectoryPath() throws IOException {
        URL resourceUrl = ResourceUtils.class.getClassLoader().getResource(".");
        if (resourceUrl == null) {
            throw new IOException("Resource directory not found");
        }
        try {
            return Paths.get(resourceUrl.toURI());
        } catch (URISyntaxException e) {
            throw new IOException("Invalid URI for resource directory", e);
        }
    }

    private static URL getResourceAsURL(String fileName) {
        return ResourceUtils.class.getClassLoader().getResource(fileName);
    }
}
