package com.elmika.tsp.application.solver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elmika.tsp.domain.Problem;
import com.elmika.tsp.domain.Solution;

/**
 * Improvement decorator that applies the 2-opt heuristic on top of any SolverStrategy.
 *
 * <p>2-opt repeatedly scans all pairs of non-adjacent edges. When reversing the segment
 * between them shortens the tour, it performs the swap and restarts. Continues until
 * no improving swap exists (local optimum).
 *
 * <p>Typical usage:
 * <pre>
 *   new TwoOptSolver(new NearestNeighborSolver())
 *   new TwoOptSolver(new RandomSolver(100))
 * </pre>
 *
 * <p>Complexity: O(n²) per pass, O(n³) worst case overall.
 */
public class TwoOptSolver implements SolverStrategy {

    private static final Logger log = LoggerFactory.getLogger(TwoOptSolver.class);

    private final SolverStrategy inner;

    public TwoOptSolver(SolverStrategy inner) {
        this.inner = inner;
    }

    @Override
    public Solution solve(Problem problem) {
        log.info("Applying 2-opt improvement over {}.", inner.getClass().getSimpleName());
        Solution initial = inner.solve(problem);
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
