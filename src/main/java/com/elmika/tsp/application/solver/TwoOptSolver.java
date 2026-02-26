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
                    double delta = improvement(problem, route, i, j);
                    if (delta > 0) {
                        reverse(route, i + 1, j);
                        improved = true;
                    }
                }
            }
        }
        return new Solution(route, totalDistance(problem, route));
    }

    /**
     * Returns the distance saved by swapping the two edges at positions i and j.
     * A positive value means the swap improves the tour.
     */
    private static double improvement(Problem problem, Integer[] route, int i, int j) {
        int n = route.length;
        int a = route[i],         b = route[(i + 1) % n];
        int c = route[j],         d = route[(j + 1) % n];
        return problem.getDistance(a, b) + problem.getDistance(c, d)
             - problem.getDistance(a, c) - problem.getDistance(b, d);
    }

    private static void reverse(Integer[] route, int from, int to) {
        while (from < to) {
            Integer tmp = route[from];
            route[from++] = route[to];
            route[to--] = tmp;
        }
    }

    private static double totalDistance(Problem problem, Integer[] route) {
        if (route.length == 0) {
            return 0;
        }
        double total = problem.getDistance(route[route.length - 1], route[0]);
        for (int i = 1; i < route.length; i++) {
            total += problem.getDistance(route[i - 1], route[i]);
        }
        return total;
    }
}
