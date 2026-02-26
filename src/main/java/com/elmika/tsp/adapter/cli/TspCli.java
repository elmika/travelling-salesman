package com.elmika.tsp.adapter.cli;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.elmika.tsp.adapter.view.RouteCoordinatesJsonExporter;
import com.elmika.tsp.adapter.view.RouteCoordinatesMapper;
import com.elmika.tsp.adapter.view.RouteCoordinatesView;
import com.elmika.tsp.application.BenchmarkResult;
import com.elmika.tsp.application.BenchmarkTspUseCase;
import com.elmika.tsp.application.ConfigLoader;
import com.elmika.tsp.application.ProblemConfiguration;
import com.elmika.tsp.application.ProblemProvider;
import com.elmika.tsp.application.SolverConfiguration;
import com.elmika.tsp.application.TspBenchmark;
import com.elmika.tsp.application.TspSolver;
import com.elmika.tsp.domain.EuclideanProblem;
import com.elmika.tsp.domain.Problem;
import com.elmika.tsp.domain.Solution;

/**
 * Driving adapter: CLI entry point. Loads configuration, obtains problem,
 * runs the use case, and prints the solution to stdout.
 *
 * <p>When {@code resolutionStrategy} in the config file contains multiple
 * comma/space-separated strategy names, benchmark mode is activated: all
 * strategies are timed and results are printed ranked by solution quality.
 */
public class TspCli {

    private final ConfigLoader configLoader;
    private final ProblemProvider problemProvider;
    private final TspSolver solver;
    private final TspBenchmark benchmark;

    public TspCli(ConfigLoader configLoader, ProblemProvider problemProvider, TspSolver solver) {
        this(configLoader, problemProvider, solver, new BenchmarkTspUseCase(solver));
    }

    public TspCli(ConfigLoader configLoader, ProblemProvider problemProvider, TspSolver solver, TspBenchmark benchmark) {
        this.configLoader = configLoader;
        this.problemProvider = problemProvider;
        this.solver = solver;
        this.benchmark = benchmark;
    }

    public void run() {
        System.out.println("Traveling Salesman Problem Solver");

        ProblemConfiguration raw = configLoader.loadConfiguration();
        SolverConfiguration config = SolverConfiguration.createFrom(raw);
        Problem problem = problemProvider.create(config.getProblem());

        if (config.isBenchmark()) {
            List<BenchmarkResult> results = benchmark.benchmark(problem, config.getResolutionStrategies());
            displayBenchmarkResults(results, config.getProblem());
        } else {
            Solution solution = solver.solve(problem, config.getResolutionStrategy());
            exportRouteIfEuclidean(problem, solution);
            displaySolution(solution);
        }
    }

    private void displayBenchmarkResults(List<BenchmarkResult> results, String problemName) {
        System.out.printf("%nBenchmarking %d strategies on: %s%n%n", results.size(), problemName);
        System.out.printf("  %-3s  %-35s  %14s  %8s  %9s%n", "#", "Strategy", "Distance", "Gap %", "Time");
        System.out.printf("  %-3s  %-35s  %14s  %8s  %9s%n",
            "---", "-----------------------------------", "--------------", "--------", "---------");
        double best = results.get(0).getSolution().getTotalDistance();
        for (int i = 0; i < results.size(); i++) {
            BenchmarkResult r = results.get(i);
            double gap = (r.getSolution().getTotalDistance() - best) / best * 100.0;
            System.out.printf("  %3d  %-35s  %14.4f  %7.2f%%  %7d ms%n",
                i + 1,
                r.getStrategy(),
                r.getSolution().getTotalDistance(),
                gap,
                r.getDurationMs());
        }
        System.out.println();
    }

    private void exportRouteIfEuclidean(Problem problem, Solution solution) {
        if (!(problem instanceof EuclideanProblem)) {
            return;
        }
        try {
            RouteCoordinatesView view = RouteCoordinatesMapper.toView(problem, solution);
            String json = RouteCoordinatesJsonExporter.toJson(view);

            Path outputDir = Paths.get("output");
            Files.createDirectories(outputDir);
            Files.writeString(outputDir.resolve("route.json"), json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Could not write output/route.json: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("Could not export route.json: " + e.getMessage());
        }
    }

    private void displaySolution(Solution solution) {
        Integer[] route = solution.getRoute();
        StringBuilder sb = new StringBuilder();
        if (route.length > 0) {
            sb.append(route[0]);
            for (int i = 1; i < route.length; i++) {
                sb.append("->").append(route[i]);
            }
        }
        System.out.println("Solution is:" + sb);
        System.out.println("Distance is:" + solution.getTotalDistance());
    }
}
