package com.elmika.tsp.adapter.api;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.elmika.tsp.application.improvement.CrossingEliminationSolver;
import com.elmika.tsp.application.improvement.ImprovementStrategy;
import com.elmika.tsp.application.improvement.OrOptSolver;
import com.elmika.tsp.application.improvement.SimulatedAnnealingSolver;
import com.elmika.tsp.application.improvement.TwoOptSolver;
import com.elmika.tsp.domain.problem.Problem;
import com.elmika.tsp.domain.solution.Solution;

/**
 * API-layer service that maps improvement strategy names to ImprovementStrategy instances.
 * Each entry carries optional size limits for synchronous (and future asynchronous) requests.
 */
public class ImprovementService {

    private final Map<String, StrategyEntry> strategies = new LinkedHashMap<>();

    public ImprovementService() {
        strategies.put("2opt",      StrategyEntry.unlimited(new TwoOptSolver()));
        strategies.put("oropt",     StrategyEntry.unlimited(new OrOptSolver()));
        strategies.put("sa",        StrategyEntry.unlimited(new SimulatedAnnealingSolver()));
        strategies.put("uncrossing", StrategyEntry.unlimited(new CrossingEliminationSolver()));
    }

    public Solution improve(Problem problem, Solution initial, String strategyName) {
        StrategyEntry entry = strategies.get(strategyName);
        if (entry == null) {
            throw new IllegalArgumentException(
                "Unknown improvement strategy: '" + strategyName + "'. "
                + "Allowed: " + String.join(", ", strategies.keySet()));
        }
        entry.validateSyncSize(problem.getSize(), strategyName);
        return entry.strategy.improve(problem, initial);
    }

    public Set<String> getStrategyNames() {
        return strategies.keySet();
    }

    // ── Inner type ────────────────────────────────────────────────────────────

    static final class StrategyEntry {
        final ImprovementStrategy strategy;
        /** Maximum problem size for synchronous requests. {@code null} means no limit. */
        final Integer syncSizeLimit;
        /** Maximum problem size for asynchronous requests (reserved for future use). {@code null} means no limit. */
        final Integer asyncSizeLimit;

        private StrategyEntry(ImprovementStrategy strategy, Integer syncSizeLimit, Integer asyncSizeLimit) {
            this.strategy = strategy;
            this.syncSizeLimit = syncSizeLimit;
            this.asyncSizeLimit = asyncSizeLimit;
        }

        static StrategyEntry unlimited(ImprovementStrategy strategy) {
            return new StrategyEntry(strategy, null, null);
        }

        static StrategyEntry limited(ImprovementStrategy strategy, Integer syncMax, Integer asyncMax) {
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
