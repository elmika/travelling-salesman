package com.elmika.tsp.application.improvement;

import com.elmika.tsp.application.solver.SolverUtils;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elmika.tsp.application.resolution.ResolutionStrategy;
import com.elmika.tsp.domain.problem.Problem;
import com.elmika.tsp.domain.solution.Solution;

/**
 * Improvement decorator that applies Simulated Annealing.
 *
 * <p>At each step a random 2-opt swap is proposed. The swap is always accepted when it
 * improves the tour. When it worsens the tour, it is accepted with probability
 * {@code exp(improvement / T)}, where T is the current temperature and improvement is
 * negative. This allows the search to escape local optima early on, then converge as T
 * falls.
 *
 * <p>Cooling schedule: geometric from {@code initialCost} down to {@code 1e-4} over
 * {@code n² × 100} iterations, so the schedule automatically adapts to problem size
 * and initial tour quality.
 *
 * <p>The best solution seen across all iterations is returned, so the result is always
 * at least as good as the initial solution.
 *
 * <p>Implements both {@link ResolutionStrategy} (wrapping an inner strategy for CLI composite
 * use) and {@link ImprovementStrategy} (standalone improvement for API use).
 */
public class SimulatedAnnealingSolver implements ResolutionStrategy, ImprovementStrategy {

    private static final Logger log = LoggerFactory.getLogger(SimulatedAnnealingSolver.class);
    private static final double FINAL_TEMP = 1e-4;

    private final ResolutionStrategy inner;
    private final Random random;

    /** For CLI composite use: wraps an inner resolution strategy. */
    public SimulatedAnnealingSolver(ResolutionStrategy inner) {
        this(inner, new Random());
    }

    /** For API use: standalone improvement (no inner strategy). */
    public SimulatedAnnealingSolver() {
        this(null, new Random());
    }

    /** Package-private constructor for deterministic testing. */
    SimulatedAnnealingSolver(ResolutionStrategy inner, Random random) {
        this.inner = inner;
        this.random = random;
    }

    @Override
    public Solution solve(Problem problem) {
        if (inner == null) {
            throw new IllegalStateException("SimulatedAnnealingSolver used as ResolutionStrategy requires an inner strategy.");
        }
        log.info("Applying Simulated Annealing over {}.", inner.getClass().getSimpleName());
        Solution initial = inner.solve(problem);
        int n = problem.getSize();
        if (n < 3) {
            return initial;
        }
        return anneal(problem, initial);
    }

    @Override
    public Solution improve(Problem problem, Solution initial) {
        log.info("Applying Simulated Annealing improvement.");
        int n = problem.getSize();
        if (n < 3) {
            return initial;
        }
        return anneal(problem, initial);
    }

    private Solution anneal(Problem problem, Solution initial) {
        int n = problem.getSize();
        Integer[] current = initial.getRoute();
        double currentCost = initial.getTotalDistance();

        Integer[] best = current.clone();
        double bestCost = currentCost;

        int iterations = n * n * 100;
        double temp = currentCost > 0 ? currentCost : 1.0;
        double cooling = Math.pow(FINAL_TEMP / temp, 1.0 / (iterations - 1));

        for (int iter = 0; iter < iterations; iter++) {
            int i = random.nextInt(n - 1);
            int j = i + 1 + random.nextInt(n - 1 - i);

            // Skip full-reversal (same tour in symmetric TSP, wastes a step)
            if (i == 0 && j == n - 1) {
                temp *= cooling;
                continue;
            }

            double improvement = SolverUtils.twoOptImprovement(problem, current, i, j);

            if (improvement > 0 || random.nextDouble() < Math.exp(improvement / temp)) {
                SolverUtils.reverse(current, i + 1, j);
                currentCost -= improvement;
                if (currentCost < bestCost) {
                    bestCost = currentCost;
                    best = current.clone();
                }
            }

            temp *= cooling;
        }

        return new Solution(best, SolverUtils.totalDistance(problem, best));
    }
}
