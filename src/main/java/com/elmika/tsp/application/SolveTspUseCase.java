package com.elmika.tsp.application;

import com.elmika.tsp.domain.Problem;
import com.elmika.tsp.domain.Solution;

/**
 * Application use case implementing TspSolver.
 * Delegates to SimpleSolver for the actual solving logic.
 */
public class SolveTspUseCase implements TspSolver {

    @Override
    public Solution solve(Problem problem, String strategy) {
        SimpleSolver solver = new SimpleSolver(problem);
        return solver.findSolution(strategy);
    }
}
