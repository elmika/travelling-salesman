package com.elmika.tsp;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SimpleSolver {
    
    private double[][] distanceMatrix;
    private int iterationIndex = 0;

    public SimpleSolver(double[][] distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
    }

    public List<Integer> solveTSP() {
        // Implementation of TSP solving algorithm
        // Return the optimal path as a List of Integer
        return null;
    }
    
    /*
     * Call relevant internal function to solve our problem.
    */
    public int[] findSolution() {
       
        // return findBestRandomSolution();
        return findBestSolution();
    }

    /*
     * Best random 5 point solution of 10 attempts
    */
    private int[] findBestRandomSolution() {
       
        int[] solution = this.findRandomSolution();
        double distance = this.getTotalDistance(solution);

        for (int i = 0; i < 10; i++) {
            int[] newSolution = findRandomSolution();
            double newDistance = this.getTotalDistance(newSolution);
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
    public int[] findBestSolution() {
       
        int[] solution = findNextIteration();
        double distance = getTotalDistance(solution);

        int[] newSolution = findNextIteration();
        while(newSolution.length != 0) {
            double newDistance = getTotalDistance(newSolution);
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
    private int[] findRandomSolution() {
        
        Integer[] solution =  new Integer[this.distanceMatrix.length];
        for(int i = 0; i < this.distanceMatrix.length; i++) {
            solution[i]= i+1;
        }

		List<Integer> intList = Arrays.asList(solution);

		Collections.shuffle(intList);

        int[] intSolution = new int[intList.size()];
        for (int i = 0; i < intList.size(); i++) {
            intSolution[i] = intList.get(i).intValue();
        }

        return intSolution;
    }

    /*
     * Iterate over all possible path for 5 points... Starting from point 1!
    */
    private int[] findNextIteration() {
        
        // Still works for asymetric problem!
        int[][] solutions = {
             {1, 2, 3, 4, 5 }, {1, 2, 3, 5, 4 }, {1, 2, 4, 3, 5 }, {1, 2, 4, 5, 3 }, {1, 2, 5, 3, 4 }, 
             {1, 2, 5, 4, 3 }, {1, 3, 2, 4, 5 }, {1, 3, 2, 5, 4 }, {1, 3, 4, 2, 5 }, {1, 3, 4, 5, 2 }, 
             {1, 3, 5, 2, 4 }, {1, 3, 5, 4, 2 }, {1, 4, 2, 3, 5 }, {1, 4, 2, 5, 3 }, {1, 4, 3, 2, 5 }, 
             {1, 4, 3, 5, 2 }, {1, 4, 5, 2, 3 }, {1, 4, 5, 3, 2 }, {1, 5, 2, 3, 4 }, {1, 5, 2, 4, 3 }, 
             {1, 5, 3, 2, 4 }, {1, 5, 3, 4, 2 }, {1, 5, 4, 2, 3 }, {1, 5, 4, 3, 2 }
        };

        if(iterationIndex == solutions.length) {
            return new int[0];
        }

        iterationIndex++;
        return solutions[iterationIndex-1];
    }

    /*
     * Problem of 5 cities, set up with distance matrix
    */
    private double getDistance(int A, int B) {
                   
        return this.distanceMatrix[A-1][B-1];        
    }

    /*
     * Problem of 5 cities, set up with distance matrix
    */
    public double getTotalDistance(int[] sol) {
                
        double totalDistance = this.getDistance(sol[sol.length-1], sol[0]);
        
        for(int i = 1; i < sol.length; i++){
            totalDistance += this.getDistance(sol[i-1], sol[i]);
        }

        return totalDistance;        
    }

}

