package com.elmika.tsp.application.resolution;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elmika.tsp.domain.problem.Problem;
import com.elmika.tsp.domain.solution.Solution;

/**
 * Solver strategy that uses random permutation search.
 * With iterations=1 returns a single random route; otherwise returns the best of that many random trials.
 */
public class RandomSolver implements ResolutionStrategy {

    private static final Logger log = LoggerFactory.getLogger(RandomSolver.class);

    private final int iterations;

    public RandomSolver(int iterations) {
        if (iterations < 1) {
            throw new IllegalArgumentException("Iterations must be at least 1.");
        }
        this.iterations = iterations;
    }

    @Override
    public Solution solve(Problem problem) {
        if (iterations == 1) {
            log.info("Finding one random solution.");
            Integer[] route = findRandomSolution(problem);
            return new Solution(route, SolverUtils.totalDistance(problem, route));
        }
        log.info("Comparing {} random solutions to find the best route.", iterations);
        Integer[] best = findBestRandomSolution(problem, iterations);
        return new Solution(best, SolverUtils.totalDistance(problem, best));
    }

    private static Integer[] findRandomSolution(Problem problem) {
        int n = problem.getSize();
        Integer[] solution = new Integer[n];
        for (int i = 0; i < n; i++) {
            solution[i] = i + 1;
        }
        List<Integer> list = Arrays.asList(solution);
        Collections.shuffle(list);
        return list.toArray(new Integer[0]);
    }

    private static Integer[] findBestRandomSolution(Problem problem, int iterations) {
        Integer[] solution = findRandomSolution(problem);
        double distance = SolverUtils.totalDistance(problem, solution);
        for (int i = 0; i < iterations; i++) {
            Integer[] candidate = findRandomSolution(problem);
            double candidateDistance = SolverUtils.totalDistance(problem, candidate);
            if (candidateDistance < distance) {
                solution = candidate;
                distance = candidateDistance;
            }
        }
        return solution;
    }
}
