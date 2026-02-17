package com.elmika.tsp.domain;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EuclideanProblem implements Problem {

    private final Point[] points;

    public EuclideanProblem(double[][] coordinates) {
        if (!this.isValid2DPointArray(coordinates)) {
            throw new IllegalArgumentException("Euclidean problems instanciation expects array of Euclidean coordinates.");
        }
        this.points = this.removeDuplicatesAndConvert(coordinates);
    }

    private boolean isValid2DPointArray(double[][] array) {
        if (array == null) {
            return false;
        }
        for (double[] point : array) {
            if (point == null || point.length != 2) {
                return false;
            }
        }
        return true;
    }

    private Point[] removeDuplicatesAndConvert(double[][] coordinates) {
        Set<Point> uniquePoints = new HashSet<>();
        return Arrays.stream(coordinates)
            .map(coords -> new Point(coords[0], coords[1]))
            .filter(uniquePoints::add)
            .toArray(Point[]::new);
    }

    private Point getPoint(int index) {
        return this.points[index];
    }

    @Override
    public double getDistance(int A, int B) {
        if (A > getSize() || B > getSize() || A <= 0 || B <= 0) {
            throw new IllegalArgumentException("Point index out of range for distance calculation.");
        }
        return getPoint(A - 1).distanceTo(getPoint(B - 1));
    }

    @Override
    public int getSize() {
        return this.points.length;
    }
}
