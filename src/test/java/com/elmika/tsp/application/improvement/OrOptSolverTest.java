package com.elmika.tsp.application.improvement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.elmika.tsp.application.resolution.GreedyEdgeSolver;
import com.elmika.tsp.application.resolution.NearestNeighborSolver;
import com.elmika.tsp.application.resolution.ResolutionStrategy;
import com.elmika.tsp.domain.problem.DistanceMatrixProblem;
import com.elmika.tsp.domain.problem.Problem;
import com.elmika.tsp.domain.solution.Solution;

public class OrOptSolverTest {

    /**
     * 4-city problem where city 3 is an expensive detour between cities 2 and 4.
     * Moving city 3 to after city 4 saves distance.
     *
     * Route from stub:  [1, 2, 3, 4]  distance = 1+9+1+3 = 14
     *   d(2,3)=9, d(3,4)=1: removal gain = d(2,3)+d(3,4)-d(2,4) = 9+1-1 = 9
     *   insert after 4: d(4,3)+d(3,1)-d(4,1) = 1+9-3 = 7
     *   delta = 9-7 = 2 > 0  →  improvement
     *
     * After relocation:  [4, 3, 1, 2]  (same cycle as [1, 2, 4, 3])
     *   distance = d(1,2)+d(2,4)+d(4,3)+d(3,1) = 1+1+1+9 = 12
     */
    @Test
    public void relocatesSingleCityDetour() {
        double[][] distances = {
            {0, 1, 9, 3},
            {1, 0, 9, 1},
            {9, 9, 0, 1},
            {3, 1, 1, 0}
        };
        Problem problem = new DistanceMatrixProblem(distances);
        ResolutionStrategy stub = p -> new Solution(new Integer[]{1, 2, 3, 4}, 14.0);

        Solution result = new OrOptSolver(stub).solve(problem);

        assertEquals(12.0, result.getTotalDistance(), 1e-9);
    }

    /**
     * When the initial tour is already locally optimal for Or-opt, it must be left unchanged.
     */
    @Test
    public void doesNotWorsenAlreadyOptimalTour() {
        double[][] distances = {
            {0, 1, 1, 1},
            {1, 0, 1, 1},
            {1, 1, 0, 1},
            {1, 1, 1, 0}
        };
        Problem problem = new DistanceMatrixProblem(distances);
        ResolutionStrategy stub = p -> new Solution(new Integer[]{1, 2, 3, 4}, 4.0);

        Solution result = new OrOptSolver(stub).solve(problem);

        assertEquals(4.0, result.getTotalDistance(), 1e-9);
    }

    /**
     * The output route must visit every city exactly once.
     */
    @Test
    public void routeVisitsEachCityExactlyOnce() {
        double[][] distances = {
            {0, 3, 1, 4},
            {3, 0, 2, 5},
            {1, 2, 0, 6},
            {4, 5, 6, 0}
        };
        Problem problem = new DistanceMatrixProblem(distances);
        Solution result = new OrOptSolver(new NearestNeighborSolver()).solve(problem);

        Integer[] route = result.getRoute();
        Set<Integer> cities = new HashSet<>(Arrays.asList(route));
        assertEquals(4, route.length);
        assertEquals(4, cities.size());
        assertTrue(cities.contains(1));
        assertTrue(cities.contains(2));
        assertTrue(cities.contains(3));
        assertTrue(cities.contains(4));
    }

    /**
     * Or-opt stacked on nearest-neighbor must not worsen the result.
     */
    @Test
    public void stackedOnNearestNeighborDoesNotWorsen() {
        double[][] distances = {
            {0, 2, 9, 10},
            {1, 0, 6,  4},
            {15, 7, 0,  8},
            {6, 3, 12,  0}
        };
        Problem problem = new DistanceMatrixProblem(distances);
        NearestNeighborSolver nn = new NearestNeighborSolver();

        double nnDistance = nn.solve(problem).getTotalDistance();
        double orOptDistance = new OrOptSolver(nn).solve(problem).getTotalDistance();

        assertTrue(orOptDistance <= nnDistance + 1e-9,
            "Or-opt (" + orOptDistance + ") should be <= nearest-neighbor (" + nnDistance + ")");
    }

    /**
     * Or-opt stacked on greedy edge must not worsen the result.
     */
    @Test
    public void stackedOnGreedyEdgeDoesNotWorsen() {
        double[][] distances = {
            {0, 3, 1, 4},
            {3, 0, 2, 5},
            {1, 2, 0, 6},
            {4, 5, 6, 0}
        };
        Problem problem = new DistanceMatrixProblem(distances);
        GreedyEdgeSolver greedy = new GreedyEdgeSolver();

        double greedyDistance = greedy.solve(problem).getTotalDistance();
        double orOptDistance = new OrOptSolver(greedy).solve(problem).getTotalDistance();

        assertTrue(orOptDistance <= greedyDistance + 1e-9,
            "Or-opt (" + orOptDistance + ") should be <= greedy edge (" + greedyDistance + ")");
    }

    /**
     * Single-city problem: trivial tour, or-opt must return it unchanged.
     */
    @Test
    public void handlesSingleCity() {
        double[][] distances = {{0}};
        Problem problem = new DistanceMatrixProblem(distances);
        ResolutionStrategy stub = p -> new Solution(new Integer[]{1}, 0.0);

        Solution result = new OrOptSolver(stub).solve(problem);

        assertEquals(1, result.getRoute().length);
        assertEquals(0.0, result.getTotalDistance());
    }
}
