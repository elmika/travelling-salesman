package com.ValueObjects;

import java.util.Objects;

public class Point {
    double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    private boolean hasX(double x) {
        return this.getX() == x;
    }

    private boolean hasY(double y) {
        return this.getY() == y;
    }

    private boolean hasCoordinates(double x, double y) {
        return this.hasX(x) && this.hasY(y);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Point B = (Point) obj;
        return this.hasCoordinates(B.getX(), B.getY());
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
