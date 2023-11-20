package com.elmika.tsp;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SimpleSolver {
    
    private double[][] distanceMatrix;
    private Integer iterationIndex = 0;

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
    public Integer[] findSolution() {
       
        // return findBestRandomSolution();
        return findBestSolution();
    }

    /*
     * Best random 5 point solution of 10 attempts
    */
    private Integer[] findBestRandomSolution() {
       
        Integer[] solution = this.findRandomSolution();
        double distance = this.getTotalDistance(solution);

        for (Integer i = 0; i < 10; i++) {
            Integer[] newSolution = findRandomSolution();
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
    public Integer[] findBestSolution() {
       
        Integer[] solution = findNextIteration();
        double distance = getTotalDistance(solution);

        Integer[] newSolution = findNextIteration();
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
     * Random solution
    */
    private Integer[] findRandomSolution() {
        
        Integer[] solution =  new Integer[this.distanceMatrix.length];
        for(Integer i = 0; i < this.distanceMatrix.length; i++) {
            solution[i]= i+1;
        }

		List<Integer> intList = Arrays.asList(solution);

		Collections.shuffle(intList);

        Integer[] intSolution = new Integer[intList.size()];
        for (Integer i = 0; i < intList.size(); i++) {
            intSolution[i] = intList.get(i).intValue();
        }

        return intSolution;
    }

    /*
     * Iterate over all possible path for 4 to 6 points... Starting from point 1!
    */
    private Integer[] findNextIteration() {
        
        Integer problemSize = this.distanceMatrix.length;

        if(problemSize <= 3 || problemSize >6){
            Integer[] empty = {};

            return empty;
        }
        Integer combinations;
        switch(problemSize) {
            case 4: combinations = 6; break;
            case 5: combinations = 24; break;
            case 6: combinations = 120; break;
            default: combinations = 0;                          
        }

        Integer[][] solutions = new Integer[combinations][problemSize];
        // Still works for asymetric problem!
        switch(problemSize) {
            case 4:                
                Integer[][] solutions4 = {
                    {1, 2, 3, 4}, {1, 2, 4, 3}, 
                    {1, 3, 2, 4}, {1, 3, 4, 2},
                    {1, 4, 2, 3}, {1, 4, 3, 2}
                };
                solutions = solutions4;
            break;
            case 5:
                Integer[][] solutions5 = {
                    {1, 2, 3, 4, 5 }, {1, 2, 3, 5, 4 }, {1, 2, 4, 3, 5 }, {1, 2, 4, 5, 3 }, {1, 2, 5, 3, 4 }, 
                    {1, 2, 5, 4, 3 }, {1, 3, 2, 4, 5 }, {1, 3, 2, 5, 4 }, {1, 3, 4, 2, 5 }, {1, 3, 4, 5, 2 }, 
                    {1, 3, 5, 2, 4 }, {1, 3, 5, 4, 2 }, {1, 4, 2, 3, 5 }, {1, 4, 2, 5, 3 }, {1, 4, 3, 2, 5 }, 
                    {1, 4, 3, 5, 2 }, {1, 4, 5, 2, 3 }, {1, 4, 5, 3, 2 }, {1, 5, 2, 3, 4 }, {1, 5, 2, 4, 3 }, 
                    {1, 5, 3, 2, 4 }, {1, 5, 3, 4, 2 }, {1, 5, 4, 2, 3 }, {1, 5, 4, 3, 2 }
                };
                solutions = solutions5;
            break;
            case 6:                
                Integer[][] solutions6 = {
                    {1, 2, 3, 4, 5, 6}, {1, 2, 3, 5, 4, 6}, {1, 2, 4, 3, 5, 6}, {1, 2, 4, 5, 3, 6}, {1, 2, 5, 3, 4, 6}, {1, 2, 5, 4, 3, 6}, {1, 3, 2, 4, 5, 6}, {1, 3, 2, 5, 4, 6}, {1, 3, 4, 2, 5, 6}, {1, 3, 4, 5, 2, 6}, {1, 3, 5, 2, 4, 6}, {1, 3, 5, 4, 2, 6}, {1, 4, 2, 3, 5, 6}, {1, 4, 2, 5, 3, 6}, {1, 4, 3, 2, 5, 6}, {1, 4, 3, 5, 2, 6}, {1, 4, 5, 2, 3, 6}, {1, 4, 5, 3, 2, 6}, {1, 5, 2, 3, 4, 6}, {1, 5, 2, 4, 3, 6}, {1, 5, 3, 2, 4, 6}, {1, 5, 3, 4, 2, 6}, {1, 5, 4, 2, 3, 6}, {1, 5, 4, 3, 2, 6}, 
                    {2, 1, 3, 4, 5, 6}, {2, 1, 3, 5, 4, 6}, {2, 1, 4, 3, 5, 6}, {2, 1, 4, 5, 3, 6}, {2, 1, 5, 3, 4, 6}, {2, 1, 5, 4, 3, 6}, {2, 3, 1, 4, 5, 6}, {2, 3, 1, 5, 4, 6}, {2, 3, 4, 1, 5, 6}, {2, 3, 4, 5, 1, 6}, {2, 3, 5, 1, 4, 6}, {2, 3, 5, 4, 1, 6}, {2, 4, 1, 3, 5, 6}, {2, 4, 1, 5, 3, 6}, {2, 4, 3, 1, 5, 6}, {2, 4, 3, 5, 1, 6}, {2, 4, 5, 1, 3, 6}, {2, 4, 5, 3, 1, 6}, {2, 5, 1, 3, 4, 6}, {2, 5, 1, 4, 3, 6}, {2, 5, 3, 1, 4, 6}, {2, 5, 3, 4, 1, 6}, {2, 5, 4, 1, 3, 6}, {2, 5, 4, 3, 1, 6}, 
                    {3, 1, 2, 4, 5, 6}, {3, 1, 2, 5, 4, 6}, {3, 1, 4, 2, 5, 6}, {3, 1, 4, 5, 2, 6}, {3, 1, 5, 2, 4, 6}, {3, 1, 5, 4, 2, 6}, {3, 2, 1, 4, 5, 6}, {3, 2, 1, 5, 4, 6}, {3, 2, 4, 1, 5, 6}, {3, 2, 4, 5, 1, 6}, {3, 2, 5, 1, 4, 6}, {3, 2, 5, 4, 1, 6}, {3, 4, 1, 2, 5, 6}, {3, 4, 1, 5, 2, 6}, {3, 4, 2, 1, 5, 6}, {3, 4, 2, 5, 1, 6}, {3, 4, 5, 1, 2, 6}, {3, 4, 5, 2, 1, 6}, {3, 5, 1, 2, 4, 6}, {3, 5, 1, 4, 2, 6}, {3, 5, 2, 1, 4, 6}, {3, 5, 2, 4, 1, 6}, {3, 5, 4, 1, 2, 6}, {3, 5, 4, 2, 1, 6}, 
                    {4, 1, 2, 3, 5, 6}, {4, 1, 2, 5, 3, 6}, {4, 1, 3, 2, 5, 6}, {4, 1, 3, 5, 2, 6}, {4, 1, 5, 2, 3, 6}, {4, 1, 5, 3, 2, 6}, {4, 2, 1, 3, 5, 6}, {4, 2, 1, 5, 3, 6}, {4, 2, 3, 1, 5, 6}, {4, 2, 3, 5, 1, 6}, {4, 2, 5, 1, 3, 6}, {4, 2, 5, 3, 1, 6}, {4, 3, 1, 2, 5, 6}, {4, 3, 1, 5, 2, 6}, {4, 3, 2, 1, 5, 6}, {4, 3, 2, 5, 1, 6}, {4, 3, 5, 1, 2, 6}, {4, 3, 5, 2, 1, 6}, {4, 5, 1, 2, 3, 6}, {4, 5, 1, 3, 2, 6}, {4, 5, 2, 1, 3, 6}, {4, 5, 2, 3, 1, 6}, {4, 5, 3, 1, 2, 6}, {4, 5, 3, 2, 1, 6}, 
                    {5, 1, 2, 3, 4, 6}, {5, 1, 2, 4, 3, 6}, {5, 1, 3, 2, 4, 6}, {5, 1, 3, 4, 2, 6}, {5, 1, 4, 2, 3, 6}, {5, 1, 4, 3, 2, 6}, {5, 2, 1, 3, 4, 6}, {5, 2, 1, 4, 3, 6}, {5, 2, 3, 1, 4, 6}, {5, 2, 3, 4, 1, 6}, {5, 2, 4, 1, 3, 6}, {5, 2, 4, 3, 1, 6}, {5, 3, 1, 2, 4, 6}, {5, 3, 1, 4, 2, 6}, {5, 3, 2, 1, 4, 6}, {5, 3, 2, 4, 1, 6}, {5, 3, 4, 1, 2, 6}, {5, 3, 4, 2, 1, 6}, {5, 4, 1, 2, 3, 6}, {5, 4, 1, 3, 2, 6}, {5, 4, 2, 1, 3, 6}, {5, 4, 2, 3, 1, 6}, {5, 4, 3, 1, 2, 6}, {5, 4, 3, 2, 1, 6}
                };
                solutions = solutions6;
            break;            
        }
        

        if(iterationIndex == solutions.length) {
            Integer[] empty = {};
            return empty;
        }

        iterationIndex++;
        return solutions[iterationIndex-1];
    }

    /*
     * Problem of 5 cities, set up with distance matrix
    */
    private double getDistance(Integer A, Integer B) {
                   
        return this.distanceMatrix[A-1][B-1];        
    }

    /*
     * Compute the distance of current solution
    */
    public double getTotalDistance(Integer[] sol) {
                
        double totalDistance = this.getDistance(sol[sol.length-1], sol[0]);
        
        for(Integer i = 1; i < sol.length; i++){
            totalDistance += this.getDistance(sol[i-1], sol[i]);
        }

        return totalDistance;        
    }

}

