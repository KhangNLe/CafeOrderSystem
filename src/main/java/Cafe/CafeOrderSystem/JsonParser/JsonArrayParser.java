package Cafe.CafeOrderSystem.JsonParser;

import com.fasterxml.jackson.databind.*;

import java.util.*;
import java.io.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class JsonArrayParser {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonArrayParser() {}

    public static List<JsonNode> parse(String filePath){
        File file = validateFile(filePath);

        try {
            JsonNode rootNode = MAPPER.readTree(file);

            if (!rootNode.isArray()) {
                throw new IllegalArgumentException("Json file is not an array");
            }

            return StreamSupport.stream(rootNode.spliterator(), false).collect(Collectors.toList());

        } catch (IOException e){
            throw new IllegalArgumentException(
                    String.format("Failed to parse json file: %s. Reason: %s", filePath, e.getMessage()), e
            );
        }
    }

    private static File validateFile(String filePath){
        if (filePath == null || filePath.isEmpty()){
            throw new IllegalArgumentException("File path cannot be null or empty");
        }

        File file = new File(filePath);

        if (!file.exists()){
            throw new IllegalArgumentException("File does not exist for path: " + filePath);
        }

        if (!file.canRead()){
            throw new IllegalArgumentException("File is not readable for path: " + filePath);
        }

        return file;
    }
}
