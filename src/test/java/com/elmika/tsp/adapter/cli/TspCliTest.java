package com.elmika.tsp.adapter.cli;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import com.elmika.tsp.application.ConfigLoader;
import com.elmika.tsp.application.ProblemConfiguration;
import com.elmika.tsp.application.ProblemProvider;
import com.elmika.tsp.application.SolveTspUseCase;
import com.elmika.tsp.application.TspSolver;
import com.elmika.tsp.application.benchmark.BenchmarkTspUseCase;
import com.elmika.tsp.domain.problem.DistanceMatrixProblem;
import com.elmika.tsp.domain.problem.EuclideanProblem;
import com.elmika.tsp.domain.solution.Solution;

public class TspCliTest {

    @Test
    public void runCompletesWithFakeAdapters() {
        ConfigLoader configLoader = () -> new ProblemConfiguration("trivial", "brute-force");
        ProblemProvider problemProvider = type -> {
            double[][] distances = {{0, 1, 1}, {1, 0, 1}, {1, 1, 0}};
            return new DistanceMatrixProblem(distances);
        };
        TspCli cli = new TspCli(configLoader, problemProvider, new SolveTspUseCase());

        assertDoesNotThrow(cli::run);
    }

    @Test
    public void runBenchmarkModeCompletesWithMultipleStrategies() {
        ConfigLoader configLoader = () -> new ProblemConfiguration("trivial", "brute-force, nearest-neighbor");
        ProblemProvider problemProvider = type -> {
            double[][] distances = {{0, 1, 1, 1}, {1, 0, 1, 1}, {1, 1, 0, 1}, {1, 1, 1, 0}};
            return new DistanceMatrixProblem(distances);
        };
        TspSolver solver = new SolveTspUseCase();
        TspCli cli = new TspCli(configLoader, problemProvider, solver, new BenchmarkTspUseCase(solver));

        assertDoesNotThrow(cli::run);
    }

    @Test
    public void runExportsRouteJsonForEuclideanProblem() throws Exception {
        Path outputDir = Path.of("output");
        Path routeFile = outputDir.resolve("route.json");
        Files.deleteIfExists(routeFile);

        ConfigLoader configLoader = () -> new ProblemConfiguration("euclidean", "brute-force");
        ProblemProvider problemProvider = type -> {
            double[][] points = {
                {0.0, 0.0},
                {3.0, 4.0}
            };
            return new EuclideanProblem(points);
        };
        TspSolver solver = (problem, strategy) ->
            new Solution(new Integer[] {1, 2}, 0.0);

        TspCli cli = new TspCli(configLoader, problemProvider, solver);

        cli.run();

        assertTrue(Files.exists(routeFile));
    }
}
