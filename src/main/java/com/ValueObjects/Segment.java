package com.ValueObjects;

public class Segment {
    private Point A;
    private Point B;

    public Segment(Point A, Point B) {
        this.A = A;
        this.B = B;
    }

    public Point getA() {
        return this.A;
    }

    public Point getB() {
        return this.B;
    }

    public Vector getVector() {
        double x = this.B.getX() - this.A.getX();
        double y = this.B.getY() - this.A.getY();
        
        return new Vector(x, y);
    }

    // Orientation helper function
    private int orientation(Point p, Point q, Point r) {
        double val = (q.getX() - p.getX()) * (r.getY() - p.getY()) -
                     (q.getY() - p.getY()) * (r.getX() - p.getX());
        if (val == 0) return 0; // Collinear
        return (val > 0) ? 1 : 2; // Clockwise or Counterclockwise
    }

    // Check if point lies on segment
    private boolean onSegment(Point p, Point q, Point r) {
        return q.getX() >= Math.min(p.getX(), r.getX()) && q.getX() <= Math.max(p.getX(), r.getX()) &&
               q.getY() >= Math.min(p.getY(), r.getY()) && q.getY() <= Math.max(p.getY(), r.getY());
    }

    public boolean intersects(Segment CD) {
        Point C = CD.getA();
        Point D = CD.getB();

        // Find the four orientations
        int o1 = orientation(this.A, this.B, C);
        int o2 = orientation(this.A, this.B, D);
        int o3 = orientation(C, D, this.A);
        int o4 = orientation(C, D, this.B);

        // General case
        if (o1 != o2 && o3 != o4) return true;

        // Special cases: check collinear points
        // A, B, C are collinear and C lies on segment AB
        if (o1 == 0 && onSegment(this.A, C, this.B)) return true;
        // A, B, D are collinear and D lies on segment AB
        if (o2 == 0 && onSegment(this.A, D, this.B)) return true;
        // C, D, A are collinear and A lies on segment CD
        if (o3 == 0 && onSegment(C, this.A, D)) return true;
        // C, D, B are collinear and B lies on segment CD
        if (o4 == 0 && onSegment(C, this.B, D)) return true;

        // Otherwise, no intersection
        return false;
    }
}