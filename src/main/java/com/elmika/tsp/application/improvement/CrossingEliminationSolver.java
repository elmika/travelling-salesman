package com.elmika.tsp.application.improvement;

import com.elmika.tsp.application.solver.SolverUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elmika.tsp.application.resolution.ResolutionStrategy;
import com.elmika.tsp.domain.problem.EuclideanProblem;
import com.elmika.tsp.domain.problem.Point;
import com.elmika.tsp.domain.problem.Problem;
import com.elmika.tsp.domain.solution.Solution;

/**
 * Improvement decorator that eliminates all crossing edges from a Euclidean tour.
 *
 * <p>In a Euclidean tour, two crossing edges can always be uncrossed to produce a shorter
 * tour (triangle inequality). No distance arithmetic is needed: a geometric intersection
 * test suffices, making each candidate check cheaper than a standard 2-opt delta.
 *
 * <p>Requires an {@link EuclideanProblem}; throws {@link IllegalArgumentException} otherwise.
 *
 * <p>Implements both {@link ResolutionStrategy} (wrapping an inner strategy for CLI composite
 * use) and {@link ImprovementStrategy} (standalone improvement for API use).
 *
 * <p>Complexity: O(n²) per pass.
 */
public class CrossingEliminationSolver implements ResolutionStrategy, ImprovementStrategy {

    private static final Logger log = LoggerFactory.getLogger(CrossingEliminationSolver.class);

    private final ResolutionStrategy inner;

    /** For CLI composite use: wraps an inner resolution strategy. */
    public CrossingEliminationSolver(ResolutionStrategy inner) {
        this.inner = inner;
    }

    /** For API use: standalone improvement (no inner strategy). */
    public CrossingEliminationSolver() {
        this.inner = null;
    }

    @Override
    public Solution solve(Problem problem) {
        if (inner == null) {
            throw new IllegalStateException("CrossingEliminationSolver used as ResolutionStrategy requires an inner strategy.");
        }
        if (!(problem instanceof EuclideanProblem)) {
            throw new IllegalArgumentException(
                "CrossingEliminationSolver requires a EuclideanProblem, got: "
                + problem.getClass().getSimpleName());
        }
        EuclideanProblem euclidean = (EuclideanProblem) problem;
        log.info("Applying crossing elimination over {}.", inner.getClass().getSimpleName());
        return improve(euclidean, inner.solve(euclidean));
    }

    @Override
    public Solution improve(Problem problem, Solution initial) {
        if (!(problem instanceof EuclideanProblem)) {
            throw new IllegalArgumentException(
                "CrossingEliminationSolver requires a EuclideanProblem, got: "
                + problem.getClass().getSimpleName());
        }
        log.info("Applying crossing elimination improvement.");
        return eliminateCrossings((EuclideanProblem) problem, initial);
    }

    private static Solution eliminateCrossings(EuclideanProblem problem, Solution solution) {
        Integer[] route = solution.getRoute();
        int n = route.length;
        boolean improved = true;
        while (improved) {
            improved = false;
            for (int i = 0; i < n - 1; i++) {
                for (int j = i + 2; j < n; j++) {
                    if (i == 0 && j == n - 1) {
                        continue; // edges share route[0], not a valid pair
                    }
                    Point a = problem.getPoint(route[i]);
                    Point b = problem.getPoint(route[(i + 1) % n]);
                    Point c = problem.getPoint(route[j]);
                    Point d = problem.getPoint(route[(j + 1) % n]);
                    if (cross(a, b, c, d)) {
                        SolverUtils.reverse(route, i + 1, j);
                        improved = true;
                    }
                }
            }
        }
        return new Solution(route, SolverUtils.totalDistance(problem, route));
    }

    /**
     * Returns true if segment AB and segment CD properly intersect.
     * Uses the cross-product sign test: the segments cross iff each endpoint
     * of one segment lies on opposite sides of the line through the other.
     */
    static boolean cross(Point a, Point b, Point c, Point d) {
        double d1 = crossProduct(a, b, c);
        double d2 = crossProduct(a, b, d);
        double d3 = crossProduct(c, d, a);
        double d4 = crossProduct(c, d, b);
        return sign(d1) != sign(d2) && sign(d3) != sign(d4);
    }

    /** Signed area of the triangle OPQ, i.e. (P-O) × (Q-O). */
    private static double crossProduct(Point o, Point p, Point q) {
        return (p.getX() - o.getX()) * (q.getY() - o.getY())
             - (p.getY() - o.getY()) * (q.getX() - o.getX());
    }

    private static int sign(double d) {
        if (d > 0) return  1;
        if (d < 0) return -1;
        return 0;
    }
}
