package com.elmika.tsp.application.solver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elmika.tsp.application.PermutationsIterator;
import com.elmika.tsp.domain.Problem;
import com.elmika.tsp.domain.Solution;

/**
 * Solver strategy that exhaustively evaluates all permutations (with first city fixed)
 * and returns the route with minimum total distance.
 */
public class BruteForceSolver implements SolverStrategy {

    private static final Logger log = LoggerFactory.getLogger(BruteForceSolver.class);

    @Override
    public Solution solve(Problem problem) {
        log.info("Using Brute Force algorithm to find the best route.");
        PermutationsIterator it = new PermutationsIterator(problem.getSize());
        Integer[] best = it.next();
        if (!it.hasNext()) {
            return new Solution(best, totalDistance(problem, best));
        }
        double bestDistance = totalDistance(problem, best);
        Integer[] candidate = it.next();
        while (it.hasNext()) {
            double d = totalDistance(problem, candidate);
            if (d < bestDistance) {
                best = candidate;
                bestDistance = d;
            }
            candidate = it.next();
        }
        return new Solution(best, bestDistance);
    }

    private static double totalDistance(Problem problem, Integer[] route) {
        if (route.length == 0) {
            return 0;
        }
        double total = problem.getDistance(route[route.length - 1], route[0]);
        for (int i = 1; i < route.length; i++) {
            total += problem.getDistance(route[i - 1], route[i]);
        }
        return total;
    }
}
