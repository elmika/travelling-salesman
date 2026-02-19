package com.elmika.tsp.adapter.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.elmika.tsp.domain.DistanceMatrixProblem;
import com.elmika.tsp.domain.EuclideanProblem;
import com.elmika.tsp.domain.Problem;
import com.elmika.tsp.domain.Solution;

public class RouteCoordinatesMapperTest {

    @Test
    public void mapsEuclideanProblemAndSolutionToCoordinates() {
        double[][] points = {
            {0.0, 0.0},
            {1.0, 0.0},
            {1.0, 1.0}
        };
        Problem problem = new EuclideanProblem(points);
        Solution solution = new Solution(new Integer[] {1, 3, 2}, 0.0);

        RouteCoordinatesView view = RouteCoordinatesMapper.toView(problem, solution);
        List<RouteCoordinatesView.Coordinate> coords = view.getCoordinates();

        assertEquals(3, coords.size());
        assertEquals(0.0, coords.get(0).getX());
        assertEquals(0.0, coords.get(0).getY());
        assertEquals(1.0, coords.get(1).getX());
        assertEquals(1.0, coords.get(1).getY());
        assertEquals(1.0, coords.get(2).getX());
        assertEquals(0.0, coords.get(2).getY());
    }

    @Test
    public void nonEuclideanProblemIsRejected() {
        double[][] distanceMatrix = {
            {0, 1},
            {1, 0}
        };
        Problem problem = new DistanceMatrixProblem(distanceMatrix);
        Solution solution = new Solution(new Integer[] {1, 2}, 0.0);

        assertThrows(IllegalArgumentException.class,
            () -> RouteCoordinatesMapper.toView(problem, solution));
    }
}
