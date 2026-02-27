package com.elmika.tsp.adapter.api.dto;

import java.util.List;

public class ImproveRequest {
    private List<PointDto> points;
    private SolutionDto solution;
    private String strategy;

    public ImproveRequest() {}

    public List<PointDto> getPoints() { return points; }
    public void setPoints(List<PointDto> points) { this.points = points; }
    public SolutionDto getSolution() { return solution; }
    public void setSolution(SolutionDto solution) { this.solution = solution; }
    public String getStrategy() { return strategy; }
    public void setStrategy(String strategy) { this.strategy = strategy; }
}
