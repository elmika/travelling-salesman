package com.elmika.tsp.application;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Value object representing a valid solver configuration.
 * Only obtain via {@link #createFrom(ProblemConfiguration)}; invalid input throws.
 *
 * <p>{@code resolutionStrategy} in the config file may contain a single strategy name
 * or multiple names separated by commas and/or whitespace. When multiple strategies are
 * present, {@link #isBenchmark()} returns true and {@link #getResolutionStrategies()}
 * returns all of them; in that case the CLI runs a full benchmark comparison.
 */
public final class SolverConfiguration {

    private static final Set<String> ALLOWED_STRATEGIES = Set.of(
        "brute-force", "random", "random10", "random100", "nearest-neighbor", "nearest-neighbor-2opt",
        "nearest-neighbor-uncrossing", "greedy-edge", "greedy-edge-2opt",
        "nearest-neighbor-oropt", "greedy-edge-oropt",
        "nearest-neighbor-sa", "greedy-edge-sa"
    );

    private final String problem;
    private final List<String> resolutionStrategies;

    private SolverConfiguration(String problem, List<String> resolutionStrategies) {
        this.problem = problem;
        this.resolutionStrategies = resolutionStrategies;
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

        String strategyRaw = raw.getResolutionStrategy();
        if (strategyRaw != null) {
            strategyRaw = strategyRaw.trim();
        }
        if (strategyRaw == null || strategyRaw.isBlank()) {
            throw new IllegalArgumentException(
                "Configuration error: 'resolutionStrategy' must not be null or blank. " +
                "Allowed: " + allowedStrategiesHint() + ".");
        }

        List<String> strategies = Arrays.stream(strategyRaw.split("[,\\s]+"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        for (String s : strategies) {
            if (!ALLOWED_STRATEGIES.contains(s)) {
                throw new IllegalArgumentException(
                    "Configuration error: unknown resolution strategy '" + s + "'. " +
                    "Allowed: " + allowedStrategiesHint() + ".");
            }
        }

        return new SolverConfiguration(problem, strategies);
    }

    public String getProblem() {
        return problem;
    }

    /** Returns the single configured strategy. Use only when {@link #isBenchmark()} is false. */
    public String getResolutionStrategy() {
        return resolutionStrategies.get(0);
    }

    /** Returns all configured strategies. Contains exactly one entry in single-run mode. */
    public List<String> getResolutionStrategies() {
        return Collections.unmodifiableList(resolutionStrategies);
    }

    /** Returns true when more than one strategy is configured, triggering benchmark mode. */
    public boolean isBenchmark() {
        return resolutionStrategies.size() > 1;
    }

    private static String allowedStrategiesHint() {
        return String.join(", ", ALLOWED_STRATEGIES);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SolverConfiguration)) return false;
        SolverConfiguration that = (SolverConfiguration) o;
        return problem.equals(that.problem) && resolutionStrategies.equals(that.resolutionStrategies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(problem, resolutionStrategies);
    }
}
