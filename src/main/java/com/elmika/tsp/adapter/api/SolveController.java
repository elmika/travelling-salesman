package com.elmika.tsp.adapter.api;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elmika.tsp.adapter.api.dto.PointDto;
import com.elmika.tsp.adapter.api.dto.SolveRequest;
import com.elmika.tsp.adapter.api.dto.SolveResponse;
import com.elmika.tsp.domain.problem.EuclideanProblem;
import com.elmika.tsp.domain.solution.Solution;

@RestController
@RequestMapping("/api/solve")
public class SolveController {

    private final ResolutionService resolutionService;

    public SolveController(ResolutionService resolutionService) {
        this.resolutionService = resolutionService;
    }

    @PostMapping
    public SolveResponse solve(@RequestBody SolveRequest request) {
        if (request.getPoints() == null || request.getPoints().isEmpty()) {
            throw new IllegalArgumentException("'points' must not be null or empty.");
        }
        if (request.getStrategy() == null || request.getStrategy().isBlank()) {
            throw new IllegalArgumentException("'strategy' must not be null or blank.");
        }

        EuclideanProblem problem = toEuclideanProblem(request.getPoints());

        long startNs = System.nanoTime();
        Solution solution = resolutionService.solve(problem, request.getStrategy());
        long durationMs = (System.nanoTime() - startNs) / 1_000_000L;

        return new SolveResponse(
            Arrays.asList(solution.getRoute()),
            solution.getTotalDistance(),
            request.getStrategy(),
            durationMs
        );
    }

    static EuclideanProblem toEuclideanProblem(List<PointDto> points) {
        double[][] coords = new double[points.size()][2];
        for (int i = 0; i < points.size(); i++) {
            coords[i][0] = points.get(i).getX();
            coords[i][1] = points.get(i).getY();
        }
        return new EuclideanProblem(coords);
    }
}
