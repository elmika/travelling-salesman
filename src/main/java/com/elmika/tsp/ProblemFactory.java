package com.elmika.tsp;

import java.util.Random;

public class ProblemFactory {

    public static Problem createProblem(String problemType) {
        
        Problem problem;

        // Constants for problem types
        final String TRIVIAL = "trivial";
        final String SIMPLE = "simple";
        final String BIGGER = "bigger";
        final String EUCLIDEAN = "euclidean";
        final String CITIES = "cities";
        final String FULLY_RANDOM = "fully-random";
        final String PARTIALLY_RANDOM = "partially-random";

        switch(coreProblemType(problemType)){
            case TRIVIAL: problem = ProblemFactory.createSimplestProblem(); break;
            case SIMPLE: problem = ProblemFactory.createSimpleProblem(); break;
            case BIGGER: problem = ProblemFactory.createBiggerProblem(); break;
            case EUCLIDEAN: problem = ProblemFactory.createEuclideanProblem(); break;
            case CITIES: problem = ProblemFactory.createEuclideanProblemOfSize(sizeOfProblemType(problemType)); break;
            case FULLY_RANDOM: problem = ProblemFactory.createRandomProblemOfSize(sizeOfProblemType(problemType)); break;
            case PARTIALLY_RANDOM: problem = ProblemFactory.createPredictableRandomProblemOfSize(sizeOfProblemType(problemType)); break;
            
            default: problem = ProblemFactory.createSimplestProblem(); break;
        }

        return problem;
    }

    private static String coreProblemType(String type) {
        // Use a regular expression to remove trailing digits
        return type.replaceAll("\\d+$", "");
    }

    private static int sizeOfProblemType(String type) {
        // Use a regular expression to extract trailing digits
        String trailingDigits = type.replaceAll(".*?(\\d+)$", "$1");

        // Check if a number was found and return as an integer
        return trailingDigits.matches("\\d+") ? Integer.parseInt(trailingDigits) : null;
   
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

    private static Problem createEuclideanProblemOfSize(int size) {
        
        if (size >15 || size <=0) {
            throw new IllegalArgumentException("Cannot generate an Euclidean problem of size "+size+".");
        }

        double[][] fullArray = {
            {37, 95},
            {73, 59},
            {15, 15},
            {5, 86},
            {60, 70},
            {2, 96},
            {83, 21},
            {18, 18},
            {30, 52},
            {43, 29},
            {61, 13},
            {29, 36},
            {45, 78},
            {19, 51},
            {59, 4}
        };

        // Truncate the array
        double[][] truncated = new double[size][];
        for (int j = 0; j < size; j++) {
            truncated[j] = fullArray[j];
        }

        return new EuclideanProblem(truncated);
        
    }

    // Synctactic sugar
    private static Problem createPredictableRandomProblemOfSize(int size) {
        return createRandomProblemOfSize(size, true);
    }

    // Defaults to fully random.
    private static Problem createRandomProblemOfSize(int size) {
        return createRandomProblemOfSize(size, false);
    }

    

    private static Problem createRandomProblemOfSize(int size, boolean fixedSeed) {
        
        if (size >150 || size <=0) {
            throw new IllegalArgumentException("Cannot generate a Random problem of size "+size+".");
        }
        Random random;
        if(fixedSeed) {
            random = new Random(42);
        } else {
            random = new Random();
        }
        
        double[][] coordinates = new double[size][2];
        for (int j = 0; j < size; j++) {
            coordinates[j][0] = (double) random.nextInt(101);
            coordinates[j][1] = (double) random.nextInt(101);
        }

        // Fair enough, if we get twice the same coordinates, problem will be smaller...
        return new EuclideanProblem(coordinates);
        
    }

}
