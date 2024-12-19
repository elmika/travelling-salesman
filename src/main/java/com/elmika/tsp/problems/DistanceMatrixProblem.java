package com.elmika.tsp.problems;

import com.ValueObjects.Point;

public class DistanceMatrixProblem implements Problem {
    private double[][] distanceMatrix;

    public DistanceMatrixProblem(double[][] distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
    }

    @Override
    public double getDistance(int A, int B) {
        return this.distanceMatrix[A - 1][B - 1];
    }

    @Override
    public int getSize() {
        return this.distanceMatrix.length;
    }

    @Override
    public Point getPoint(int i) {
        return null;
    }
}