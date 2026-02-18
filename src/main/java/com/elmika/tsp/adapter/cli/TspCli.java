package com.elmika.tsp.adapter.cli;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.elmika.tsp.adapter.view.RouteCoordinatesJsonExporter;
import com.elmika.tsp.adapter.view.RouteCoordinatesMapper;
import com.elmika.tsp.adapter.view.RouteCoordinatesView;
import com.elmika.tsp.application.ConfigLoader;
import com.elmika.tsp.application.ProblemConfiguration;
import com.elmika.tsp.application.ProblemProvider;
import com.elmika.tsp.application.SolverConfiguration;
import com.elmika.tsp.application.TspSolver;
import com.elmika.tsp.domain.EuclideanProblem;
import com.elmika.tsp.domain.Problem;
import com.elmika.tsp.domain.Solution;

/**
 * Driving adapter: CLI entry point. Loads configuration, obtains problem,
 * runs the use case, and prints the solution to stdout.
 */
public class TspCli {

    private final ConfigLoader configLoader;
    private final ProblemProvider problemProvider;
    private final TspSolver solver;

    public TspCli(ConfigLoader configLoader, ProblemProvider problemProvider, TspSolver solver) {
        this.configLoader = configLoader;
        this.problemProvider = problemProvider;
        this.solver = solver;
    }

    public void run() {
        System.out.println("Traveling Salesman Problem Solver");

        ProblemConfiguration raw = configLoader.loadConfiguration();
        SolverConfiguration config = SolverConfiguration.createFrom(raw);
        Problem problem = problemProvider.create(config.getProblem());
        Solution solution = solver.solve(problem, config.getResolutionStrategy());

        exportRouteIfEuclidean(problem, solution);
        displaySolution(solution);
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
