package com.elmika.tsp.application;

import com.elmika.tsp.domain.problem.Problem;
import com.elmika.tsp.domain.solution.Solution;

/**
 * Inbound port for solving a TSP problem with a given strategy.
 */
public interface TspSolver {
    Solution solve(Problem problem, String strategy);
}
