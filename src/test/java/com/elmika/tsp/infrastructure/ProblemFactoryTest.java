package com.elmika.tsp.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.elmika.tsp.domain.EuclideanProblem;
import com.elmika.tsp.domain.Problem;

public class ProblemFactoryTest {

    @Test
    public void trivialReturnsProblemWithSize4() {
        Problem problem = ProblemFactory.createProblem("trivial");
        assertEquals(4, problem.getSize());
    }

    @Test
    public void simpleReturnsProblemWithSize5() {
        Problem problem = ProblemFactory.createProblem("simple");
        assertEquals(5, problem.getSize());
    }

    @Test
    public void biggerReturnsProblemWithSize6() {
        Problem problem = ProblemFactory.createProblem("bigger");
        assertEquals(6, problem.getSize());
    }

    @Test
    public void euclideanReturnsEuclideanProblemWithSize7() {
        Problem problem = ProblemFactory.createProblem("euclidean");
        assertTrue(problem instanceof EuclideanProblem);
        assertEquals(7, problem.getSize());
    }

    @Test
    public void citiesTypeUsesTrailingDigitsAsSize() {
        Problem problem = ProblemFactory.createProblem("cities10");
        assertTrue(problem instanceof EuclideanProblem);
        assertEquals(10, problem.getSize());
    }

    @Test
    public void fullyRandomReturnsEuclideanProblemWithRequestedSize() {
        Problem problem = ProblemFactory.createProblem("fully-random10");
        assertTrue(problem instanceof EuclideanProblem);
        assertEquals(10, problem.getSize());
    }

    @Test
    public void partiallyRandomReturnsEuclideanProblemWithRequestedSize() {
        Problem problem = ProblemFactory.createProblem("partially-random5");
        assertTrue(problem instanceof EuclideanProblem);
        assertEquals(5, problem.getSize());
    }
}
