package com.elmika.tsp.application.improvement;

import com.elmika.tsp.domain.problem.Problem;
import com.elmika.tsp.domain.solution.Solution;

/**
 * Strategy for improving an existing TSP solution.
 */
public interface ImprovementStrategy {
    Solution improve(Problem problem, Solution initial);
}
