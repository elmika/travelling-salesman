package com.elmika.tsp.application.solver;

import com.elmika.tsp.domain.Problem;
import com.elmika.tsp.domain.Solution;

/**
 * Strategy for solving a single TSP problem.
 * Implementations may use brute-force, random search, or other algorithms.
 */
public interface SolverStrategy {
    Solution solve(Problem problem);
}
