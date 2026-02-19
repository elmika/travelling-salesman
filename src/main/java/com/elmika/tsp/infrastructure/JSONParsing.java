package com.elmika.tsp.infrastructure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elmika.tsp.application.ConfigLoadException;
import com.elmika.tsp.application.ProblemConfiguration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONParsing {

    private static final Logger log = LoggerFactory.getLogger(JSONParsing.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Reads a JSON file into a JsonNode tree.
     *
     * @param filename the path to the JSON file
     * @return the parsed JsonNode, or null if the file cannot be read or parsed
     */
    public static JsonNode read(String filename) {
        Path path = Paths.get(filename);
        if (!Files.exists(path)) {
            log.warn("Could not find json file: {}", filename);
            return null;
        }
        try {
            return MAPPER.readTree(path.toFile());
        } catch (IOException e) {
            log.warn("Exception when reading json file {}: {}", filename, e.getMessage());
            return null;
        }
    }

    /**
     * Loads configuration from problemConfiguration.json.
     *
     * @throws ConfigLoadException if the file is missing or malformed
     */
    public static ProblemConfiguration getConfig() {
        return getConfig(Paths.get("problemConfiguration.json"));
    }

    /**
     * Loads configuration from the given path.
     *
     * @param path the path to the configuration file
     * @return the configuration
     * @throws ConfigLoadException if the file is missing or malformed
     */
    public static ProblemConfiguration getConfig(Path path) {
        if (!Files.exists(path)) {
            throw new ConfigLoadException("Configuration file not found: " + path.toAbsolutePath());
        }

        ProblemConfigFile configFile;
        try {
            configFile = MAPPER.readValue(path.toFile(), ProblemConfigFile.class);
        } catch (IOException e) {
            throw new ConfigLoadException("Failed to parse configuration file " + path + ": " + e.getMessage(), e);
        }

        String problem = configFile.getProblem();
        String strategy = configFile.getResolutionStrategy();
        if (problem == null || problem.isBlank()) {
            problem = "simple";
        } else {
            problem = problem.trim();
        }
        if (strategy == null || strategy.isBlank()) {
            strategy = "random10";
        } else {
            strategy = strategy.trim();
        }

        log.info("Problem: {}, Strategy: {}", problem, strategy);
        return new ProblemConfiguration(problem, strategy);
    }
}
