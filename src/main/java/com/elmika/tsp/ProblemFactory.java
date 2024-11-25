package com.elmika.tsp;

public class ProblemFactory {

    public static Problem createProblem(String problemType) {
        
        Problem problem;

        // Constants for problem types
        final String TRIVIAL = "trivial";
        final String SIMPLE = "simple";
        final String BIGGER = "bigger";
        final String EUCLIDEAN = "euclidean";

        switch(problemType){
            case TRIVIAL: problem = ProblemFactory.createSimplestProblem(); break;
            case SIMPLE: problem = ProblemFactory.createSimpleProblem(); break;
            case BIGGER: problem = ProblemFactory.createBiggerProblem(); break;
            case EUCLIDEAN: problem = ProblemFactory.createEuclideanProblem(); break;
            default: problem = ProblemFactory.createSimplestProblem(); break;
        }

        return problem;
    }

    /*
     * Problem of 5 cities, set up with distance matrix
    */
    private static Problem createSimpleProblem() {
        double[][] distances =  {
            {0.0,  3.0,  4.0,  2.0,  7.0},
            {3.0,  0.0,  4.0,  6.0,  3.0},
            {4.0,  4.0,  0.0,  5.0,  8.0},
            {2.0,  6.0,  5.0,  0.0,  6.0},
            {7.0,  3.0,  8.0,  6.0,  0.0}
        };
        return new DistanceMatrixProblem(distances);
    }

    /*
     * Problem of 4 cities, set up with distance matrix
    */
    private static Problem createSimplestProblem() {
        double[][] distances =  {
            {0.0,  4.0,  6.0,  3.0},
            {4.0,  0.0,  5.0,  8.0},
            {6.0,  5.0,  0.0,  6.0},
            {3.0,  8.0,  6.0,  0.0}
        };
        return new DistanceMatrixProblem(distances);
    }

    /*
     * Non Euclidean problem of 6 cities, set up with distance matrix
    */
    private static Problem createBiggerProblem() {
        double[][] distances =  {
            {0.0,  2.0,  7.0, 3.0,  4.0, 5.0},
            {2.0, 0.0,  3.0,  4.0,  2.0,  7.0},
            {7.0, 3.0,  0.0,  4.0,  6.0,  3.0},
            {3.0, 4.0,  4.0,  0.0,  5.0,  8.0},
            {4.0, 2.0,  6.0,  5.0,  0.0,  6.0},
            {5.0, 7.0,  3.0,  8.0,  6.0,  0.0}
        };
        return new DistanceMatrixProblem(distances);
    }

    /*
     * Euclidean problem, 7 cities
    */
    private static Problem createEuclideanProblem() {
        double[][] points =  {
            {0.0,  2.0},
            {2.0, 0.0},
            {7.0, 3.0},
            {3.0, 4.0},
            {4.0, 2.0},
            {5.0, 7.0},
            {1.0, 7.0}
        };
        return new EuclideanProblem(points);
    }

}
