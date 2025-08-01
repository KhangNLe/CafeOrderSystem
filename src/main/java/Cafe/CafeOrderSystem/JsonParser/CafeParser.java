package Cafe.CafeOrderSystem.JsonParser;

import Cafe.CafeOrderSystem.Exceptions.InvalidInputException;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages a collection of {@link Parsers} instances and coordinates their lifecycle.
 * <p>
 * This class serves as a central orchestrator that can start and end all registered parsers.
 * It's especially useful in contexts like batch jobs, ETL pipelines, or modular loaders
 * where multiple parser components need to be started and stopped together.
 * </p>
 *
 * <p>Parsers can be added before or after the shop is "opened". If added after startup,
 * the parser will not be automatically started unless you explicitly handle it.</p>
 */
public class CafeParser {
    List<Parsers> cafeMenuParser;

    /**
     * Constructs a new {@code CafeParser} with no parsers registered.
     */
    public CafeParser(){
        cafeMenuParser = new ArrayList<>();
    }

    /**
     * Starts all registered parsers by calling their {@link Parsers#startCollection()} method.
     * <p>
     *     This is typically called once at application start up.
     * </p>
     */
    public void openCafeShop(){
        cafeMenuParser.forEach(Parsers::startCollection);
    }

    /**
     * Registers a new parser to be managed
     *
     * @param parser the parser to add
     */
    public void addParser(Parsers parser){
        cafeMenuParser.add(parser);
    }

    /**
     * End all registered parsers by calling their {@link Parsers#endCollection()} method
     * <p>
     *     This is typically called during shutdown.
     * </p>
     */
    public void closeShop(){
        cafeMenuParser.forEach(Parsers::endCollection);
    }

    /**
     * Retrieves a registered parser by its concrete type
     *
     * @param type the class of the parser to retrieve
     * @param <T> the specific subtype of {@link Parsers}
     * @return the matching parser instance
     * @throws InvalidInputException if no parser of the specified type is found
     */
    public <T extends Parsers> T getParserType(Class<T> type){
        return cafeMenuParser.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .findFirst()
                .orElseThrow(() -> new InvalidInputException(
                        String.format("No parser found for type %s", type.getSimpleName())
                ));
    }
}
