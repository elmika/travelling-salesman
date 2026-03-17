package com.elmika.tsp.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.elmika.tsp.application.benchmark.BenchmarkResult;
import com.elmika.tsp.application.benchmark.BenchmarkTspUseCase;
import com.elmika.tsp.application.benchmark.TspBenchmark;
import com.elmika.tsp.domain.problem.DistanceMatrixProblem;
import com.elmika.tsp.domain.problem.Problem;

public class BenchmarkTspUseCaseTest {

    private static final Problem SIMPLE_PROBLEM = new DistanceMatrixProblem(new double[][]{
        {0, 1, 1, 1},
        {1, 0, 1, 1},
        {1, 1, 0, 1},
        {1, 1, 1, 0}
    });

    @Test
    public void returnsOneResultPerStrategy() {
        List<String> strategies = Arrays.asList("brute-force", "nearest-neighbor", "random");
        TspBenchmark benchmark = new BenchmarkTspUseCase(new SolveTspUseCase());

        List<BenchmarkResult> results = benchmark.benchmark(SIMPLE_PROBLEM, strategies);

        assertEquals(3, results.size());
    }

    @Test
    public void resultsAreSortedByDistanceAscending() {
        List<String> strategies = Arrays.asList("brute-force", "random", "nearest-neighbor");
        TspBenchmark benchmark = new BenchmarkTspUseCase(new SolveTspUseCase());

        List<BenchmarkResult> results = benchmark.benchmark(SIMPLE_PROBLEM, strategies);

        for (int i = 0; i < results.size() - 1; i++) {
            assertTrue(
                results.get(i).getSolution().getTotalDistance()
                    <= results.get(i + 1).getSolution().getTotalDistance(),
                "Results must be sorted by distance ascending"
            );
        }
    }

    @Test
    public void eachResultHasCorrectStrategyName() {
        List<String> strategies = Arrays.asList("brute-force", "nearest-neighbor");
        TspBenchmark benchmark = new BenchmarkTspUseCase(new SolveTspUseCase());

        List<BenchmarkResult> results = benchmark.benchmark(SIMPLE_PROBLEM, strategies);

        List<String> returnedStrategies = results.stream()
            .map(BenchmarkResult::getStrategy)
            .sorted()
            .collect(java.util.stream.Collectors.toList());
        assertEquals(Arrays.asList("brute-force", "nearest-neighbor"), returnedStrategies);
    }

    @Test
    public void durationIsNonNegative() {
        TspBenchmark benchmark = new BenchmarkTspUseCase(new SolveTspUseCase());

        List<BenchmarkResult> results = benchmark.benchmark(SIMPLE_PROBLEM, List.of("brute-force"));

        assertTrue(results.get(0).getDurationMs() >= 0);
    }
}
