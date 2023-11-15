package com.elmika.tsp;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SimpleSolver {
    private double[][] distanceMatrix;

    public SimpleSolver(double[][] distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
    }

    public List<Integer> solveTSP() {
        // Implementation of TSP solving algorithm
        // Return the optimal path as a List of Integer
        return null;
    }

    
    /*
     * Random 5 point solution
    */
    public static int[] findSolution(double[][] problem) {
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

