package com.elmika.tsp.adapter.api;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elmika.tsp.adapter.api.dto.BenchmarkRequest;
import com.elmika.tsp.adapter.api.dto.BenchmarkResponse;
import com.elmika.tsp.adapter.api.dto.BenchmarkResultDto;

@RestController
@RequestMapping("/api/benchmark")
public class BenchmarkController {

    private final BenchmarkService benchmarkService;

    public BenchmarkController(BenchmarkService benchmarkService) {
        this.benchmarkService = benchmarkService;
    }

    @PostMapping
    public BenchmarkResponse benchmark(@RequestBody BenchmarkRequest request) {
        if (request.getSolutions() == null || request.getSolutions().isEmpty()) {
            throw new IllegalArgumentException("'solutions' must not be null or empty.");
        }
        List<BenchmarkResultDto> results = benchmarkService.rank(request.getSolutions());
        return new BenchmarkResponse(results);
    }
}
