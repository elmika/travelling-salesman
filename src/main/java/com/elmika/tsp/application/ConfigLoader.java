package com.elmika.tsp.application;

/**
 * Port for loading problem configuration (problem type and resolution strategy).
 * Implementations may read from file, environment, or in-memory defaults.
 */
public interface ConfigLoader {
    ProblemConfiguration loadConfiguration();
}
