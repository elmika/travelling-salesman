package com.elmika.tsp.adapter.api;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.elmika.tsp.application.resolution.BruteForceSolver;
import com.elmika.tsp.application.resolution.GreedyEdgeSolver;
import com.elmika.tsp.application.resolution.NearestNeighborSolver;
import com.elmika.tsp.application.resolution.RandomSolver;
import com.elmika.tsp.application.resolution.ResolutionStrategy;
import com.elmika.tsp.domain.problem.Problem;
import com.elmika.tsp.domain.solution.Solution;

/**
 * API-layer service that maps resolution strategy names to ResolutionStrategy instances.
 * Each entry carries optional size limits for synchronous (and future asynchronous) requests.
 */
public class ResolutionService {

    private final Map<String, StrategyEntry> strategies = new LinkedHashMap<>();

    public ResolutionService() {
        strategies.put("brute-force",       StrategyEntry.limited(new BruteForceSolver(), 12, null));
        strategies.put("random",            StrategyEntry.unlimited(new RandomSolver(1)));
        strategies.put("random10",          StrategyEntry.unlimited(new RandomSolver(10)));
        strategies.put("random100",         StrategyEntry.unlimited(new RandomSolver(100)));
        strategies.put("nearest-neighbor",  StrategyEntry.unlimited(new NearestNeighborSolver()));
        strategies.put("greedy-edge",       StrategyEntry.unlimited(new GreedyEdgeSolver()));
    }

    public Solution solve(Problem problem, String strategyName) {
        StrategyEntry entry = strategies.get(strategyName);
        if (entry == null) {
            throw new IllegalArgumentException(
                "Unknown resolution strategy: '" + strategyName + "'. "
                + "Allowed: " + String.join(", ", strategies.keySet()));
        }
        entry.validateSyncSize(problem.getSize(), strategyName);
        return entry.strategy.solve(problem);
    }

    public Set<String> getStrategyNames() {
        return strategies.keySet();
    }

    // ── Inner type ────────────────────────────────────────────────────────────

    static final class StrategyEntry {
        final ResolutionStrategy strategy;
        /** Maximum problem size for synchronous requests. {@code null} means no limit. */
        final Integer syncSizeLimit;
        /** Maximum problem size for asynchronous requests (reserved for future use). {@code null} means no limit. */
        final Integer asyncSizeLimit;

        private StrategyEntry(ResolutionStrategy strategy, Integer syncSizeLimit, Integer asyncSizeLimit) {
            this.strategy = strategy;
            this.syncSizeLimit = syncSizeLimit;
            this.asyncSizeLimit = asyncSizeLimit;
        }

        static StrategyEntry unlimited(ResolutionStrategy strategy) {
            return new StrategyEntry(strategy, null, null);
        }

        static StrategyEntry limited(ResolutionStrategy strategy, Integer syncMax, Integer asyncMax) {
            return new StrategyEntry(strategy, syncMax, asyncMax);
        }

        void validateSyncSize(int problemSize, String strategyName) {
            if (syncSizeLimit != null && problemSize > syncSizeLimit) {
                throw new IllegalArgumentException(
                    "Strategy '" + strategyName + "' supports at most " + syncSizeLimit
                    + " cities for synchronous requests, but the problem has " + problemSize + " cities.");
            }
        }
    }
}
