package com.elmika.tsp;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SimpleSolver {
    
    private double[][] distanceMatrix;
    private PermutationsIterator iterator;

    public SimpleSolver(double[][] distanceMatrix) {
        this.distanceMatrix = distanceMatrix;        
    }

    private PermutationsIterator getPermutationsIterator(){
        if(this.iterator == null) {
            Integer problemSize = this.distanceMatrix.length;        
            this.iterator = new PermutationsIterator(problemSize);
        }
        return this.iterator;
    }

    /*
     * Call relevant internal function to solve our problem.
    */
    public Integer[] findSolution(String type) {
       
        Integer[] sol;
        switch(type){
            case "brute-force":
                System.out.println("Using Brute Force algorithm to find the best route.");
                sol = findBestSolution(); 
            break;
            case "random":
                System.out.println("Finding one random solution.");
                sol = findRandomSolution();
            break;
            case "random10":
                System.out.println("Comparing 10 random solutions to find the best route.");
                sol = findBestRandomSolution(10);
            break;
            case "random100":
                System.out.println("Comparing 100 random solutions to find the best route.");
                sol = findBestRandomSolution(100);
            break;
            default:
                System.out.println("Comparing 10 random solutions to find the best route.");
                sol = findBestRandomSolution(10);
        }

        return sol;
    }

    /*
     * Best random 5 point solution of 10 attempts
    */
    private Integer[] findBestRandomSolution(Integer iterations) {
       
        Integer[] solution = this.findRandomSolution();
        double distance = this.getTotalDistance(solution);

        for (Integer i = 0; i < iterations; i++) {
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
        if(solution.length == 0) {
            return solution;
        }

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
     * Iterate over all possible path... Starting from point 1!
    */
    private Integer[] findNextIteration() {
        
        iterator = getPermutationsIterator();

        if( ! iterator.hasNext()) {
            Integer[] empty = {};
            return empty;
        }
        return iterator.next();
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
             
        if(sol.length == 0) {
            return 0;
        }

        double totalDistance = this.getDistance(sol[sol.length-1], sol[0]);
        
        for(Integer i = 1; i < sol.length; i++){
            totalDistance += this.getDistance(sol[i-1], sol[i]);
        }

        return totalDistance;        
    }

}

