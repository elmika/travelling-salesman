package com.elmika.tsp.infrastructure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.elmika.tsp.application.ProblemConfiguration;

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
     * Loads configuration from problemConfiguration.json into a typed DTO,
     * falling back to defaults when the file is missing or malformed.
     */
    public static ProblemConfiguration getConfig() {
        String filename = "problemConfiguration.json";
        String problem = "simple";
        String strategy = "random10";

        Path path = Paths.get(filename);
        if (!Files.exists(path)) {
            log.info("Default configuration loaded");
            return new ProblemConfiguration(problem, strategy);
        }

        ProblemConfigFile configFile;
        try {
            configFile = MAPPER.readValue(path.toFile(), ProblemConfigFile.class);
        } catch (IOException e) {
            log.warn("Exception when parsing config file {}: {}", filename, e.getMessage());
            log.info("Default configuration loaded");
            return new ProblemConfiguration(problem, strategy);
        }

        if (configFile.getProblem() != null && !configFile.getProblem().isBlank()) {
            problem = configFile.getProblem();
        }
        if (configFile.getResolutionStrategy() != null && !configFile.getResolutionStrategy().isBlank()) {
            strategy = configFile.getResolutionStrategy();
        }

        log.info("Problem: {}, Strategy: {}", problem, strategy);
        return new ProblemConfiguration(problem, strategy);
    }
}
