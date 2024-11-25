package com.elmika.tsp;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EuclideanProblem implements Problem {
    
    private double[][] points;

    public EuclideanProblem(double[][] points) {
        if (!this.isValid2DPointArray(points)) {
            throw new InvalidParameterException("Euclidean problems needs to be initialized with an array of Euclidean coordinates.");
        }
        this.points = this.removeDuplicates(points);
    }

    private boolean isValid2DPointArray(double[][] array) {
        if (array == null) {
            return false; // The array itself is null
        }

        for (double[] point : array) {
            if (point == null || point.length != 2) {
                return false; // Sub-array is null or does not have exactly 2 elements
            }
        }

        return true; // All checks passed
    }

    private double[][] removeDuplicates(double[][] points) {
        Set<String> uniquePoints = new HashSet<>();

        return Arrays.stream(points)
            .filter(point -> uniquePoints.add(Arrays.toString(point))) // Filters out duplicates
            .toArray(double[][]::new);
    }

    @Override
    public double getDistance(int A, int B) {
        double x1, y1, x2, y2;
        x1 = this.points[A - 1][0];
        y1 = this.points[A - 1][1];
        x2 = this.points[B - 1][0];
        y2 = this.points[B - 1][1];

        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    @Override
    public int getSize() {
        return this.points.length;
    }
}