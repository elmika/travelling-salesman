package com.elmika.tsp.application;

import java.util.Collections;
import java.util.List;

import com.elmika.tsp.domain.problem.Problem;

/**
 * Port for creating a TSP problem from a problem type identifier.
 * Implementations may use predefined matrices, random generation, or external data.
 */
public interface ProblemProvider {
    Problem create(String problemType);

    /** Returns the names of all available TSPLIB problems (e.g. {@code "tsplib-berlin52"}). */
    default List<String> tsplibNames() {
        return Collections.emptyList();
    }
}
