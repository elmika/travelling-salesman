package com.elmika.tsp.adapter.api.dto;

public class BenchmarkResultDto {
    private int rank;
    private String label;
    private double distance;
    private double gapPercent;
    private long durationMs;

    public BenchmarkResultDto() {}

    public BenchmarkResultDto(int rank, String label, double distance, double gapPercent, long durationMs) {
        this.rank = rank;
        this.label = label;
        this.distance = distance;
        this.gapPercent = gapPercent;
        this.durationMs = durationMs;
    }

    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }
    public double getGapPercent() { return gapPercent; }
    public void setGapPercent(double gapPercent) { this.gapPercent = gapPercent; }
    public long getDurationMs() { return durationMs; }
    public void setDurationMs(long durationMs) { this.durationMs = durationMs; }
}
