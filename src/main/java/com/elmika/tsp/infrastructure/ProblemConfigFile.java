package com.elmika.tsp.infrastructure;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Typed representation of the problemConfiguration.json file.
 * Only a subset of fields are currently used by the application.
 */
public class ProblemConfigFile {

    private String problem;
    private String resolutionStrategy;

    // Additional documentation / metadata fields from the JSON are kept
    // for future use but are not yet interpreted by the core application.
    private List<String> possibleProblems;
    private List<String> possibleResolutionStrategies;

    @JsonProperty("X")
    private String X;

    @JsonProperty("Y")
    private String Y;

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getResolutionStrategy() {
        return resolutionStrategy;
    }

    public void setResolutionStrategy(String resolutionStrategy) {
        this.resolutionStrategy = resolutionStrategy;
    }

    public List<String> getPossibleProblems() {
        return possibleProblems;
    }

    public void setPossibleProblems(List<String> possibleProblems) {
        this.possibleProblems = possibleProblems;
    }

    public List<String> getPossibleResolutionStrategies() {
        return possibleResolutionStrategies;
    }

    public void setPossibleResolutionStrategies(List<String> possibleResolutionStrategies) {
        this.possibleResolutionStrategies = possibleResolutionStrategies;
    }

    public String getX() {
        return X;
    }

    public void setX(String x) {
        X = x;
    }

    public String getY() {
        return Y;
    }

    public void setY(String y) {
        Y = y;
    }
}

