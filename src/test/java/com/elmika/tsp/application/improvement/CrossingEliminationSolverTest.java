package com.elmika.tsp.application.improvement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.elmika.tsp.application.resolution.NearestNeighborSolver;
import com.elmika.tsp.application.resolution.ResolutionStrategy;
import com.elmika.tsp.domain.problem.DistanceMatrixProblem;
import com.elmika.tsp.domain.problem.EuclideanProblem;
import com.elmika.tsp.domain.problem.Point;
import com.elmika.tsp.domain.solution.Solution;

public class CrossingEliminationSolverTest {

    /**
     * 4 cities at the corners of a unit square:
     *   City 1=(0,0), City 2=(1,0), City 3=(1,1), City 4=(0,1)
     *
     * The inner solver returns the crossing route [1,3,2,4]:
     *   edge (1→3): diagonal from (0,0) to (1,1)
     *   edge (2→4): diagonal from (1,0) to (0,1)
     *   These two diagonals cross at (0.5, 0.5).
     *
     * After uncrossing, the tour should follow the perimeter with distance 4.0.
     */
    @Test
    public void eliminatesCrossingEdgesInSquare() {
        double[][] coords = {{0, 0}, {1, 0}, {1, 1}, {0, 1}};
        EuclideanProblem problem = new EuclideanProblem(coords);
        ResolutionStrategy crossingRoute = p -> new Solution(new Integer[]{1, 3, 2, 4}, 999.0);

        Solution result = new CrossingEliminationSolver(crossingRoute).solve(problem);

        assertEquals(4.0, result.getTotalDistance(), 1e-9);
    }

    /**
     * A tour with no crossings should be left unchanged.
     */
    @Test
    public void doesNotWorsenAlreadyUncrossedTour() {
        double[][] coords = {{0, 0}, {1, 0}, {1, 1}, {0, 1}};
        EuclideanProblem problem = new EuclideanProblem(coords);
        ResolutionStrategy perimeter = p -> new Solution(new Integer[]{1, 2, 3, 4}, 4.0);

        Solution result = new CrossingEliminationSolver(perimeter).solve(problem);

        assertEquals(4.0, result.getTotalDistance(), 1e-9);
    }

    /**
     * Stacked on nearest-neighbor, the result must be no worse.
     */
    @Test
    public void stackedOnNearestNeighborDoesNotWorsen() {
        double[][] coords = {{0, 0}, {3, 0}, {3, 2}, {0, 2}, {1, 1}};
        EuclideanProblem problem = new EuclideanProblem(coords);
        NearestNeighborSolver nn = new NearestNeighborSolver();

        double nnDistance = nn.solve(problem).getTotalDistance();
        double uncrossingDistance = new CrossingEliminationSolver(nn).solve(problem).getTotalDistance();

        assertTrue(uncrossingDistance <= nnDistance + 1e-9,
            "uncrossing (" + uncrossingDistance + ") should be <= nearest-neighbor (" + nnDistance + ")");
    }

    /**
     * Passing a non-Euclidean problem must throw with a clear message.
     */
    @Test
    public void throwsForNonEuclideanProblem() {
        double[][] distances = {{0, 1}, {1, 0}};
        DistanceMatrixProblem matrix = new DistanceMatrixProblem(distances);
        ResolutionStrategy stub = p -> new Solution(new Integer[]{1, 2}, 1.0);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> new CrossingEliminationSolver(stub).solve(matrix));

        assertTrue(ex.getMessage().contains("EuclideanProblem"));
    }

    // --- cross() geometry unit tests ---

    @Test
    public void crossDetectsCrossingDiagonals() {
        // (0,0)→(1,1) crosses (1,0)→(0,1)
        Point a = new Point(0, 0), b = new Point(1, 1);
        Point c = new Point(1, 0), d = new Point(0, 1);
        assertTrue(CrossingEliminationSolver.cross(a, b, c, d));
    }

    @Test
    public void crossReturnsFalseForParallelSegments() {
        Point a = new Point(0, 0), b = new Point(1, 0);
        Point c = new Point(0, 1), d = new Point(1, 1);
        assertFalse(CrossingEliminationSolver.cross(a, b, c, d));
    }

    @Test
    public void crossReturnsFalseForCollinearNonOverlappingSegments() {
        Point a = new Point(0, 0), b = new Point(1, 0);
        Point c = new Point(2, 0), d = new Point(3, 0);
        assertFalse(CrossingEliminationSolver.cross(a, b, c, d));
    }

    // Note: T-junctions (one endpoint on the other segment) are not tested because
    // they cannot arise between non-adjacent TSP edges: edges that share a vertex
    // are always adjacent and are already skipped in the elimination loop.
}
