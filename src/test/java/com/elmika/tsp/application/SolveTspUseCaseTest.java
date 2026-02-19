package com.elmika.tsp.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.elmika.tsp.domain.DistanceMatrixProblem;
import com.elmika.tsp.domain.Problem;
import com.elmika.tsp.domain.Solution;

public class SolveTspUseCaseTest {

    private Problem create4xProblem() {
        double[][] distanceMatrix = {
            {1, 1, 1, 1},
            {1, 1, 1, 1},
            {1, 1, 1, 1},
            {1, 1, 1, 1}
        };
        return new DistanceMatrixProblem(distanceMatrix);
    }

    @Test
    public void solveReturnsSolutionForBruteForce() {
        double[][] distances = {
            {0, 1, 1, 1},
            {1, 0, 1, 1},
            {1, 1, 0, 1},
            {1, 1, 1, 0}
        };
        Problem problem = new DistanceMatrixProblem(distances);
        TspSolver useCase = new SolveTspUseCase();

        Solution solution = useCase.solve(problem, "brute-force");

        assertEquals(4.0, solution.getTotalDistance());
        assertEquals(4, solution.getRoute().length);
    }

    @ParameterizedTest
    @ValueSource(strings = {"brute-force", "random", "random10", "random100"})
    public void solveReturnsSolutionForAllStrategies(String strategy) {
        Problem problem = create4xProblem();
        TspSolver useCase = new SolveTspUseCase();

        Solution result = useCase.solve(problem, strategy);

        assertEquals(4.0, result.getTotalDistance());
        assertEquals(4, result.getRoute().length);
    }

    @Test
    public void solveReturnsSolutionFor3xProblem() {
        double[][] distanceMatrix = {
            {1, 1, 1},
            {1, 1, 1},
            {1, 1, 1}
        };
        Problem problem = new DistanceMatrixProblem(distanceMatrix);
        TspSolver useCase = new SolveTspUseCase();

        Solution result = useCase.solve(problem, "brute-force");

        assertEquals(3.0, result.getTotalDistance());
        assertEquals(3, result.getRoute().length);
    }
}
