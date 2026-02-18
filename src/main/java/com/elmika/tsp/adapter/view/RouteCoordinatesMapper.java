package com.elmika.tsp.adapter.view;

import java.util.ArrayList;
import java.util.List;

import com.elmika.tsp.domain.EuclideanProblem;
import com.elmika.tsp.domain.Point;
import com.elmika.tsp.domain.Problem;
import com.elmika.tsp.domain.Solution;

/**
 * Maps a Problem + Solution to a RouteCoordinatesView when the problem is Euclidean.
 */
public final class RouteCoordinatesMapper {

    private RouteCoordinatesMapper() {
    }

    public static RouteCoordinatesView toView(Problem problem, Solution solution) {
        if (!(problem instanceof EuclideanProblem)) {
            throw new IllegalArgumentException("RouteCoordinatesView is only supported for EuclideanProblem.");
        }
        EuclideanProblem euclidean = (EuclideanProblem) problem;
        Integer[] route = solution.getRoute();
        List<RouteCoordinatesView.Coordinate> coords = new ArrayList<>(route.length);
        for (Integer city : route) {
            Point p = euclidean.getPoint(city);
            coords.add(new RouteCoordinatesView.Coordinate(p.getX(), p.getY()));
        }
        return new RouteCoordinatesView(coords);
    }
}

