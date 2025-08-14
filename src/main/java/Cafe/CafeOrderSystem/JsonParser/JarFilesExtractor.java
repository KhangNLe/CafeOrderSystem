package Cafe.CafeOrderSystem.JsonParser;

import Cafe.CafeOrderSystem.Exceptions.BackendErrorException;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.jar.*;

/**
 * Utility class for accessing resource directories and JSON files packaged
 * either on the filesystem (e.g., during development in an IDE) or inside a JAR
 * (e.g., after building and packaging the application).
 * <p>
 * This class provides two main use cases:
 * <ul>
 *     <li>{@link #getDirFile(String, Class)} – ensures a persistent folder
 *     exists under the user's home directory (under "CafeOrderSystemApp") and
 *     copies default JSON files there if the folder is empty. This folder is
 *     writable and meant for storing or modifying files during application runtime.</li>
 *     <li>{@link #getDirFiles(String, Class)} – retrieves a list of JSON files
 *     from a resource directory, whether on the filesystem or inside a JAR.
 *     When reading from a JAR, the method extracts files to temporary locations
 *     so they can be accessed via {@link java.io.File} APIs.</li>
 * </ul>
 * <p>
 * Typical usage:
 * <pre>
 *     // Ensure a user folder exists with default JSON files copied
 *     File dir = JarFilesExtractor.getDirFile("config", MyClass.class);
 *
 *     // Get a list of JSON files from a resource folder (temporary files if in JAR)
 *     List&lt;File&gt; jsonFiles = JarFilesExtractor.getDirFiles("/config", MyClass.class);
 * </pre>
 *
 * <p><b>Notes:</b>
 * <ul>
 *     <li>{@code folderPath} and {@code resourceDir} are classpath-relative paths
 *     (e.g., "/myResources").</li>
 *     <li>{@link #getDirFile(String, Class)} writes files into a persistent user-home folder.</li>
 *     <li>Only files with the ".json" extension are processed by these utilities.</li>
 * </ul>
 */
public class JarFilesExtractor {

    /**
     * Private constructor to prevent instantiation.
     * This class only contains static utility methods.
     */
    private JarFilesExtractor() {
    }

    /**
     * Returns a writable {@link File} reference to a directory inside the user's
     * home folder (under "CafeOrderSystemApp/{folderPath}").
     * <p>
     * If the directory does not exist, it is created. If the directory exists but
     * is empty, this method copies default JSON files from the application's JAR
     * resources into the directory.
     * </p>
     *
     * @param folderPath  the relative folder path inside the user's home directory
     *                    (e.g., "config")
     * @param classSource a {@link Class} whose classloader is used to locate the
     *                    default resources inside the JAR
     * @param <T>         the type parameter of the class; used for resource resolution
     * @return a {@link File} pointing to the writable directory in the user's home folder
     * @throws BackendErrorException if copying default resources fails due to I/O errors
     */
    public static <T> File getDirFile(String folderPath, Class<T> classSource) {
        File userFolder = new File(System.getProperty("user.home"),
                "CafeOrderSystemApp/" + folderPath);

        if (!userFolder.exists()) {
            userFolder.mkdirs();
        }


        if (userFolder.listFiles() == null || Objects.requireNonNull(userFolder.listFiles()).length == 0) {
            List<File> defaultFiles = JarFilesExtractor.getDirFiles(folderPath, classSource);
            for (File defaultFile : defaultFiles) {
                File destination = new File(userFolder, defaultFile.getName());
                try (InputStream in = new FileInputStream(defaultFile);
                     OutputStream out = new FileOutputStream(destination)) {

                    in.transferTo(out);
                } catch (IOException e) {
                    throw new BackendErrorException(
                            String.format("Failed to copy default JSONs from %s", folderPath), e
                    );
                }
            }
        }

        return Objects.requireNonNull(userFolder);
    }

    /**
     * Returns a list of JSON files from the given resource directory, regardless
     * of whether the directory exists on the filesystem or is packaged inside a JAR.
     * <p>
     * If inside a JAR, matching JSON files are extracted to temporary files and
     * returned in the list.
     *
     * @param resourceDir classpath-relative path to the folder (e.g., {@code "/config"})
     * @param classSource a {@link Class} whose classloader will be used to locate the resource
     * @param <T>         the type parameter, used only for class reference
     * @return a list of {@link File} objects pointing to the JSON files
     * @throws BackendErrorException if the files cannot be read or extracted
     */
    public static <T> List<File> getDirFiles(String resourceDir, Class<T> classSource) {
        try {
            List<File> files = new ArrayList<>();
            URL dirURL = classSource.getResource(resourceDir);

            if (dirURL != null && dirURL.getProtocol().equals("file")) {
                File directory = new File(dirURL.toURI());
                files.addAll(Arrays.asList(
                        Objects.requireNonNull(directory.listFiles((f, name) ->
                                name.endsWith(".json")))
                ));

            } else if (dirURL != null && dirURL.getProtocol().equals("jar")) {
                String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!"));
                try (JarFile jar = new JarFile(URLDecoder.decode(jarPath, StandardCharsets.UTF_8))) {
                    for (JarEntry entry : Collections.list(jar.entries())) {
                        if (entry.getName().startsWith(resourceDir.substring(1)) &&
                                entry.getName().endsWith(".json")) {
                            File tempFile = File.createTempFile("res_", ".json");
                            tempFile.deleteOnExit();
                            try (InputStream in = jar.getInputStream(entry);
                                 OutputStream out = new FileOutputStream(tempFile)) {
                                in.transferTo(out);
                            }
                            files.add(tempFile);
                        }
                    }
                }
            }
            return files;

        } catch (Exception e) {
            throw new BackendErrorException("Failed to load resource files from " + resourceDir, e);
        }
    }
}