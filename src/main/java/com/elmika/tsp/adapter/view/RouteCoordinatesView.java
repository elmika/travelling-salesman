package com.elmika.tsp.adapter.view;

import java.util.List;

/**
 * View model representing a TSP solution as an ordered list of coordinates.
 * Intended for use by visualization adapters (e.g., HTML canvas).
 */
public class RouteCoordinatesView {

    public static final class Coordinate {
        private final double x;
        private final double y;

        public Coordinate(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }

    private final List<Coordinate> coordinates;

    public RouteCoordinatesView(List<Coordinate> coordinates) {
        this.coordinates = List.copyOf(coordinates);
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }
}

