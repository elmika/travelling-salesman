package com.elmika.tsp.application.resolution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elmika.tsp.domain.problem.Problem;
import com.elmika.tsp.domain.solution.Solution;

/**
 * Solver strategy that exhaustively evaluates all permutations (with first city fixed)
 * and returns the route with minimum total distance.
 */
public class BruteForceSolver implements ResolutionStrategy {

    private static final Logger log = LoggerFactory.getLogger(BruteForceSolver.class);

    @Override
    public Solution solve(Problem problem) {
        log.info("Using Brute Force algorithm to find the best route.");
        PermutationsIterator it = new PermutationsIterator(problem.getSize());
        Integer[] best = it.next();
        if (!it.hasNext()) {
            return new Solution(best, SolverUtils.totalDistance(problem, best));
        }
        double bestDistance = SolverUtils.totalDistance(problem, best);
        Integer[] candidate = it.next();
        while (it.hasNext()) {
            double d = SolverUtils.totalDistance(problem, candidate);
            if (d < bestDistance) {
                best = candidate;
                bestDistance = d;
            }
            candidate = it.next();
        }
        return new Solution(best, bestDistance);
    }
}
