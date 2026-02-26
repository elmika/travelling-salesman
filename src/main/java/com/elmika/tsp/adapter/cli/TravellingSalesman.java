package com.elmika.tsp.adapter.cli;

import com.elmika.tsp.application.BenchmarkTspUseCase;
import com.elmika.tsp.application.SolveTspUseCase;
import com.elmika.tsp.application.TspSolver;
import com.elmika.tsp.application.ConfigLoader;
import com.elmika.tsp.application.ProblemProvider;
import com.elmika.tsp.infrastructure.InMemoryProblemFactory;
import com.elmika.tsp.infrastructure.JsonFileConfigLoader;

/**
 * Composition root: wires adapters and use case, then runs the CLI.
 */
public class TravellingSalesman {

    public static void main(String[] args) {
        ConfigLoader configLoader = new JsonFileConfigLoader();
        ProblemProvider problemProvider = new InMemoryProblemFactory();
        TspSolver solver = new SolveTspUseCase();

        TspCli cli = new TspCli(configLoader, problemProvider, solver, new BenchmarkTspUseCase(solver));
        cli.run();
    }
}
