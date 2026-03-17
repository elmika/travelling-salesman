package com.elmika.tsp.application.improvement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elmika.tsp.application.resolution.ResolutionStrategy;
import com.elmika.tsp.domain.problem.Problem;
import com.elmika.tsp.domain.solution.Solution;

/**
 * Improvement decorator that applies the 2-opt heuristic.
 *
 * <p>2-opt repeatedly scans all pairs of non-adjacent edges. When reversing the segment
 * between them shortens the tour, it performs the swap and restarts. Continues until
 * no improving swap exists (local optimum).
 *
 * <p>Implements both {@link ResolutionStrategy} (wrapping an inner strategy for CLI composite
 * use) and {@link ImprovementStrategy} (standalone improvement for API use).
 *
 * <p>Complexity: O(n²) per pass, O(n³) worst case overall.
 */
public class TwoOptSolver implements ResolutionStrategy, ImprovementStrategy {

    private static final Logger log = LoggerFactory.getLogger(TwoOptSolver.class);

    private final ResolutionStrategy inner;

    /** For CLI composite use: wraps an inner resolution strategy. */
    public TwoOptSolver(ResolutionStrategy inner) {
        this.inner = inner;
    }

    /** For API use: standalone improvement (no inner strategy). */
    public TwoOptSolver() {
        this.inner = null;
    }

    @Override
    public Solution solve(Problem problem) {
        if (inner == null) {
            throw new IllegalStateException("TwoOptSolver used as ResolutionStrategy requires an inner strategy.");
        }
        log.info("Applying 2-opt improvement over {}.", inner.getClass().getSimpleName());
        return improve(problem, inner.solve(problem));
    }

    @Override
    public Solution improve(Problem problem, Solution initial) {
        log.info("Applying 2-opt improvement.");
        return twoOpt(problem, initial);
    }

    private static Solution twoOpt(Problem problem, Solution solution) {
        Integer[] route = solution.getRoute();
        int n = route.length;
        boolean improved = true;
        while (improved) {
            improved = false;
            for (int i = 0; i < n - 1; i++) {
                for (int j = i + 2; j < n; j++) {
                    if (i == 0 && j == n - 1) {
                        continue; // edges share route[0], not a valid 2-opt pair
                    }
                    double delta = SolverUtils.twoOptImprovement(problem, route, i, j);
                    if (delta > 0) {
                        SolverUtils.reverse(route, i + 1, j);
                        improved = true;
                    }
                }
            }
        }
        return new Solution(route, SolverUtils.totalDistance(problem, route));
    }
}
