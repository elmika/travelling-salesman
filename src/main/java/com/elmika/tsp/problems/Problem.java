package com.elmika.tsp.problems;

public interface Problem {
    double getDistance(int A, int B);
    int getSize();
    double[] getPoint(int i);
}