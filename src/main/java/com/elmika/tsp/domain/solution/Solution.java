package com.elmika.tsp.domain.solution;

import java.util.Arrays;

public class Solution {

    private final Integer[] route;
    private final double totalDistance;

    public Solution(Integer[] route, double totalDistance) {
        if (route == null) {
            throw new IllegalArgumentException("Route cannot be null.");
        }
        this.route = route.clone();
        this.totalDistance = totalDistance;
    }

    public Integer[] getRoute() {
        return route.clone();
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    @Override
    public String toString() {
        return "Solution{route=" + Arrays.toString(route) + ", totalDistance=" + totalDistance + "}";
    }
}
