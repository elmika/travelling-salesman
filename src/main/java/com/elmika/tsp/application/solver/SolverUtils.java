package com.elmika.tsp.application.solver;

import com.elmika.tsp.domain.problem.Problem;

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

    /**
     * Reverses a segment of the route in-place.
     *
     * @param route the route array to modify
     * @param from  the starting index (inclusive)
     * @param to    the ending index (inclusive)
     */
    public static void reverse(Integer[] route, int from, int to) {
        while (from < to) {
            Integer tmp = route[from];
            route[from++] = route[to];
            route[to--] = tmp;
        }
    }

    /**
     * Computes the distance improvement from a 2-opt swap at positions i and j.
     * Returns the distance saved by reversing route[i+1..j].
     *
     * <p>A positive value means the swap shortens the tour:
     * <pre>
     *   old edges: (route[i], route[i+1]) and (route[j], route[j+1 mod n])
     *   new edges: (route[i], route[j])   and (route[i+1], route[j+1 mod n])
     * </pre>
     *
     * @param problem the TSP problem instance
     * @param route   the current tour
     * @param i       the first edge position (before the segment to reverse)
     * @param j       the second edge position (end of the segment to reverse)
     * @return the distance improvement (positive = better)
     */
    public static double twoOptImprovement(Problem problem, Integer[] route, int i, int j) {
        int n = route.length;
        int a = route[i],         b = route[(i + 1) % n];
        int c = route[j],         d = route[(j + 1) % n];
        return problem.getDistance(a, b) + problem.getDistance(c, d)
             - problem.getDistance(a, c) - problem.getDistance(b, d);
    }
}
