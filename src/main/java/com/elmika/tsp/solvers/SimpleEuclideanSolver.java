package com.elmika.tsp.solvers;

import com.ValueObjects.Point;
import com.elmika.tsp.problems.Problem;

public class SimpleEuclideanSolver extends SimpleSolver {
    
    public SimpleEuclideanSolver(Problem problem) {        
        super(problem);
    }

    public Point[] getSolutionCoordinates(Integer[] sol) {      

        Point[] solutionCoordinates = new Point[sol.length];;
        for (int i = 0; i < sol.length; i++) {
            solutionCoordinates[i] = this.getProblem().getPoint(sol[i]);
        }

        return solutionCoordinates;
    }
}
