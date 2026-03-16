package com.elmika.tsp.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.elmika.tsp.domain.problem.EuclideanProblem;
import com.elmika.tsp.domain.problem.Problem;

public class TspLibParserTest {

    @Test
    public void loadBerlin52ReturnsCorrectSize() {
        Problem p = TspLibParser.load("berlin52");
        assertEquals(52, p.getSize());
    }

    @Test
    public void loadBerlin52ReturnsEuclideanProblem() {
        Problem p = TspLibParser.load("berlin52");
        assertTrue(p instanceof EuclideanProblem);
    }

    @Test
    public void loadBerlin52HasCorrectFirstPoint() {
        // berlin52: city 1 = 565.0 575.0
        EuclideanProblem p = (EuclideanProblem) TspLibParser.load("berlin52");
        assertEquals(565.0, p.getPoint(1).getX(), 1e-6);
        assertEquals(575.0, p.getPoint(1).getY(), 1e-6);
    }

    @Test
    public void loadEil51ReturnsCorrectSize() {
        Problem p = TspLibParser.load("eil51");
        assertEquals(51, p.getSize());
    }

    @Test
    public void loadEil51HasCorrectFirstPoint() {
        // eil51: city 1 = 37 52
        EuclideanProblem p = (EuclideanProblem) TspLibParser.load("eil51");
        assertEquals(37.0, p.getPoint(1).getX(), 1e-6);
        assertEquals(52.0, p.getPoint(1).getY(), 1e-6);
    }

    @Test
    public void loadKroA100ReturnsCorrectSize() {
        Problem p = TspLibParser.load("kroA100");
        assertEquals(100, p.getSize());
    }

    @Test
    public void loadUnknownNameThrows() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
            () -> TspLibParser.load("nonexistent"));
        assertTrue(e.getMessage().contains("nonexistent"));
    }
}
