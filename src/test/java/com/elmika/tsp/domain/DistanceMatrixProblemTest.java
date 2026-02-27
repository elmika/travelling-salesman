package com.elmika.tsp.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.elmika.tsp.domain.problem.DistanceMatrixProblem;
import com.elmika.tsp.domain.problem.Problem;

public class DistanceMatrixProblemTest {

    @Test
    public void getSizeReturnsMatrixLength() {
        double[][] matrix = {
            {0.0, 1.0, 2.0},
            {1.0, 0.0, 3.0},
            {2.0, 3.0, 0.0}
        };
        Problem problem = new DistanceMatrixProblem(matrix);

        assertEquals(3, problem.getSize());
    }

    @Test
    public void getDistanceReturnsCorrectValueFor1BasedIndices() {
        double[][] matrix = {
            {0.0, 3.0, 4.0},
            {3.0, 0.0, 5.0},
            {4.0, 5.0, 0.0}
        };
        Problem problem = new DistanceMatrixProblem(matrix);

        assertEquals(3.0, problem.getDistance(1, 2));
        assertEquals(5.0, problem.getDistance(2, 3));
        assertEquals(4.0, problem.getDistance(1, 3));
    }

    @Test
    public void getDistanceIsSymmetricForSymmetricMatrix() {
        double[][] matrix = {
            {0.0, 2.0},
            {2.0, 0.0}
        };
        Problem problem = new DistanceMatrixProblem(matrix);

        assertEquals(problem.getDistance(1, 2), problem.getDistance(2, 1));
    }

    @Test
    public void getDistanceReturnsDiagonalValue() {
        double[][] matrix = {
            {0.0, 1.0},
            {1.0, 0.0}
        };
        Problem problem = new DistanceMatrixProblem(matrix);

        assertEquals(0.0, problem.getDistance(1, 1));
        assertEquals(0.0, problem.getDistance(2, 2));
    }
}
