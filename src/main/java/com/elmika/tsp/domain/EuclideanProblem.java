package com.elmika.tsp.domain;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EuclideanProblem implements Problem {

    private double[][] points;

    public EuclideanProblem(double[][] points) {
        if (!this.isValid2DPointArray(points)) {
            throw new IllegalArgumentException("Euclidean problems instanciation expects array of Euclidean coordinates.");
        }
        this.points = this.removeDuplicates(points);
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

    private double[][] removeDuplicates(double[][] points) {
        Set<String> uniquePoints = new HashSet<>();
        return Arrays.stream(points)
            .filter(point -> uniquePoints.add(Arrays.toString(point)))
            .toArray(double[][]::new);
    }

    @Override
    public double getDistance(int A, int B) {
        if (A > getSize() || B > getSize() || A <= 0 || B <= 0) {
            throw new IllegalArgumentException("Point index out of range for distance calculation.");
        }
        double x1 = this.points[A - 1][0];
        double y1 = this.points[A - 1][1];
        double x2 = this.points[B - 1][0];
        double y2 = this.points[B - 1][1];
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    @Override
    public int getSize() {
        return this.points.length;
    }
}
