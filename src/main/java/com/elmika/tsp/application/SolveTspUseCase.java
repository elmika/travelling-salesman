package com.elmika.tsp.application;

import com.elmika.tsp.application.solver.BruteForceSolver;
import com.elmika.tsp.application.solver.NearestNeighborSolver;
import com.elmika.tsp.application.solver.RandomSolver;
import com.elmika.tsp.application.solver.SolverStrategy;
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
        String name = strategy != null ? strategy : "random10";
        switch (name) {
            case "random":
                return new RandomSolver(1);
            case "random10":
                return new RandomSolver(10);
            case "random100":
                return new RandomSolver(100);
            case "brute-force":
                return new BruteForceSolver();
            case "nearest-neighbor":
                return new NearestNeighborSolver();
            default:
                return new RandomSolver(10);
        }
    }
}
