package com.elmika.tsp;

/**
 * Application use case implementing TspSolver.
 * Delegates to SimpleSolver for the actual solving logic.
 * No I/O; pure orchestration.
 */
public class SolveTspUseCase implements TspSolver {

    @Override
    public Solution solve(Problem problem, String strategy) {
        SimpleSolver solver = new SimpleSolver(problem);
        return solver.findSolution(strategy);
    }
}
