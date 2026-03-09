package com.elmika.tsp.infrastructure;

import java.util.Random;

import com.elmika.tsp.application.ProblemTypeParser;
import com.elmika.tsp.domain.problem.DistanceMatrixProblem;
import com.elmika.tsp.domain.problem.EuclideanProblem;
import com.elmika.tsp.domain.problem.Problem;

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
            case ProblemTypeParser.CIRCLE:
                ProblemTypeParser.validateSizeForType(core, size);
                return createCircleProblemOfSize(size);
            case ProblemTypeParser.CLUSTER:
                ProblemTypeParser.validateSizeForType(core, size);
                return createClusterProblemOfSize(size);
            default:
                throw new IllegalArgumentException("Unknown problem type: '" + problemType + "'.");
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

    /**
     * Returns the first {@code size} cities from the 30-city curated set.
     * Coordinates are hand-picked to give good spread across [0,100]x[0,100].
     */
    private static Problem createEuclideanProblemOfSize(int size) {
        double[][] fullArray = {
            // --- original 15 ---
            {37, 95}, {73, 59}, {15, 15}, {5,  86}, {60, 70},
            {2,  96}, {83, 21}, {18, 18}, {30, 52}, {43, 29},
            {61, 13}, {29, 36}, {45, 78}, {19, 51}, {59,  4},
            // --- extended to 30 ---
            {90, 90}, {10, 90}, {90, 10}, {50, 50}, {70, 40},
            {80, 70}, {40, 80}, {20, 30}, {55, 20}, {85, 55},
            {30, 70}, {65, 85}, {75, 15}, {45, 60}, {10, 50}
        };
        double[][] truncated = new double[size][];
        for (int j = 0; j < size; j++) {
            truncated[j] = fullArray[j];
        }
        return new EuclideanProblem(truncated);
    }

    /**
     * Places {@code n} cities equally spaced on a circle of radius 40
     * centred at (50, 50). The trivially optimal tour visits them in order.
     */
    private static Problem createCircleProblemOfSize(int n) {
        double[][] coords = new double[n][2];
        double centerX = 50.0, centerY = 50.0, radius = 40.0;
        for (int i = 0; i < n; i++) {
            double angle = 2.0 * Math.PI * i / n;
            coords[i][0] = centerX + radius * Math.cos(angle);
            coords[i][1] = centerY + radius * Math.sin(angle);
        }
        return new EuclideanProblem(coords);
    }

    /**
     * Places {@code n} cities in sqrt(n) tight clusters spread across the map.
     * Seeded by {@code n} so the same size always produces the same layout.
     */
    private static Problem createClusterProblemOfSize(int n) {
        int k = Math.max(2, (int) Math.round(Math.sqrt(n)));
        Random rng = new Random((long) n * 1_000_003L);

        double[][] centers = new double[k][2];
        for (int c = 0; c < k; c++) {
            centers[c][0] = 15.0 + rng.nextDouble() * 70.0;
            centers[c][1] = 15.0 + rng.nextDouble() * 70.0;
        }

        double[][] coords = new double[n][2];
        for (int i = 0; i < n; i++) {
            int cluster = rng.nextInt(k);
            coords[i][0] = Math.max(0, Math.min(100, centers[cluster][0] + rng.nextGaussian() * 8.0));
            coords[i][1] = Math.max(0, Math.min(100, centers[cluster][1] + rng.nextGaussian() * 8.0));
        }
        return new EuclideanProblem(coords);
    }

    /**
     * Reproducible random problem: each size {@code n} uses an independent seed
     * derived from {@code n}, so partially-random5 always gives the same 5 cities
     * and those cities are unrelated to partially-random10.
     */
    private static Problem createPredictableRandomProblemOfSize(int size) {
        return createRandomProblemOfSize(size, (long) size * 1_000_003L);
    }

    private static Problem createRandomProblemOfSize(int size) {
        return createRandomProblemOfSize(size, null);
    }

    private static Problem createRandomProblemOfSize(int size, Long seed) {
        Random random = seed != null ? new Random(seed) : new Random();
        double[][] coordinates = new double[size][2];
        for (int j = 0; j < size; j++) {
            coordinates[j][0] = (double) random.nextInt(101);
            coordinates[j][1] = (double) random.nextInt(101);
        }
        return new EuclideanProblem(coordinates);
    }
}
