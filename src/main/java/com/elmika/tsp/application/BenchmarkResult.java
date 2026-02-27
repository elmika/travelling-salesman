package com.elmika.tsp.application;

import com.elmika.tsp.domain.Solution;

/**
 * Immutable record of a single strategy's performance in a benchmark run.
 */
public final class BenchmarkResult {

    private final String strategy;
    private final Solution solution;
    private final long durationMs;

    public BenchmarkResult(String strategy, Solution solution, long durationMs) {
        this.strategy = strategy;
        this.solution = solution;
        this.durationMs = durationMs;
    }

    public String getStrategy() {
        return strategy;
    }

    public Solution getSolution() {
        return solution;
    }

    public long getDurationMs() {
        return durationMs;
    }
}
