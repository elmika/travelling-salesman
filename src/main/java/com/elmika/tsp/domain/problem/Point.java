package com.elmika.tsp.domain.problem;

import java.util.Objects;

/**
 * Immutable value object representing a 2D point in Euclidean space.
 */
public final class Point {

    private final double x;
    private final double y;

    public Point(double x, double y) {
        if (Double.isNaN(x) || Double.isInfinite(x) || Double.isNaN(y) || Double.isInfinite(y)) {
            throw new IllegalArgumentException("Point coordinates must be finite numbers.");
        }
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /**
     * Euclidean distance from this point to another.
     *
     * @param other the other point (must not be null)
     * @return the distance
     */
    public double distanceTo(Point other) {
        if (other == null) {
            throw new IllegalArgumentException("Other point must not be null.");
        }
        double dx = other.x - this.x;
        double dy = other.y - this.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Point)) {
            return false;
        }
        Point other = (Point) o;
        return Double.compare(other.x, x) == 0 && Double.compare(other.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Point{" + "x=" + x + ", y=" + y + '}';
    }
}
