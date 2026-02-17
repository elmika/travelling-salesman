package com.elmika.tsp.application;

import com.elmika.tsp.domain.Problem;
import com.elmika.tsp.domain.Solution;

/**
 * Inbound port for solving a TSP problem with a given strategy.
 * Strategies: "brute-force", "random", "random10", "random100".
 */
public interface TspSolver {
    Solution solve(Problem problem, String strategy);
}
