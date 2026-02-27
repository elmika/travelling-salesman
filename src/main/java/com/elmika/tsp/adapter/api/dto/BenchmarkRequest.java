package com.elmika.tsp.adapter.api.dto;

import java.util.List;

public class BenchmarkRequest {
    private List<BenchmarkEntryDto> solutions;

    public BenchmarkRequest() {}

    public List<BenchmarkEntryDto> getSolutions() { return solutions; }
    public void setSolutions(List<BenchmarkEntryDto> solutions) { this.solutions = solutions; }
}
