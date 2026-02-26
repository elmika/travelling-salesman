package com.elmika.tsp.application.solver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.elmika.tsp.domain.DistanceMatrixProblem;
import com.elmika.tsp.domain.Problem;
import com.elmika.tsp.domain.Solution;

public class TwoOptSolverTest {

    /**
     * 4-city problem with two crossing edges.
     *
     * Layout (distances):
     *   d(1,2)=1, d(2,3)=1, d(3,4)=1, d(4,1)=1  (the good cycle)
     *   d(1,3)=5, d(2,4)=5                        (the crossing edges)
     *
     * Inner solver returns the crossing route [1,3,2,4] (distance=12).
     * 2-opt should uncross it to [1,2,3,4] (distance=4).
     */
    @Test
    public void twoOptUncrossesCrossingRoute() {
        double[][] distances = {
            {0, 1, 5, 1},
            {1, 0, 1, 5},
            {5, 1, 0, 1},
            {1, 5, 1, 0}
        };
        Problem problem = new DistanceMatrixProblem(distances);
        SolverStrategy crossingRoute = p -> new Solution(new Integer[]{1, 3, 2, 4}, 12.0);

        Solution result = new TwoOptSolver(crossingRoute).solve(problem);

        assertEquals(4.0, result.getTotalDistance());
    }

    /**
     * When the inner solver already returns an optimal route, 2-opt should leave it unchanged.
     */
    @Test
    public void twoOptDoesNotWorsenOptimalRoute() {
        double[][] distances = {
            {0, 1, 1, 1},
            {1, 0, 1, 1},
            {1, 1, 0, 1},
            {1, 1, 1, 0}
        };
        Problem problem = new DistanceMatrixProblem(distances);
        SolverStrategy alreadyOptimal = p -> new Solution(new Integer[]{1, 2, 3, 4}, 4.0);

        Solution result = new TwoOptSolver(alreadyOptimal).solve(problem);

        assertEquals(4.0, result.getTotalDistance());
    }

    /**
     * 2-opt stacked on nearest-neighbor produces a result no worse than nearest-neighbor alone.
     */
    @Test
    public void twoOptStackedOnNearestNeighborDoesNotWorsen() {
        double[][] distances = {
            {0, 2, 9, 10},
            {1, 0, 6,  4},
            {15, 7, 0,  8},
            {6, 3, 12,  0}
        };
        Problem problem = new DistanceMatrixProblem(distances);
        NearestNeighborSolver nn = new NearestNeighborSolver();
        TwoOptSolver twoOpt = new TwoOptSolver(nn);

        double nnDistance = nn.solve(problem).getTotalDistance();
        double twoOptDistance = twoOpt.solve(problem).getTotalDistance();

        assertTrue(twoOptDistance <= nnDistance,
            "2-opt result (" + twoOptDistance + ") should be <= nearest-neighbor (" + nnDistance + ")");
    }

    /**
     * Single-city problem: 2-opt should return a valid 1-city solution without errors.
     */
    @Test
    public void twoOptHandlesSingleCity() {
        double[][] distances = {{0}};
        Problem problem = new DistanceMatrixProblem(distances);
        SolverStrategy stub = p -> new Solution(new Integer[]{1}, 0.0);

        Solution result = new TwoOptSolver(stub).solve(problem);

        assertEquals(1, result.getRoute().length);
        assertEquals(0.0, result.getTotalDistance());
    }
}
