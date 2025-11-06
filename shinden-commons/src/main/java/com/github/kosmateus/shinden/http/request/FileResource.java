package com.github.kosmateus.shinden.http.request;

import java.io.IOException;
import java.io.InputStream;

/**
 * Represents a file resource to be used in HTTP requests.
 * <p>
 * The {@code FileResource} interface defines methods for accessing file-related data,
 * such as obtaining the file's name, content as an {@link InputStream}, and the file's size.
 * Implementations of this interface can be used to represent files that are uploaded
 * as part of an HTTP request.
 * </p>
 *
 * @version 1.0.0
 */
public interface FileResource {

    /**
     * Returns the name of the file.
     * <p>
     * This method provides the file name, which can be used, for example, as part of
     * a multipart HTTP request where the file is being uploaded.
     * </p>
     *
     * @return the name of the file as a {@link String}.
     */
    String getFileName();

    /**
     * Returns an {@link InputStream} representing the content of the file.
     * <p>
     * This method provides access to the file's content, which can be read and used
     * in HTTP requests. The method may throw an {@link IOException} if an I/O error occurs
     * while trying to obtain the stream.
     * </p>
     *
     * @return an {@link InputStream} containing the file's content.
     * @throws IOException if an I/O error occurs while obtaining the input stream.
     */
    InputStream getInputStream() throws IOException;

    /**
     * Returns the size of the file in bytes.
     * <p>
     * This method provides the size of the file, which can be useful for validating
     * the file size before uploading or processing it.
     * </p>
     *
     * @return the size of the file in bytes as a {@code long}.
     */
    long getSize();
}
