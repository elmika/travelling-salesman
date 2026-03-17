package com.elmika.tsp.application.benchmark;

import java.util.List;

import com.elmika.tsp.domain.problem.Problem;

/**
 * Inbound port: runs a set of solver strategies against a problem and returns
 * the results sorted by solution quality.
 */
public interface TspBenchmark {
    List<BenchmarkResult> benchmark(Problem problem, List<String> strategies);
}
