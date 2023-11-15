package com.elmika.tsp;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TravellingSalesman {

    public static void main(String[] args) {
        System.out.println("Traveling Salesman Problem Solver");

        double[][] problem = setupProblem();
        int[] solution = findSolution(problem);

        displaySolution(solution, problem);
    }

    // You can add methods for TSP solving logic

    /*
     * Problem of 5 cities, set up with distance matrix
    */
    private static double[][] setupProblem() {
        double[][] distances =  {
            {0.0,  3.0,  4.0,  2.0,  7.0},
            {3.0,  0.0,  4.0,  6.0,  3.0},
            {4.0,  4.0,  0.0,  5.0,  8.0},
            {2.0,  6.0,  5.0,  0.0,  6.0},
            {7.0,  3.0,  8.0,  6.0,  0.0}
        };
        return distances;
    }

    /*
     * Random 5 point solution
    */
    private static int[] findSolution(double[][] problem) {
        int[] solution =  
            // {5,  4,  3,  2,  1};
            {1,  2,  3,  5,  4};
        return solution;
    }

    /*
     * Problem of 5 cities, set up with distance matrix
    */
    private static double getDistance(int A, int B, double[][] problem) {
        double distance = problem[A-1][B-1];            
        return distance;
    }

    private static void displaySolution(int[] sol, double[][] pb){

            double totalDistance = getDistance(sol[0], sol[1], pb)
                                + getDistance(sol[1], sol[2], pb)
                                + getDistance(sol[2], sol[3], pb)
                                + getDistance(sol[3], sol[4], pb)
                                + getDistance(sol[4], sol[0], pb);

            System.out.println("Solution is:"+sol[0]+
                                "->"+sol[1]+
                                "->"+sol[2]+
                                "->"+sol[3]+
                                "->"+sol[4]
            );

            System.out.println("Distance is:"+totalDistance);
    }
}
