package com.elmika.tsp.adapter.api.dto;

import java.util.List;

public class BenchmarkResponse {
    private List<BenchmarkResultDto> results;

    public BenchmarkResponse() {}

    public BenchmarkResponse(List<BenchmarkResultDto> results) {
        this.results = results;
    }

    public List<BenchmarkResultDto> getResults() { return results; }
    public void setResults(List<BenchmarkResultDto> results) { this.results = results; }
}
