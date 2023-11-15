package com.elmika.tsp;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SimpleSolver {
    
    // private double[][] distanceMatrix;
    private int iterationIndex = 0;

    public SimpleSolver(double[][] distanceMatrix) {
        //this.distanceMatrix = distanceMatrix;
    }

    public List<Integer> solveTSP() {
        // Implementation of TSP solving algorithm
        // Return the optimal path as a List of Integer
        return null;
    }
    
/*
     * Best random 5 point solution of 10 attempts
    */
    public int[] findSolution(double[][] problem) {
       
        // return findBestRandomSolution(problem);
        return findBestSolution(problem);
    }

    /*
     * Best random 5 point solution of 10 attempts
    */
    public int[] findBestRandomSolution(double[][] problem) {
       
        int[] solution = findRandomSolution(problem);
        double distance = getTotalDistance(solution, problem);

        for (int i = 0; i < 10; i++) {
            int[] newSolution = findRandomSolution(problem);
            double newDistance = getTotalDistance(newSolution, problem);
            if( newDistance < distance){
                solution = newSolution;
                distance = newDistance;
            }
        }

        return solution;
    }

    /*
     * Best solution with brute force.
    */
    public int[] findBestSolution(double[][] problem) {
       
        int[] solution = findNextIteration();
        double distance = getTotalDistance(solution, problem);

        int[] newSolution = findNextIteration();
        while(newSolution.length != 0) {
            double newDistance = getTotalDistance(newSolution, problem);
            if( newDistance < distance){
                solution = newSolution;
                distance = newDistance;
            }
            newSolution = findNextIteration();
        }

        return solution;
    }

    /*
     * Random 5 point solution
    */
    private static int[] findRandomSolution(double[][] problem) {
        Integer[] solution =  {1,  2,  3,  5,  4};

		List<Integer> intList = Arrays.asList(solution);

		Collections.shuffle(intList);

        int[] intSolution = new int[intList.size()];
        for (int i = 0; i < intList.size(); i++) {
            intSolution[i] = intList.get(i).intValue();
        }

        return intSolution;
    }

    /*
     * Iterate over all possible path for 5 points.
    */
    private int[] findNextIteration() {
        
        int[][] solutions = {
             {1, 2, 3, 4, 5 }, {1, 2, 3, 5, 4 }, {1, 2, 4, 3, 5 }, {1, 2, 4, 5, 3 }, {1, 2, 5, 3, 4 }, {1, 2, 5, 4, 3 }, {1, 3, 2, 4, 5 }, {1, 3, 2, 5, 4 }, {1, 3, 4, 2, 5 }, {1, 3, 4, 5, 2 }, {1, 3, 5, 2, 4 }, {1, 3, 5, 4, 2 }, {1, 4, 2, 3, 5 }, {1, 4, 2, 5, 3 }, {1, 4, 3, 2, 5 }, {1, 4, 3, 5, 2 }, {1, 4, 5, 2, 3 }, {1, 4, 5, 3, 2 }, {1, 5, 2, 3, 4 }, {1, 5, 2, 4, 3 }, {1, 5, 3, 2, 4 }, {1, 5, 3, 4, 2 }, {1, 5, 4, 2, 3 }, {1, 5, 4, 3, 2 }, {2, 1, 3, 4, 5 }, {2, 1, 3, 5, 4 }, {2, 1, 4, 3, 5 }, {2, 1, 4, 5, 3 }, {2, 1, 5, 3, 4 }, {2, 1, 5, 4, 3 }, {2, 3, 1, 4, 5 }, {2, 3, 1, 5, 4 }, {2, 3, 4, 1, 5 }, {2, 3, 4, 5, 1 }, {2, 3, 5, 1, 4 }, {2, 3, 5, 4, 1 }, {2, 4, 1, 3, 5 }, {2, 4, 1, 5, 3 }, {2, 4, 3, 1, 5 }, {2, 4, 3, 5, 1 }, {2, 4, 5, 1, 3 }, {2, 4, 5, 3, 1 }, {2, 5, 1, 3, 4 }, {2, 5, 1, 4, 3 }, {2, 5, 3, 1, 4 }, {2, 5, 3, 4, 1 }, {2, 5, 4, 1, 3 }, {2, 5, 4, 3, 1 }, {3, 1, 2, 4, 5 }, {3, 1, 2, 5, 4 }, {3, 1, 4, 2, 5 }, {3, 1, 4, 5, 2 }, {3, 1, 5, 2, 4 }, {3, 1, 5, 4, 2 }, {3, 2, 1, 4, 5 }, {3, 2, 1, 5, 4 }, {3, 2, 4, 1, 5 }, {3, 2, 4, 5, 1 }, {3, 2, 5, 1, 4 }, {3, 2, 5, 4, 1 }, {3, 4, 1, 2, 5 }, {3, 4, 1, 5, 2 }, {3, 4, 2, 1, 5 }, {3, 4, 2, 5, 1 }, {3, 4, 5, 1, 2 }, {3, 4, 5, 2, 1 }, {3, 5, 1, 2, 4 }, {3, 5, 1, 4, 2 }, {3, 5, 2, 1, 4 }, {3, 5, 2, 4, 1 }, {3, 5, 4, 1, 2 }, {3, 5, 4, 2, 1 }, {4, 1, 2, 3, 5 }, {4, 1, 2, 5, 3 }, {4, 1, 3, 2, 5 }, {4, 1, 3, 5, 2 }, {4, 1, 5, 2, 3 }, {4, 1, 5, 3, 2 }, {4, 2, 1, 3, 5 }, {4, 2, 1, 5, 3 }, {4, 2, 3, 1, 5 }, {4, 2, 3, 5, 1 }, {4, 2, 5, 1, 3 }, {4, 2, 5, 3, 1 }, {4, 3, 1, 2, 5 }, {4, 3, 1, 5, 2 }, {4, 3, 2, 1, 5 }, {4, 3, 2, 5, 1 }, {4, 3, 5, 1, 2 }, {4, 3, 5, 2, 1 }, {4, 5, 1, 2, 3 }, {4, 5, 1, 3, 2 }, {4, 5, 2, 1, 3 }, {4, 5, 2, 3, 1 }, {4, 5, 3, 1, 2 }, {4, 5, 3, 2, 1 }, {5, 1, 2, 3, 4 }, {5, 1, 2, 4, 3 }, {5, 1, 3, 2, 4 }, {5, 1, 3, 4, 2 }, {5, 1, 4, 2, 3 }, {5, 1, 4, 3, 2 }, {5, 2, 1, 3, 4 }, {5, 2, 1, 4, 3 }, {5, 2, 3, 1, 4 }, {5, 2, 3, 4, 1 }, {5, 2, 4, 1, 3 }, {5, 2, 4, 3, 1 }, {5, 3, 1, 2, 4 }, {5, 3, 1, 4, 2 }, {5, 3, 2, 1, 4 }, {5, 3, 2, 4, 1 }, {5, 3, 4, 1, 2 }, {5, 3, 4, 2, 1 }, {5, 4, 1, 2, 3 }, {5, 4, 1, 3, 2 }, {5, 4, 2, 1, 3 }, {5, 4, 2, 3, 1 }, {5, 4, 3, 1, 2 }, {5, 4, 3, 2, 1 }
        };

        if(iterationIndex == 120) {
            return new int[0];
        }

        iterationIndex++;
        return solutions[iterationIndex-1];
    }

    /*
     * Problem of 5 cities, set up with distance matrix
    */
    private static double getDistance(int A, int B, double[][] problem) {
        double distance = problem[A-1][B-1];            
        return distance;
    }

    /*
     * Problem of 5 cities, set up with distance matrix
    */
    public static double getTotalDistance(int[] sol, double[][] pb) {
        
        return      getDistance(sol[0], sol[1], pb)
                            + getDistance(sol[1], sol[2], pb)
                            + getDistance(sol[2], sol[3], pb)
                            + getDistance(sol[3], sol[4], pb)
                            + getDistance(sol[4], sol[0], pb);
    }

}

