package com.elmika.tsp.application;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.elmika.tsp.domain.Problem;
import com.elmika.tsp.domain.Solution;

/**
 * Application use case implementing TspBenchmark.
 * Delegates each strategy to the TspSolver port, measures wall-clock time,
 * and returns results sorted by total distance ascending (best first).
 */
public class BenchmarkTspUseCase implements TspBenchmark {

    private final TspSolver solver;

    public BenchmarkTspUseCase(TspSolver solver) {
        this.solver = solver;
    }

    @Override
    public List<BenchmarkResult> benchmark(Problem problem, List<String> strategies) {
        return strategies.stream()
                .map(strategy -> runOne(problem, strategy))
                .sorted(Comparator.comparingDouble(r -> r.getSolution().getTotalDistance()))
                .collect(Collectors.toList());
    }

    private BenchmarkResult runOne(Problem problem, String strategy) {
        long startNs = System.nanoTime();
        Solution solution = solver.solve(problem, strategy);
        long durationMs = (System.nanoTime() - startNs) / 1_000_000L;
        return new BenchmarkResult(strategy, solution, durationMs);
    }
}
