package Cafe.CafeOrderSystem.JsonParser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;

import java.io.*;
import java.util.*;

public class ItemsParser {
    private final ObjectMapper MAPPER;

    public ItemsParser() {
        MAPPER = new ObjectMapper();
    }

    public <T> List<T> readFile(File file, TypeReference<List<T>> reference){
        try{
            return MAPPER.readValue(file, reference);
        } catch (IOException e) {
            throw new IllegalArgumentException(
                    String.format("Error while reading file '%s', reason: %s", file.getAbsolutePath(), e.getMessage()
            ));
        }
    }

    public <T> void writeFile(File file, List<T> items){
        try {
            SequenceWriter writer = MAPPER.writerWithDefaultPrettyPrinter().writeValues(file);
            writer.write(items);
            writer.close();
        } catch (IOException e) {
            throw new IllegalArgumentException(
                    String.format("Failed to write pending order to file '%s', reason: %s",
                            file.getAbsolutePath(), e.getMessage())
            );
        }
    }

    public <T> void appendToFile(File file, T item){
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            String jsonData = MAPPER.writeValueAsString(item);

            if (file.length() == 0) {
                raf.writeBytes("[\n" + jsonData + "\n]");
            } else {
                raf.seek(file.length() - 2);
                raf.writeBytes("\n," + jsonData + "\n]");
            }

        } catch (FileNotFoundException e){
            throw new IllegalArgumentException(
                    String.format("File %s not found", file.getAbsolutePath())
            );
        } catch (IOException e){
            throw new IllegalArgumentException(
                    "Error while writing order to json file: " + e.getMessage()
            );
        }
    }
}
