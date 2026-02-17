package com.elmika.tsp.application;

public class ProblemConfiguration {

    private String problem;
    private String resolutionStrategy;

    public ProblemConfiguration(String problem, String strategy) {
        this.problem = problem;
        this.resolutionStrategy = strategy;
    }

    public String getProblem() {
        return this.problem;
    }

    public String getResolutionStrategy() {
        return this.resolutionStrategy;
    }
}
