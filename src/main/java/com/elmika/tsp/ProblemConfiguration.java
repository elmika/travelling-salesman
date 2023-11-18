package com.elmika.tsp;

public class ProblemConfiguration {
    
    private String problem;
    private String resolutionStrategy;

    public ProblemConfiguration(String problem, String strategy){
        this.problem = problem;
        this.resolutionStrategy = strategy;
    }

    public String getProblem() {
        return this.problem;
    }

    String getResolutionStrategy(){
        return this.resolutionStrategy;
    }

}