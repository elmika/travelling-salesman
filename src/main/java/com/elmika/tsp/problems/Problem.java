package com.elmika.tsp.problems;

import com.elmika.tsp.ValueObjects.Point;

public interface Problem {
    double getDistance(int A, int B);
    int getSize();
    Point getPoint(int i);
}