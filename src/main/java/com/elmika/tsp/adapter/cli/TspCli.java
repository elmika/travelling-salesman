package com.elmika.tsp.adapter.cli;

import com.elmika.tsp.application.ConfigLoader;
import com.elmika.tsp.application.ProblemConfiguration;
import com.elmika.tsp.application.ProblemProvider;
import com.elmika.tsp.application.TspSolver;
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

        ProblemConfiguration config = configLoader.loadConfiguration();
        Problem problem = problemProvider.create(config.getProblem());
        Solution solution = solver.solve(problem, config.getResolutionStrategy());

        displaySolution(solution);
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
