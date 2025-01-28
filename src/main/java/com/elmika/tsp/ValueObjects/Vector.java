package com.ValueObjects;

/*
 * isColinear courtesy of https://github.com/pgkelley4/line-segments-intersect/blob/master/js/test-line-segments-intersect.js
 * 
*/
public class Vector {
    double x,y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double crossProduct(Vector W) {
        return this.getX() * W.getY() - W.getX() * this.getY();        
    }

    public boolean isColinear(Vector W) {
        return this.crossProduct(W) == 0;
    }
}
