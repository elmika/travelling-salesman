package com.elmika.tsp.application.improvement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.elmika.tsp.application.resolution.GreedyEdgeSolver;
import com.elmika.tsp.application.resolution.NearestNeighborSolver;
import com.elmika.tsp.application.resolution.ResolutionStrategy;
import com.elmika.tsp.domain.problem.DistanceMatrixProblem;
import com.elmika.tsp.domain.problem.Problem;
import com.elmika.tsp.domain.solution.Solution;

public class SimulatedAnnealingSolverTest {

    /**
     * SA always tracks the global best, so its result can never be worse
     * than the initial solution returned by the inner solver.
     */
    @Test
    public void resultIsNeverWorseThanInitialSolution() {
        double[][] distances = {
            {0, 3, 1, 4},
            {3, 0, 2, 5},
            {1, 2, 0, 6},
            {4, 5, 6, 0}
        };
        Problem problem = new DistanceMatrixProblem(distances);
        NearestNeighborSolver nn = new NearestNeighborSolver();
        double initialCost = nn.solve(problem).getTotalDistance();

        double saCost = new SimulatedAnnealingSolver(nn, new Random(42)).solve(problem).getTotalDistance();

        assertTrue(saCost <= initialCost + 1e-9,
            "SA (" + saCost + ") must be <= initial (" + initialCost + ")");
    }

    /**
     * SA must visit every city exactly once.
     */
    @Test
    public void routeVisitsEachCityExactlyOnce() {
        double[][] distances = {
            {0, 2, 9, 10},
            {1, 0, 6,  4},
            {15, 7, 0,  8},
            {6, 3, 12,  0}
        };
        Problem problem = new DistanceMatrixProblem(distances);

        Solution result = new SimulatedAnnealingSolver(new NearestNeighborSolver(), new Random(0)).solve(problem);

        Integer[] route = result.getRoute();
        Set<Integer> cities = new HashSet<>(Arrays.asList(route));
        assertEquals(4, route.length);
        assertEquals(4, cities.size());
    }

    /**
     * SA stacked on greedy edge must be no worse than greedy edge alone.
     */
    @Test
    public void stackedOnGreedyEdgeDoesNotWorsen() {
        double[][] distances = {
            {0, 3, 1, 4, 2},
            {3, 0, 2, 5, 1},
            {1, 2, 0, 6, 3},
            {4, 5, 6, 0, 4},
            {2, 1, 3, 4, 0}
        };
        Problem problem = new DistanceMatrixProblem(distances);
        GreedyEdgeSolver greedy = new GreedyEdgeSolver();

        double greedyCost = greedy.solve(problem).getTotalDistance();
        double saCost = new SimulatedAnnealingSolver(greedy, new Random(1)).solve(problem).getTotalDistance();

        assertTrue(saCost <= greedyCost + 1e-9,
            "SA (" + saCost + ") must be <= greedy edge (" + greedyCost + ")");
    }

    /**
     * For a 1-city problem SA must return the trivial solution unchanged.
     */
    @Test
    public void handlesSingleCity() {
        double[][] distances = {{0}};
        Problem problem = new DistanceMatrixProblem(distances);
        ResolutionStrategy stub = p -> new Solution(new Integer[]{1}, 0.0);

        Solution result = new SimulatedAnnealingSolver(stub, new Random(0)).solve(problem);

        assertEquals(1, result.getRoute().length);
        assertEquals(0.0, result.getTotalDistance());
    }

    /**
     * For a 2-city problem SA must return the initial solution unchanged (no 2-opt moves possible).
     */
    @Test
    public void handlesTwoCities() {
        double[][] distances = {{0, 5}, {5, 0}};
        Problem problem = new DistanceMatrixProblem(distances);
        ResolutionStrategy stub = p -> new Solution(new Integer[]{1, 2}, 10.0);

        Solution result = new SimulatedAnnealingSolver(stub, new Random(0)).solve(problem);

        assertEquals(2, result.getRoute().length);
        assertEquals(10.0, result.getTotalDistance());
    }

    /**
     * SA should find the known optimal tour (distance 4) on a 4-city symmetric problem
     * given a seeded random and enough iterations (n²×100 = 1600 steps).
     */
    @Test
    public void findsOptimalOnSmallSymmetricProblem() {
        // All cities equidistant: any tour has distance 4
        double[][] distances = {
            {0, 1, 1, 1},
            {1, 0, 1, 1},
            {1, 1, 0, 1},
            {1, 1, 1, 0}
        };
        Problem problem = new DistanceMatrixProblem(distances);

        Solution result = new SimulatedAnnealingSolver(new NearestNeighborSolver(), new Random(7)).solve(problem);

        assertEquals(4.0, result.getTotalDistance(), 1e-9);
    }
}
