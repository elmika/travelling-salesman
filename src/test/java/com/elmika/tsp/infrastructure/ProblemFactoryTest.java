package com.elmika.tsp.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.elmika.tsp.domain.problem.EuclideanProblem;
import com.elmika.tsp.domain.problem.Problem;

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
    public void citiesTypeExtendedTo30() {
        Problem problem = ProblemFactory.createProblem("cities30");
        assertTrue(problem instanceof EuclideanProblem);
        assertEquals(30, problem.getSize());
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

    @Test
    public void partiallyRandomIsReproducibleForSameSize() {
        Problem p1 = ProblemFactory.createProblem("partially-random10");
        Problem p2 = ProblemFactory.createProblem("partially-random10");
        EuclideanProblem e1 = (EuclideanProblem) p1;
        EuclideanProblem e2 = (EuclideanProblem) p2;
        for (int i = 1; i <= 10; i++) {
            assertEquals(e1.getPoint(i).getX(), e2.getPoint(i).getX());
            assertEquals(e1.getPoint(i).getY(), e2.getPoint(i).getY());
        }
    }

    @Test
    public void partiallyRandomDifferentSizesProduceDifferentPoints() {
        EuclideanProblem p5  = (EuclideanProblem) ProblemFactory.createProblem("partially-random5");
        EuclideanProblem p10 = (EuclideanProblem) ProblemFactory.createProblem("partially-random10");
        // First city of size-5 should differ from first city of size-10 (independent seeds)
        boolean differs = p5.getPoint(1).getX() != p10.getPoint(1).getX()
                       || p5.getPoint(1).getY() != p10.getPoint(1).getY();
        assertTrue(differs, "partially-random5 and partially-random10 should have independent first cities");
    }

    @Test
    public void circleReturnsEuclideanProblemWithRequestedSize() {
        Problem problem = ProblemFactory.createProblem("circle-12");
        assertTrue(problem instanceof EuclideanProblem);
        assertEquals(12, problem.getSize());
    }

    @Test
    public void circleIsSymmetric() {
        // All cities on the circle should be equidistant from the centre (50,50)
        EuclideanProblem p = (EuclideanProblem) ProblemFactory.createProblem("circle-8");
        double expectedR = 40.0;
        for (int i = 1; i <= 8; i++) {
            double dx = p.getPoint(i).getX() - 50.0;
            double dy = p.getPoint(i).getY() - 50.0;
            assertEquals(expectedR, Math.sqrt(dx * dx + dy * dy), 1e-9);
        }
    }

    @Test
    public void clusterReturnsEuclideanProblemWithRequestedSize() {
        Problem problem = ProblemFactory.createProblem("cluster-20");
        assertTrue(problem instanceof EuclideanProblem);
        assertEquals(20, problem.getSize());
    }

    @Test
    public void clusterIsReproducibleForSameSize() {
        EuclideanProblem p1 = (EuclideanProblem) ProblemFactory.createProblem("cluster-15");
        EuclideanProblem p2 = (EuclideanProblem) ProblemFactory.createProblem("cluster-15");
        for (int i = 1; i <= 15; i++) {
            assertEquals(p1.getPoint(i).getX(), p2.getPoint(i).getX());
            assertEquals(p1.getPoint(i).getY(), p2.getPoint(i).getY());
        }
    }

    @Test
    public void unknownTypeThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> ProblemFactory.createProblem("unknown-type"));
    }

    // ── TSPLIB problems ───────────────────────────────────────────

    @Test
    public void tsplibBerlin52ReturnsProblemWithCorrectSize() {
        Problem problem = ProblemFactory.createProblem("tsplib-berlin52");
        assertTrue(problem instanceof EuclideanProblem);
        assertEquals(52, problem.getSize());
    }

    @Test
    public void tsplibEil51ReturnsProblemWithCorrectSize() {
        Problem problem = ProblemFactory.createProblem("tsplib-eil51");
        assertTrue(problem instanceof EuclideanProblem);
        assertEquals(51, problem.getSize());
    }

    @Test
    public void tsplibUnknownNameThrows() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
            () -> ProblemFactory.createProblem("tsplib-doesnotexist"));
        assertTrue(e.getMessage().contains("doesnotexist"));
    }
}
