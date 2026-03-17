package com.elmika.tsp.adapter.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elmika.tsp.adapter.api.dto.PointDto;
import com.elmika.tsp.adapter.api.dto.ProblemResponse;
import com.elmika.tsp.application.ProblemProvider;
import com.elmika.tsp.domain.problem.EuclideanProblem;
import com.elmika.tsp.domain.problem.Problem;

@RestController
@RequestMapping("/api/problems")
public class ProblemController {

    private final ProblemProvider problemProvider;

    public ProblemController(ProblemProvider problemProvider) {
        this.problemProvider = problemProvider;
    }

    @GetMapping("/{type}")
    public ProblemResponse getProblem(@PathVariable String type) {
        Problem problem = problemProvider.create(type);
        if (!(problem instanceof EuclideanProblem)) {
            throw new IllegalArgumentException(
                "Problem type '" + type + "' is not a Euclidean problem and cannot be returned as points.");
        }
        EuclideanProblem euclidean = (EuclideanProblem) problem;
        List<PointDto> points = new ArrayList<>(euclidean.getSize());
        for (int i = 1; i <= euclidean.getSize(); i++) {
            points.add(new PointDto(euclidean.getPoint(i).getX(), euclidean.getPoint(i).getY()));
        }
        return new ProblemResponse(points);
    }
}
