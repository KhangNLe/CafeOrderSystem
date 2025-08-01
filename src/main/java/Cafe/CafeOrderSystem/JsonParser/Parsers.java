package Cafe.CafeOrderSystem.JsonParser;

/**
 * Defines the contract for parser components that participate in a start-end lifecycle.
 * <p>
 * This interface is designed for components that need to perform setup and teardown logic,
 * such as loading or saving data, opening and closing file streams, or initializing state.
 * </p>
 *
 * <p>Used by {@link CafeParser} to coordinate the lifecycle of multiple parsers.</p>
 */
public interface Parsers {

    /**
     * Starts the collection or initialization phase for the parser
     * <p>
     *     This method is called once at startup.
     *     Implementations may use this to load data, prepare resources, or reset internal state
     * </p>
     */
    void startCollection();

    /**
     * Ends the collection or finalization phase for the parser
     * <p>
     *     This method is called during the shutdown.
     *     Implementation may use this to write output, clean up resources.
     * </p>
     */
    void endCollection();
}
