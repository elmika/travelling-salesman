package com.elmika.tsp;

/**
 * Driven adapter for ConfigLoader. Loads configuration from problemConfiguration.json
 * via JSONParsing. Delegates to existing file-based logic.
 */
public class JsonFileConfigLoader implements ConfigLoader {

    @Override
    public ProblemConfiguration loadConfiguration() {
        return JSONParsing.getConfig();
    }
}
