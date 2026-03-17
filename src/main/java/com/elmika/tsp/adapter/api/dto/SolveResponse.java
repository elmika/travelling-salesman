package com.elmika.tsp.adapter.api.dto;

import java.util.List;

public class SolveResponse {
    private List<Integer> route;
    private double totalDistance;
    private String strategy;
    private long durationMs;

    public SolveResponse() {}

    public SolveResponse(List<Integer> route, double totalDistance, String strategy, long durationMs) {
        this.route = route;
        this.totalDistance = totalDistance;
        this.strategy = strategy;
        this.durationMs = durationMs;
    }

    public List<Integer> getRoute() { return route; }
    public void setRoute(List<Integer> route) { this.route = route; }
    public double getTotalDistance() { return totalDistance; }
    public void setTotalDistance(double totalDistance) { this.totalDistance = totalDistance; }
    public String getStrategy() { return strategy; }
    public void setStrategy(String strategy) { this.strategy = strategy; }
    public long getDurationMs() { return durationMs; }
    public void setDurationMs(long durationMs) { this.durationMs = durationMs; }
}
