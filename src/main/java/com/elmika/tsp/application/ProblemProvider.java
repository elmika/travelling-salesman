package com.elmika.tsp.application;

import com.elmika.tsp.domain.Problem;

/**
 * Port for creating a TSP problem from a problem type identifier.
 * Implementations may use predefined matrices, random generation, or external data.
 */
public interface ProblemProvider {
    Problem create(String problemType);
}
