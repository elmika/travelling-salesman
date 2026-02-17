package com.elmika.tsp.infrastructure;

import com.elmika.tsp.application.ConfigLoader;
import com.elmika.tsp.application.ProblemConfiguration;

/**
 * Driven adapter for ConfigLoader. Loads configuration from problemConfiguration.json
 * via JSONParsing.
 */
public class JsonFileConfigLoader implements ConfigLoader {

    @Override
    public ProblemConfiguration loadConfiguration() {
        return JSONParsing.getConfig();
    }
}
