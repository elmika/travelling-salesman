package com.elmika.tsp;

public class ProblemFactory {

    public static Problem createProblem(String problemType) {
        
        Problem problem;

        switch(problemType){
            case "trivial": problem = ProblemFactory.createSimplestProblem(); break;
            case "simple": problem = ProblemFactory.createSimpleProblem(); break;
            case "bigger": problem = ProblemFactory.createBiggerProblem(); break;
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

}
