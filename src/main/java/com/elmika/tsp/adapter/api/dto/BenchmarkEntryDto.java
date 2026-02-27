package com.elmika.tsp.adapter.api.dto;

public class BenchmarkEntryDto {
    private String label;
    private double distance;
    private long durationMs;

    public BenchmarkEntryDto() {}

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }
    public long getDurationMs() { return durationMs; }
    public void setDurationMs(long durationMs) { this.durationMs = durationMs; }
}
