package com.elmika.tsp.adapter.api.dto;

import java.util.List;

public class SolutionDto {
    private List<Integer> route;
    private double totalDistance;

    public SolutionDto() {}

    public List<Integer> getRoute() { return route; }
    public void setRoute(List<Integer> route) { this.route = route; }
    public double getTotalDistance() { return totalDistance; }
    public void setTotalDistance(double totalDistance) { this.totalDistance = totalDistance; }
}
