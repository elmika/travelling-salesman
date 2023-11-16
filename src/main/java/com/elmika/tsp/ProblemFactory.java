package com.elmika.tsp;

public class ProblemFactory {

    /*
     * Problem of 5 cities, set up with distance matrix
    */
    public static double[][] createSimpleProblem() {
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
     * Problem of 4 cities, set up with distance matrix
    */
    public static double[][] createSimplestProblem() {
        double[][] distances =  {
            {0.0,  4.0,  6.0,  3.0},
            {4.0,  0.0,  5.0,  8.0},
            {6.0,  5.0,  0.0,  6.0},
            {3.0,  8.0,  6.0,  0.0}
        };
        return distances;
    }

    /*
     * Non Euclidean problem of 6 cities, set up with distance matrix
    */
    public static double[][] createBiggerProblem() {
        double[][] distances =  {
            {0.0,  2.0,  7.0, 3.0,  4.0, 5.0},
            {2.0, 0.0,  3.0,  4.0,  2.0,  7.0},
            {7.0, 3.0,  0.0,  4.0,  6.0,  3.0},
            {3.0, 4.0,  4.0,  0.0,  5.0,  8.0},
            {4.0, 2.0,  6.0,  5.0,  0.0,  6.0},
            {5.0, 7.0,  3.0,  8.0,  6.0,  0.0}
        };
        return distances;
    }

}
