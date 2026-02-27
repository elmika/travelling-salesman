package com.elmika.tsp.adapter.api;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elmika.tsp.adapter.api.dto.ImproveRequest;
import com.elmika.tsp.adapter.api.dto.ImproveResponse;
import com.elmika.tsp.domain.problem.EuclideanProblem;
import com.elmika.tsp.domain.solution.Solution;

@RestController
@RequestMapping("/api/improve")
public class ImproveController {

    private final ImprovementService improvementService;

    public ImproveController(ImprovementService improvementService) {
        this.improvementService = improvementService;
    }

    @PostMapping
    public ImproveResponse improve(@RequestBody ImproveRequest request) {
        if (request.getPoints() == null || request.getPoints().isEmpty()) {
            throw new IllegalArgumentException("'points' must not be null or empty.");
        }
        if (request.getSolution() == null || request.getSolution().getRoute() == null) {
            throw new IllegalArgumentException("'solution.route' must not be null.");
        }
        if (request.getStrategy() == null || request.getStrategy().isBlank()) {
            throw new IllegalArgumentException("'strategy' must not be null or blank.");
        }

        EuclideanProblem problem = SolveController.toEuclideanProblem(request.getPoints());
        List<Integer> routeList = request.getSolution().getRoute();
        Integer[] routeArray = routeList.toArray(new Integer[0]);
        Solution initial = new Solution(routeArray, request.getSolution().getTotalDistance());

        double originalDistance = initial.getTotalDistance();

        long startNs = System.nanoTime();
        Solution improved = improvementService.improve(problem, initial, request.getStrategy());
        long durationMs = (System.nanoTime() - startNs) / 1_000_000L;

        double improvementPercent = originalDistance > 0
            ? Math.round((originalDistance - improved.getTotalDistance()) / originalDistance * 1000.0) / 10.0
            : 0.0;

        return new ImproveResponse(
            Arrays.asList(improved.getRoute()),
            improved.getTotalDistance(),
            request.getStrategy(),
            originalDistance,
            improvementPercent,
            durationMs
        );
    }
}
