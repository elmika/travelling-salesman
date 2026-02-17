package com.elmika.tsp;

/**
 * Composition root: wires adapters and use case, then runs the CLI.
 */
public class TravellingSalesman {

    public static void main(String[] args) {
        ConfigLoader configLoader = new JsonFileConfigLoader();
        ProblemProvider problemProvider = new InMemoryProblemFactory();
        TspSolver solver = new SolveTspUseCase();

        TspCli cli = new TspCli(configLoader, problemProvider, solver);
        cli.run();
    }
}
