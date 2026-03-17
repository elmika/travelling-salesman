package com.elmika.tsp.adapter.api.dto;

import java.util.List;

public class SolveRequest {
    private List<PointDto> points;
    private String strategy;

    public SolveRequest() {}

    public List<PointDto> getPoints() { return points; }
    public void setPoints(List<PointDto> points) { this.points = points; }
    public String getStrategy() { return strategy; }
    public void setStrategy(String strategy) { this.strategy = strategy; }
}
