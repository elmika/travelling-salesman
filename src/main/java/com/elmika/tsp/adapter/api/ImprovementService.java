package com.elmika.tsp.adapter.api;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.elmika.tsp.application.improvement.CrossingEliminationSolver;
import com.elmika.tsp.application.improvement.ImprovementStrategy;
import com.elmika.tsp.application.improvement.OrOptSolver;
import com.elmika.tsp.application.improvement.SimulatedAnnealingSolver;
import com.elmika.tsp.application.improvement.TwoOptSolver;
import com.elmika.tsp.domain.problem.Problem;
import com.elmika.tsp.domain.solution.Solution;

/**
 * API-layer service that maps improvement strategy names to ImprovementStrategy instances.
 */
public class ImprovementService {

    private final Map<String, ImprovementStrategy> strategies = new LinkedHashMap<>();

    public ImprovementService() {
        strategies.put("2opt", new TwoOptSolver());
        strategies.put("oropt", new OrOptSolver());
        strategies.put("sa", new SimulatedAnnealingSolver());
        strategies.put("uncrossing", new CrossingEliminationSolver());
    }

    public Solution improve(Problem problem, Solution initial, String strategyName) {
        ImprovementStrategy strategy = strategies.get(strategyName);
        if (strategy == null) {
            throw new IllegalArgumentException(
                "Unknown improvement strategy: '" + strategyName + "'. "
                + "Allowed: " + String.join(", ", strategies.keySet()));
        }
        return strategy.improve(problem, initial);
    }

    public Set<String> getStrategyNames() {
        return strategies.keySet();
    }
}
