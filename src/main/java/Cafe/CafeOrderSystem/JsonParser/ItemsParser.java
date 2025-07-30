package Cafe.CafeOrderSystem.JsonParser;

import Cafe.CafeOrderSystem.Exceptions.BackendErrorException;
import Cafe.CafeOrderSystem.Inventory.Ingredients.Ingredient;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.*;
import java.util.*;

public class ItemsParser {
    private final ObjectMapper mapper;

    public ItemsParser() {
        mapper = MapperFactory.createMapper();
    }

    public <T> List<T> readFile(File file, Class<T> type){
        try{
            return mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, type));
        } catch (IOException e) {
            throw new BackendErrorException(
                    String.format("Error while reading file '%s', reason: %s", file.getAbsolutePath(), e.getMessage()
            ));
        }
    }

    public <T> void writeFile(File file, List<T> items){
        try {
            SequenceWriter writer = mapper.writerWithDefaultPrettyPrinter().writeValues(file);
            writer.write(items);
            writer.close();
        } catch (IOException e) {
            throw new BackendErrorException(
                    String.format("Failed to write pending order to file '%s', reason: %s",
                            file.getAbsolutePath(), e.getMessage())
            );
        }
    }

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
