package com.elmika.tsp.application;

import com.elmika.tsp.application.improvement.CrossingEliminationSolver;
import com.elmika.tsp.application.improvement.OrOptSolver;
import com.elmika.tsp.application.improvement.SimulatedAnnealingSolver;
import com.elmika.tsp.application.improvement.TwoOptSolver;
import com.elmika.tsp.application.resolution.BruteForceSolver;
import com.elmika.tsp.application.resolution.GreedyEdgeSolver;
import com.elmika.tsp.application.resolution.NearestNeighborSolver;
import com.elmika.tsp.application.resolution.RandomSolver;
import com.elmika.tsp.application.resolution.ResolutionStrategy;
import com.elmika.tsp.domain.problem.Problem;
import com.elmika.tsp.domain.solution.Solution;

/**
 * Application use case implementing TspSolver.
 * Resolves the strategy name to a ResolutionStrategy and delegates to it.
 */
public class SolveTspUseCase implements TspSolver {

    @Override
    public Solution solve(Problem problem, String strategy) {
        ResolutionStrategy solverStrategy = resolveStrategy(strategy);
        return solverStrategy.solve(problem);
    }

    private static ResolutionStrategy resolveStrategy(String strategy) {
        String name = strategy != null ? strategy : "random10";
        switch (name) {
            case "random":
                return new RandomSolver(1);
            case "random10":
                return new RandomSolver(10);
            case "random100":
                return new RandomSolver(100);
            case "brute-force":
                return new BruteForceSolver();
            case "nearest-neighbor":
                return new NearestNeighborSolver();
            case "nearest-neighbor-2opt":
                return new TwoOptSolver(new NearestNeighborSolver());
            case "nearest-neighbor-uncrossing":
                return new CrossingEliminationSolver(new NearestNeighborSolver());
            case "greedy-edge":
                return new GreedyEdgeSolver();
            case "greedy-edge-2opt":
                return new TwoOptSolver(new GreedyEdgeSolver());
            case "nearest-neighbor-oropt":
                return new OrOptSolver(new NearestNeighborSolver());
            case "greedy-edge-oropt":
                return new OrOptSolver(new GreedyEdgeSolver());
            case "nearest-neighbor-sa":
                return new SimulatedAnnealingSolver(new NearestNeighborSolver());
            case "greedy-edge-sa":
                return new SimulatedAnnealingSolver(new GreedyEdgeSolver());
            default:
                throw new IllegalArgumentException("Unknown strategy: " + name);
        }
    }
}
