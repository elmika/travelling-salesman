package com.elmika.tsp.application.resolution;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.elmika.tsp.domain.problem.DistanceMatrixProblem;
import com.elmika.tsp.domain.problem.Problem;
import com.elmika.tsp.domain.solution.Solution;

public class NearestNeighborSolverTest {

    /**
     * 4-city problem where all distances are equal.
     * Nearest neighbor must still produce a valid 4-city route with correct total distance.
     */
    @Test
    public void solveReturnsValidRouteWhenAllDistancesEqual() {
        double[][] distances = {
            {1, 1, 1, 1},
            {1, 1, 1, 1},
            {1, 1, 1, 1},
            {1, 1, 1, 1}
        };
        Problem problem = new DistanceMatrixProblem(distances);
        Solution solution = new NearestNeighborSolver().solve(problem);

        assertEquals(4, solution.getRoute().length);
        assertEquals(4.0, solution.getTotalDistance());
    }

    /**
     * 4-city problem with a clear nearest-neighbor path.
     *
     * Distance matrix (1-indexed cities):
     *   1→2: 1,  1→3: 10, 1→4: 10
     *   2→3: 1,  2→4: 10
     *   3→4: 1
     *
     * Greedy from city 1: 1→2 (d=1) → 3 (d=1) → 4 (d=1) → back to 1 (d=10) = 13
     * Optimal would be 1→2→3→4→1 = 13 (same in this case).
     */
    @Test
    public void solveFollowsNearestNeighborGreedily() {
        double[][] distances = {
            { 0,  1, 10, 10},
            { 1,  0,  1, 10},
            {10,  1,  0,  1},
            {10, 10,  1,  0}
        };
        Problem problem = new DistanceMatrixProblem(distances);
        Solution solution = new NearestNeighborSolver().solve(problem);

        assertArrayEquals(new Integer[]{1, 2, 3, 4}, solution.getRoute());
        assertEquals(13.0, solution.getTotalDistance());
    }

    /**
     * Single-city problem: route contains only that city, distance is 0.
     */
    @Test
    public void solveHandlesSingleCity() {
        double[][] distances = {{0}};
        Problem problem = new DistanceMatrixProblem(distances);
        Solution solution = new NearestNeighborSolver().solve(problem);

        assertEquals(1, solution.getRoute().length);
        assertEquals(1, solution.getRoute()[0]);
    }
}
