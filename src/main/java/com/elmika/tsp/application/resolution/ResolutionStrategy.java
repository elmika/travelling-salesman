package com.elmika.tsp.application.resolution;

import com.elmika.tsp.domain.problem.Problem;
import com.elmika.tsp.domain.solution.Solution;

/**
 * Strategy for producing an initial TSP solution from scratch.
 */
public interface ResolutionStrategy {
    Solution solve(Problem problem);
}
