package com.elmika.tsp.infrastructure;

import java.util.Random;

import com.elmika.tsp.application.ProblemTypeParser;
import com.elmika.tsp.domain.DistanceMatrixProblem;
import com.elmika.tsp.domain.EuclideanProblem;
import com.elmika.tsp.domain.Problem;

public class ProblemFactory {

    public static Problem createProblem(String problemType) {
        String core = ProblemTypeParser.coreProblemType(problemType);
        int size = ProblemTypeParser.sizeOfProblemType(problemType);

        switch (core) {
            case ProblemTypeParser.TRIVIAL:
                return createSimplestProblem();
            case ProblemTypeParser.SIMPLE:
                return createSimpleProblem();
            case ProblemTypeParser.BIGGER:
                return createBiggerProblem();
            case ProblemTypeParser.EUCLIDEAN:
                return createEuclideanProblem();
            case ProblemTypeParser.CITIES:
                ProblemTypeParser.validateSizeForType(core, size);
                return createEuclideanProblemOfSize(size);
            case ProblemTypeParser.FULLY_RANDOM:
                ProblemTypeParser.validateSizeForType(core, size);
                return createRandomProblemOfSize(size);
            case ProblemTypeParser.PARTIALLY_RANDOM:
                ProblemTypeParser.validateSizeForType(core, size);
                return createPredictableRandomProblemOfSize(size);
            default:
                return createSimplestProblem();
        }
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
        Random random = fixedSeed ? new Random(42) : new Random();
        double[][] coordinates = new double[size][2];
        for (int j = 0; j < size; j++) {
            coordinates[j][0] = (double) random.nextInt(101);
            coordinates[j][1] = (double) random.nextInt(101);
        }
        return new EuclideanProblem(coordinates);
    }
}
