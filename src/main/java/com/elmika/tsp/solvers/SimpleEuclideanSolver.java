package com.elmika.tsp.solvers;

import com.elmika.tsp.problems.Problem;

public class SimpleEuclideanSolver extends SimpleSolver {
    
    public SimpleEuclideanSolver(Problem problem) {        
        super(problem);
    }

    public double[][] getSolutionCoordinates(Integer[] sol) {      

        double[][] solutionCoordinates = new double[sol.length][2];;
        for (int i = 0; i < sol.length; i++) {
            solutionCoordinates[i] = this.getProblem().getPoint(sol[i]);
        }

        return solutionCoordinates;
    }
}
