package com.elmika.tsp.application;

import com.elmika.tsp.application.solver.SolverStrategy;
import com.elmika.tsp.application.solver.SimpleSolverStrategy;
import com.elmika.tsp.domain.Problem;
import com.elmika.tsp.domain.Solution;

/**
 * Application use case implementing TspSolver.
 * Resolves the strategy name to a SolverStrategy and delegates to it.
 */
public class SolveTspUseCase implements TspSolver {

    @Override
    public Solution solve(Problem problem, String strategy) {
        SolverStrategy solverStrategy = resolveStrategy(strategy);
        return solverStrategy.solve(problem);
    }

    private static SolverStrategy resolveStrategy(String strategy) {
        return new SimpleSolverStrategy(strategy != null ? strategy : "random10");
    }
}
