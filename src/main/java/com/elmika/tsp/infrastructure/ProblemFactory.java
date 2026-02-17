package com.elmika.tsp.infrastructure;

import java.util.Random;

import com.elmika.tsp.domain.DistanceMatrixProblem;
import com.elmika.tsp.domain.EuclideanProblem;
import com.elmika.tsp.domain.Problem;

public class ProblemFactory {

    public static Problem createProblem(String problemType) {
        Problem problem;
        final String TRIVIAL = "trivial";
        final String SIMPLE = "simple";
        final String BIGGER = "bigger";
        final String EUCLIDEAN = "euclidean";
        final String CITIES = "cities";
        final String FULLY_RANDOM = "fully-random";
        final String PARTIALLY_RANDOM = "partially-random";

        switch (coreProblemType(problemType)) {
            case TRIVIAL: problem = createSimplestProblem(); break;
            case SIMPLE: problem = createSimpleProblem(); break;
            case BIGGER: problem = createBiggerProblem(); break;
            case EUCLIDEAN: problem = createEuclideanProblem(); break;
            case CITIES: problem = createEuclideanProblemOfSize(sizeOfProblemType(problemType)); break;
            case FULLY_RANDOM: problem = createRandomProblemOfSize(sizeOfProblemType(problemType)); break;
            case PARTIALLY_RANDOM: problem = createPredictableRandomProblemOfSize(sizeOfProblemType(problemType)); break;
            default: problem = createSimplestProblem(); break;
        }
        return problem;
    }

    private static String coreProblemType(String type) {
        return type.replaceAll("\\d+$", "");
    }

    private static int sizeOfProblemType(String type) {
        String trailingDigits = type.replaceAll(".*?(\\d+)$", "$1");
        return trailingDigits.matches("\\d+") ? Integer.parseInt(trailingDigits) : 0;
    }

    private static Problem createSimpleProblem() {
        double[][] distances = {
            {0.0, 3.0, 4.0, 2.0, 7.0},
            {3.0, 0.0, 4.0, 6.0, 3.0},
            {4.0, 4.0, 0.0, 5.0, 8.0},
            {2.0, 6.0, 5.0, 0.0, 6.0},
            {7.0, 3.0, 8.0, 6.0, 0.0}
        };
        return new DistanceMatrixProblem(distances);
    }

    private static Problem createSimplestProblem() {
        double[][] distances = {
            {0.0, 4.0, 6.0, 3.0},
            {4.0, 0.0, 5.0, 8.0},
            {6.0, 5.0, 0.0, 6.0},
            {3.0, 8.0, 6.0, 0.0}
        };
        return new DistanceMatrixProblem(distances);
    }

    private static Problem createBiggerProblem() {
        double[][] distances = {
            {0.0, 2.0, 7.0, 3.0, 4.0, 5.0},
            {2.0, 0.0, 3.0, 4.0, 2.0, 7.0},
            {7.0, 3.0, 0.0, 4.0, 6.0, 3.0},
            {3.0, 4.0, 4.0, 0.0, 5.0, 8.0},
            {4.0, 2.0, 6.0, 5.0, 0.0, 6.0},
            {5.0, 7.0, 3.0, 8.0, 6.0, 0.0}
        };
        return new DistanceMatrixProblem(distances);
    }

    private static Problem createEuclideanProblem() {
        double[][] points = {
            {0.0, 2.0}, {2.0, 0.0}, {7.0, 3.0}, {3.0, 4.0},
            {4.0, 2.0}, {5.0, 7.0}, {1.0, 7.0}
        };
        return new EuclideanProblem(points);
    }

    private static Problem createEuclideanProblemOfSize(int size) {
        if (size > 15 || size <= 0) {
            throw new IllegalArgumentException("Cannot generate an Euclidean problem of size " + size + ".");
        }
        double[][] fullArray = {
            {37, 95}, {73, 59}, {15, 15}, {5, 86}, {60, 70},
            {2, 96}, {83, 21}, {18, 18}, {30, 52}, {43, 29},
            {61, 13}, {29, 36}, {45, 78}, {19, 51}, {59, 4}
        };
        double[][] truncated = new double[size][];
        for (int j = 0; j < size; j++) {
            truncated[j] = fullArray[j];
        }
        return new EuclideanProblem(truncated);
    }

    private static Problem createPredictableRandomProblemOfSize(int size) {
        return createRandomProblemOfSize(size, true);
    }

    private static Problem createRandomProblemOfSize(int size) {
        return createRandomProblemOfSize(size, false);
    }

    private static Problem createRandomProblemOfSize(int size, boolean fixedSeed) {
        if (size > 150 || size <= 0) {
            throw new IllegalArgumentException("Cannot generate a Random problem of size " + size + ".");
        }
        Random random = fixedSeed ? new Random(42) : new Random();
        double[][] coordinates = new double[size][2];
        for (int j = 0; j < size; j++) {
            coordinates[j][0] = (double) random.nextInt(101);
            coordinates[j][1] = (double) random.nextInt(101);
        }
        return new EuclideanProblem(coordinates);
    }
}
