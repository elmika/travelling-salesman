package com.elmika.tsp.adapter.api.dto;

import java.util.List;

public class ImproveResponse {
    private List<Integer> route;
    private double totalDistance;
    private String strategy;
    private double originalDistance;
    private double improvementPercent;
    private long durationMs;

    public ImproveResponse() {}

    public ImproveResponse(List<Integer> route, double totalDistance, String strategy,
                           double originalDistance, double improvementPercent, long durationMs) {
        this.route = route;
        this.totalDistance = totalDistance;
        this.strategy = strategy;
        this.originalDistance = originalDistance;
        this.improvementPercent = improvementPercent;
        this.durationMs = durationMs;
    }

    public List<Integer> getRoute() { return route; }
    public void setRoute(List<Integer> route) { this.route = route; }
    public double getTotalDistance() { return totalDistance; }
    public void setTotalDistance(double totalDistance) { this.totalDistance = totalDistance; }
    public String getStrategy() { return strategy; }
    public void setStrategy(String strategy) { this.strategy = strategy; }
    public double getOriginalDistance() { return originalDistance; }
    public void setOriginalDistance(double originalDistance) { this.originalDistance = originalDistance; }
    public double getImprovementPercent() { return improvementPercent; }
    public void setImprovementPercent(double improvementPercent) { this.improvementPercent = improvementPercent; }
    public long getDurationMs() { return durationMs; }
    public void setDurationMs(long durationMs) { this.durationMs = durationMs; }
}
