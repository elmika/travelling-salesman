package com.elmika.tsp.adapter.api;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.elmika.tsp.application.resolution.BruteForceSolver;
import com.elmika.tsp.application.resolution.GreedyEdgeSolver;
import com.elmika.tsp.application.resolution.NearestNeighborSolver;
import com.elmika.tsp.application.resolution.RandomSolver;
import com.elmika.tsp.application.resolution.ResolutionStrategy;
import com.elmika.tsp.domain.problem.Problem;
import com.elmika.tsp.domain.solution.Solution;

/**
 * API-layer service that maps resolution strategy names to ResolutionStrategy instances.
 * Exposes only simple (non-compound) strategies for the REST API.
 */
public class ResolutionService {

    private final Map<String, ResolutionStrategy> strategies = new LinkedHashMap<>();

    public ResolutionService() {
        strategies.put("brute-force", new BruteForceSolver());
        strategies.put("random", new RandomSolver(1));
        strategies.put("random10", new RandomSolver(10));
        strategies.put("random100", new RandomSolver(100));
        strategies.put("nearest-neighbor", new NearestNeighborSolver());
        strategies.put("greedy-edge", new GreedyEdgeSolver());
    }

    public Solution solve(Problem problem, String strategyName) {
        ResolutionStrategy strategy = strategies.get(strategyName);
        if (strategy == null) {
            throw new IllegalArgumentException(
                "Unknown resolution strategy: '" + strategyName + "'. "
                + "Allowed: " + String.join(", ", strategies.keySet()));
        }
        return strategy.solve(problem);
    }

    public Set<String> getStrategyNames() {
        return strategies.keySet();
    }
}
