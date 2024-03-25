package kit.organiser.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Utility class that parses a text file and retrieves program arguments.
 * @author ukgyh
 */
public final class TextFileParser {
    private static final String INSTANTIATION_ERROR = "Utility class cannot be instantiated";
    private static final String FILE_MISSING_ERROR = "file does not exist: %s";
    private static final String FILE_UNREADABLE_ERROR = "file is not readable: %s";

    private TextFileParser() {
        throw new UnsupportedOperationException(INSTANTIATION_ERROR);
    }

    /**
     * Parses each line in a text file into a list of arguments.
     * @param path path to the file
     * @return list of arguments
     * @throws IOException if an I/O error occurs
     */
    public static List<String> parseFile(String path) throws IOException {
        Path filePath = Paths.get(path);

        // Validate the input path
        if (!Files.exists(filePath)) {
            throw new IOException(FILE_MISSING_ERROR.formatted(path));
        }
        if (!Files.isReadable(filePath)) {
            throw new IOException(FILE_UNREADABLE_ERROR.formatted(path));
        }

        // Now that the path has been validated, read and return the lines
        return Files.readAllLines(filePath);
    }
}
