package com.elmika.tsp.adapter.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elmika.tsp.adapter.api.dto.ProblemTypesResponse;
import com.elmika.tsp.application.ProblemProvider;

@RestController
@RequestMapping("/api/problem-types")
public class ProblemTypesController {

    private final ProblemProvider problemProvider;

    public ProblemTypesController(ProblemProvider problemProvider) {
        this.problemProvider = problemProvider;
    }

    @GetMapping
    public ProblemTypesResponse getProblemTypes() {
        return new ProblemTypesResponse(problemProvider.tsplibNames());
    }
}
