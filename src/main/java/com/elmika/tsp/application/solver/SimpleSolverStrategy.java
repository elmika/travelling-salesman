package com.elmika.tsp.application.solver;

import com.elmika.tsp.application.SimpleSolver;
import com.elmika.tsp.domain.Problem;
import com.elmika.tsp.domain.Solution;

/**
 * Delegates to SimpleSolver with a fixed strategy name.
 *
 * @deprecated SolveTspUseCase uses BruteForceSolver and RandomSolver directly.
 *             Use those strategies instead.
 */
@Deprecated
public class SimpleSolverStrategy implements SolverStrategy {

    private final String strategyName;

    public SimpleSolverStrategy(String strategyName) {
        this.strategyName = strategyName;
    }

    @Override
    public Solution solve(Problem problem) {
        SimpleSolver solver = new SimpleSolver(problem);
        return solver.findSolution(strategyName);
    }
}
