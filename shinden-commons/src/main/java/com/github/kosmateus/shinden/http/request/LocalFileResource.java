package com.github.kosmateus.shinden.http.request;

import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * Represents a local file resource for use in HTTP requests.
 * <p>
 * The {@code LocalFileResource} class wraps a {@link File} object and provides methods
 * to access the file's name, its content as an {@link InputStream}, and its size.
 * This class is typically used when a file needs to be uploaded as part of an HTTP request.
 * </p>
 *
 * @version 1.0.0
 */
@RequiredArgsConstructor(staticName = "of")
public class LocalFileResource implements FileResource {

    private final File file;

    /**
     * Returns the name of the file.
     *
     * @return the file name as a {@link String}.
     */
    @Override
    public String getFileName() {
        return file.getName();
    }

    /**
     * Returns an {@link InputStream} to read the file's content.
     * <p>
     * The returned stream allows for reading the file's data in a sequential manner.
     * </p>
     *
     * @return an {@link InputStream} for the file.
     * @throws IOException if an I/O error occurs when opening the file.
     */
    @Override
    public InputStream getInputStream() throws IOException {
        return Files.newInputStream(file.toPath());
    }

    /**
     * Returns the size of the file in bytes.
     *
     * @return the file size as a {@code long} value.
     */
    @Override
    public long getSize() {
        return file.length();
    }
}
