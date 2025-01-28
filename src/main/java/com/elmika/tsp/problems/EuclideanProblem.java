package com.elmika.tsp.problems;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.elmika.tsp.ValueObjects.Point;

public class EuclideanProblem implements Problem {
    
    private Point[] points;

    public EuclideanProblem(Point[] points) {
        
        this.points = this.removeDuplicates(points);
    }

    /** We use polymorphism to maintain compatibitily with previous version. */
    public EuclideanProblem(double[][] coordinates) {
        if (!this.isValid2DPointArray(coordinates)) {
            throw new InvalidParameterException("Euclidean problems instanciation expects array of Euclidean coordinates.");
        }
        coordinates = this.removeDuplicates(coordinates);
       
        Point[] points = Arrays.stream(coordinates)
            .map(coord -> new Point(coord[0], coord[1]))
            .toArray(Point[]::new);

        this.points = points;
    }

    @Override
    public Point getPoint(int index) {
        validatePointIndex(index);
        return this.points[index-1];
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

    private Point[] removeDuplicates(Point[] points) {
        
        // Convert to Set to remove duplicates
        Set<Point> uniquePoints = new HashSet<>(Arrays.asList(points));

        // Convert back to array if needed
        Point[] result = uniquePoints.toArray(new Point[0]);

        return result;
    }

    private void validatePointIndex(int index) {

        if(index <= 0) {
            throw new InvalidParameterException("Point index out of range.");
        }
        if(index > getSize() ) {
            throw new InvalidParameterException("Point index out of range.");
        }
        return;
    }

    @Override
    public double getDistance(int A, int B) {
        double x1, y1, x2, y2;

        validatePointIndex(A);
        validatePointIndex(B);

        Point pointA = this.points[A - 1];
        Point pointB = this.points[B - 1];

        x1 = pointA.getX();
        y1 = pointA.getY();
        x2 = pointB.getX();
        y2 = pointB.getY();

        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    @Override
    public int getSize() {
        return this.points.length;
    }
}