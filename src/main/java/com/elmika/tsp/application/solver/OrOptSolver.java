package com.elmika.tsp.application.solver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elmika.tsp.domain.Problem;
import com.elmika.tsp.domain.Solution;

/**
 * Improvement decorator that applies Or-opt on top of any SolverStrategy.
 *
 * <p>Or-opt relocates a contiguous segment of 1, 2, or 3 cities to a better position
 * in the tour. For each segment it computes:
 * <pre>
 *   delta = d(A, B1) + d(Bk, C)          // edges removed around segment
 *         + d(X, Y)                        // edge removed at insertion point
 *         - d(A, C)                        // new edge closing the gap
 *         - d(X, B1) - d(Bk, Y)           // new edges around inserted segment
 * </pre>
 * If delta > 0 the move shortens the tour. Restarts after each improvement
 * (first-improvement strategy), continues until no improving move exists.
 *
 * <p>Typical usage:
 * <pre>
 *   new OrOptSolver(new GreedyEdgeSolver())
 *   new OrOptSolver(new NearestNeighborSolver())
 * </pre>
 *
 * <p>Complexity: O(n²) per pass. Finds improvements that 2-opt cannot (single-city
 * relocations are not 2-opt moves), and is generally faster than 2-opt per pass
 * because the move set is smaller.
 */
public class OrOptSolver implements SolverStrategy {

    private static final Logger log = LoggerFactory.getLogger(OrOptSolver.class);
    private static final double EPSILON = 1e-10;

    private final SolverStrategy inner;

    public OrOptSolver(SolverStrategy inner) {
        this.inner = inner;
    }

    @Override
    public Solution solve(Problem problem) {
        log.info("Applying Or-opt improvement over {}.", inner.getClass().getSimpleName());
        return orOpt(problem, inner.solve(problem));
    }

    private static Solution orOpt(Problem problem, Solution solution) {
        Integer[] route = solution.getRoute();
        int n = route.length;
        boolean improved = true;
        while (improved) {
            improved = false;
            outer:
            for (int k = 1; k <= Math.min(3, n - 1); k++) {
                for (int i = 0; i < n; i++) {
                    int a  = route[(i - 1 + n) % n];
                    int b1 = route[i];
                    int bk = route[(i + k - 1) % n];
                    int c  = route[(i + k) % n];

                    double removalGain = problem.getDistance(a, b1)
                                      + problem.getDistance(bk, c)
                                      - problem.getDistance(a, c);

                    // n-k-1 valid insertion positions: skip the segment itself
                    // and the position directly before it (which would be a no-op)
                    for (int step = 0; step < n - k - 1; step++) {
                        int j  = (i + k + step) % n;
                        int x  = route[j];
                        int y  = route[(j + 1) % n];

                        double delta = removalGain
                                     - problem.getDistance(x, b1)
                                     - problem.getDistance(bk, y)
                                     + problem.getDistance(x, y);

                        if (delta > EPSILON) {
                            route = relocate(route, i, k, j);
                            improved = true;
                            break outer;
                        }
                    }
                }
            }
        }
        return new Solution(route, SolverUtils.totalDistance(problem, route));
    }

    /**
     * Moves the segment [segStart .. segStart+k-1] (mod n) to immediately after
     * position {@code insertAfter} in the original route.
     *
     * <p>Builds the new route by traversing the non-segment positions starting
     * from the city after the segment, inserting the segment when the insertion
     * city is reached.
     */
    private static Integer[] relocate(Integer[] route, int segStart, int k, int insertAfter) {
        int n = route.length;

        Integer[] seg = new Integer[k];
        for (int x = 0; x < k; x++) seg[x] = route[(segStart + x) % n];

        Integer[] newRoute = new Integer[n];
        int write = 0;
        int pos = (segStart + k) % n;

        for (int step = 0; step < n - k; step++) {
            newRoute[write++] = route[pos];
            if (pos == insertAfter) {
                for (int x = 0; x < k; x++) newRoute[write++] = seg[x];
            }
            // Advance to the next non-segment position
            do {
                pos = (pos + 1) % n;
            } while (isInSegment(pos, segStart, k, n));
        }

        return newRoute;
    }

    private static boolean isInSegment(int pos, int segStart, int k, int n) {
        for (int x = 0; x < k; x++) {
            if (pos == (segStart + x) % n) return true;
        }
        return false;
    }
}
