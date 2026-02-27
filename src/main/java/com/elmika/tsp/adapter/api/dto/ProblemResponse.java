package com.elmika.tsp.adapter.api.dto;

import java.util.List;

public class ProblemResponse {
    private List<PointDto> points;

    public ProblemResponse() {}

    public ProblemResponse(List<PointDto> points) {
        this.points = points;
    }

    public List<PointDto> getPoints() { return points; }
    public void setPoints(List<PointDto> points) { this.points = points; }
}
