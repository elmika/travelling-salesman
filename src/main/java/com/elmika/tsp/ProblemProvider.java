package com.elmika.tsp;

/**
 * Port for creating a TSP problem from a problem type identifier.
 * Implementations may use predefined matrices, random generation, or external data.
 */
public interface ProblemProvider {
    Problem create(String problemType);
}
