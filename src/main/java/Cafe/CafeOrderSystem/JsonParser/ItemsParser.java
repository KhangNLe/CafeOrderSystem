package Cafe.CafeOrderSystem.JsonParser;

import Cafe.CafeOrderSystem.Exceptions.BackendErrorException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;

import java.io.*;
import java.util.*;

/**
 * Utility class for reading and writing lists of objects to and from JSON files using Jackson.
 * <p>
 * Supports deserializing a list of objects from a file, serializing a list of objects to a file,
 * and appending a single item to a file containing a list.
 * </p>
 *
 * <p>This class is not thread-safe.</p>
 */
public class ItemsParser {
    private final ObjectMapper mapper;

    /**
     * Construct a new {@code ItemsParser} instance using a mapper created by {@code MapperFactory}.
     */
    public ItemsParser() {
        mapper = MapperFactory.createMapper();
    }

    /**
     * Read a JSON file and deserializes it into a list of objects of the specified type
     *
     * @param file the JSON file to read from
     * @param type the Object Class type
     * @param <T> the type of objects in the list
     * @return a list of deserialized objects
     * @throws BackendErrorException if reading or parsing file fails
     */
    public <T> List<T> readFile(File file, Class<T> type){
        try{
            return mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, type));
        } catch (IOException e) {
            throw new BackendErrorException(
                    String.format("Error while reading file '%s', reason: %s", file.getAbsolutePath(), e.getMessage()
            ));
        }
    }

    /**
     * Serialize a list of objects to a JSON file with pretty-printing.
     * <p>This method overwrites the file if it already exists</p>
     *
     * @param file the file to write to
     * @param items the list of items to serialize
     * @param <T> the type of object in the list
     * @throws BackendErrorException if writing the file fails
     */
    public <T> void writeFile(File file, List<T> items){
        try {
            if (items.isEmpty()){
                try (Writer writer = new FileWriter(file)) {
                    writer.write("[ ]");
                }
            } else {
                SequenceWriter writer = mapper.writerWithDefaultPrettyPrinter().writeValues(file);
                writer.write(items);
                writer.close();
            }
        } catch (IOException e) {
            throw new BackendErrorException(
                    String.format("Failed to write pending order to file '%s', reason: %s",
                            file.getAbsolutePath(), e.getMessage()), e
            );
        }
    }

    /**
     * Appends a single object to an existing JSON file that contains a list of similar object
     * <p>
     *     If the file does not exist or is empty, a new list is created
     * </p>
     *
     * @param file the JSON file to append to
     * @param item the item to append
     * @param classType the Object Class type
     * @param <T> the type of the item
     * @throws BackendErrorException if reading or writing to the file fails
     */
    public <T> void appendToFile(File file, T item, Class<T> classType){
       List<T> current;

       if (file.length() > 0){
           try {
               JavaType type = mapper.getTypeFactory()
                       .constructCollectionType(List.class, classType);
               current = mapper.readValue(file, type);
           } catch (IOException e) {
               throw new BackendErrorException(
                       String.format("Failed to read file %s before appending. Reason: %s",
                               file.getAbsolutePath(), e.getMessage())
               );
           }
       } else {
           current = new ArrayList<>();
       }
       current.add(item);
       writeFile(file, current);
    }
}