package com.elmika.tsp.application.resolution;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.elmika.tsp.application.improvement.TwoOptSolver;
import com.elmika.tsp.domain.problem.DistanceMatrixProblem;
import com.elmika.tsp.domain.problem.Problem;
import com.elmika.tsp.domain.solution.Solution;

public class GreedyEdgeSolverTest {

    /**
     * 4-city square where the 4 short perimeter edges (distance 2) all appear before
     * the 2 long diagonal edges (distance 5).
     *
     * Greedy picks the four short edges first, which happen to form the optimal tour.
     *
     *   distances:  sides = 2,  diagonals = 5
     */
    @Test
    public void buildsOptimalTourWhenShortEdgesFormPerimeter() {
        double[][] distances = {
            {0, 2, 5, 2},
            {2, 0, 2, 5},
            {5, 2, 0, 2},
            {2, 5, 2, 0}
        };
        Problem problem = new DistanceMatrixProblem(distances);

        Solution solution = new GreedyEdgeSolver().solve(problem);

        assertEquals(8.0, solution.getTotalDistance());
        assertEquals(4, solution.getRoute().length);
    }

    /**
     * Every city must appear in the route exactly once.
     */
    @Test
    public void routeVisitsEachCityExactlyOnce() {
        double[][] distances = {
            {0, 1, 4, 7},
            {1, 0, 2, 5},
            {4, 2, 0, 3},
            {7, 5, 3, 0}
        };
        Problem problem = new DistanceMatrixProblem(distances);

        Solution solution = new GreedyEdgeSolver().solve(problem);

        Integer[] route = solution.getRoute();
        Set<Integer> cities = new HashSet<>(Arrays.asList(route));
        assertEquals(4, cities.size(), "each city should appear exactly once");
        assertTrue(cities.contains(1));
        assertTrue(cities.contains(2));
        assertTrue(cities.contains(3));
        assertTrue(cities.contains(4));
    }

    /**
     * Greedy edge stacked with 2-opt should be no worse than greedy edge alone.
     */
    @Test
    public void greedyEdgeWithTwoOptDoesNotWorsen() {
        double[][] distances = {
            {0, 3, 1, 4},
            {3, 0, 2, 5},
            {1, 2, 0, 6},
            {4, 5, 6, 0}
        };
        Problem problem = new DistanceMatrixProblem(distances);
        GreedyEdgeSolver greedy = new GreedyEdgeSolver();

        double greedyDistance = greedy.solve(problem).getTotalDistance();
        double twoOptDistance = new TwoOptSolver(greedy).solve(problem).getTotalDistance();

        assertTrue(twoOptDistance <= greedyDistance + 1e-9,
            "2-opt on greedy (" + twoOptDistance + ") should be <= greedy alone (" + greedyDistance + ")");
    }

    /**
     * Single-city problem: trivial tour with distance 0.
     */
    @Test
    public void handlesSingleCity() {
        double[][] distances = {{0}};
        Problem problem = new DistanceMatrixProblem(distances);

        Solution solution = new GreedyEdgeSolver().solve(problem);

        assertEquals(1, solution.getRoute().length);
        assertEquals(1, solution.getRoute()[0]);
    }

    /**
     * Two-city problem: only one possible tour.
     */
    @Test
    public void handlesTwoCities() {
        double[][] distances = {{0, 3}, {3, 0}};
        Problem problem = new DistanceMatrixProblem(distances);

        Solution solution = new GreedyEdgeSolver().solve(problem);

        assertEquals(2, solution.getRoute().length);
        assertEquals(6.0, solution.getTotalDistance());
    }
}
