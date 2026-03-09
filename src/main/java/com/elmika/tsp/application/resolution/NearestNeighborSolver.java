package com.elmika.tsp.application.resolution;

import com.elmika.tsp.application.solver.SolverUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elmika.tsp.domain.problem.Problem;
import com.elmika.tsp.domain.solution.Solution;

/**
 * Solver strategy that uses the nearest-neighbor heuristic.
 * Starting from city 1, repeatedly visits the closest unvisited city.
 * Runs in O(n²) time; solution quality is typically within 20-25% of optimal.
 */
public class NearestNeighborSolver implements ResolutionStrategy {

    private static final Logger log = LoggerFactory.getLogger(NearestNeighborSolver.class);

    @Override
    public Solution solve(Problem problem) {
        log.info("Using Nearest Neighbor heuristic to find a route.");
        int n = problem.getSize();
        Integer[] route = new Integer[n];
        boolean[] visited = new boolean[n + 1]; // 1-indexed cities

        route[0] = 1;
        visited[1] = true;

        for (int step = 1; step < n; step++) {
            int current = route[step - 1];
            int nearest = -1;
            double nearestDist = Double.MAX_VALUE;
            for (int city = 1; city <= n; city++) {
                if (!visited[city]) {
                    double d = problem.getDistance(current, city);
                    if (d < nearestDist) {
                        nearestDist = d;
                        nearest = city;
                    }
                }
            }
            route[step] = nearest;
            visited[nearest] = true;
        }

        return new Solution(route, SolverUtils.totalDistance(problem, route));
    }
}
