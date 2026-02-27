package com.elmika.tsp.application.solver;

import com.elmika.tsp.domain.Problem;

/**
 * Shared utility methods for solver implementations.
 */
public final class SolverUtils {

    private SolverUtils() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    /**
     * Calculates the total distance of a tour, including the return edge to the starting city.
     *
     * @param problem the TSP problem instance
     * @param route   the tour as an array of city indices
     * @return the total distance of the tour
     */
    public static double totalDistance(Problem problem, Integer[] route) {
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
