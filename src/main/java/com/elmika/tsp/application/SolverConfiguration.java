package com.elmika.tsp.application;

import java.util.Objects;
import java.util.Set;

/**
 * Value object representing a valid solver configuration.
 * Only obtain via {@link #createFrom(ProblemConfiguration)}; invalid input throws.
 */
public final class SolverConfiguration {

    private static final Set<String> ALLOWED_STRATEGIES = Set.of(
        "brute-force", "random", "random10", "random100", "nearest-neighbor", "nearest-neighbor-2opt"
    );

    private final String problem;
    private final String resolutionStrategy;

    private SolverConfiguration(String problem, String resolutionStrategy) {
        this.problem = problem;
        this.resolutionStrategy = resolutionStrategy;
    }

    /**
     * Creates a valid configuration from the raw DTO. Throws if the DTO is invalid.
     *
     * @param raw the configuration loaded from config (e.g. JSON); must not be null
     * @return a valid SolverConfiguration
     * @throws IllegalArgumentException if problem or resolutionStrategy is null, blank, or not allowed
     */
    public static SolverConfiguration createFrom(ProblemConfiguration raw) {
        if (raw == null) {
            throw new IllegalArgumentException("Configuration must not be null.");
        }

        String problem = raw.getProblem();
        if (problem != null) {
            problem = problem.trim();
        }
        if (problem == null || problem.isBlank()) {
            throw new IllegalArgumentException(
                "Configuration error: 'problem' must not be null or blank. " +
                "Examples: trivial, simple, cities10, fully-random50.");
        }

        ProblemTypeParser.validateProblemType(problem);

        String strategy = raw.getResolutionStrategy();
        if (strategy != null) {
            strategy = strategy.trim();
        }
        if (strategy == null || strategy.isBlank()) {
            throw new IllegalArgumentException(
                "Configuration error: 'resolutionStrategy' must not be null or blank. " +
                "Allowed: brute-force, random, random10, random100, nearest-neighbor, nearest-neighbor-2opt.");
        }

        if (!ALLOWED_STRATEGIES.contains(strategy)) {
            throw new IllegalArgumentException(
                "Configuration error: unknown resolution strategy '" + strategy + "'. " +
                "Allowed: brute-force, random, random10, random100, nearest-neighbor, nearest-neighbor-2opt.");
        }

        return new SolverConfiguration(problem, strategy);
    }

    public String getProblem() {
        return problem;
    }

    public String getResolutionStrategy() {
        return resolutionStrategy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SolverConfiguration)) return false;
        SolverConfiguration that = (SolverConfiguration) o;
        return problem.equals(that.problem) && resolutionStrategy.equals(that.resolutionStrategy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(problem, resolutionStrategy);
    }
}
