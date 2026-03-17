package com.elmika.tsp.application;

/**
 * Parses and validates problem type strings (e.g. "cities10", "fully-random100", "partially-random5").
 * Centralizes core-type extraction, size extraction, and valid ranges.
 */
public final class ProblemTypeParser {

    public static final String TRIVIAL = "trivial";
    public static final String SIMPLE = "simple";
    public static final String BIGGER = "bigger";
    public static final String EUCLIDEAN = "euclidean";
    public static final String CITIES = "cities";
    public static final String FULLY_RANDOM = "fully-random";
    public static final String PARTIALLY_RANDOM = "partially-random";
    /** Core type string for "circle-N" problems (note trailing hyphen, as digits are stripped). */
    public static final String CIRCLE = "circle-";
    /** Core type string for "cluster-N" problems (note trailing hyphen, as digits are stripped). */
    public static final String CLUSTER = "cluster-";
    /**
     * Core type prefix for TSPLIB95 benchmark problems (e.g. "tsplib-berlin52").
     * Names like "berlin52" end in digits but must not be truncated, so
     * {@link #coreProblemType(String)} special-cases this prefix.
     */
    public static final String TSPLIB = "tsplib-";

    /** Valid size range for "citiesN": 8 to 30. */
    public static final int CITIES_SIZE_MIN = 8;
    public static final int CITIES_SIZE_MAX = 30;

    /** Valid size range for "fully-randomN" and "partially-randomN": 1 to 1000. */
    public static final int RANDOM_SIZE_MIN = 1;
    public static final int RANDOM_SIZE_MAX = 1000;

    /** Valid size range for "circle-N" and "cluster-N": 3 to 1000. */
    public static final int STRUCTURED_SIZE_MIN = 3;
    public static final int STRUCTURED_SIZE_MAX = 1000;

    private ProblemTypeParser() {
    }

    /**
     * Returns the core problem type with any trailing digits stripped (e.g. "cities10" -> "cities").
     * TSPLIB names (e.g. "tsplib-berlin52") return the prefix {@code "tsplib-"} unchanged, because
     * the suffix is an opaque name that may end in digits.
     */
    public static String coreProblemType(String type) {
        if (type == null) {
            return "";
        }
        if (type.startsWith(TSPLIB)) {
            return TSPLIB;
        }
        return type.replaceAll("\\d+$", "");
    }

    /**
     * Returns the numeric suffix if present (e.g. "cities10" -> 10, "simple" -> 0).
     * Returns 0 when there is no trailing number.
     */
    public static int sizeOfProblemType(String type) {
        if (type == null) {
            return 0;
        }
        String trailingDigits = type.replaceAll(".*?(\\d+)$", "$1");
        return trailingDigits.matches("\\d+") ? Integer.parseInt(trailingDigits) : 0;
    }

    /**
     * Validates that the given size is within the allowed range for the core type.
     * @throws IllegalArgumentException if size is out of range for cities or random types
     */
    public static void validateSizeForType(String coreType, int size) {
        if (CITIES.equals(coreType)) {
            if (size < CITIES_SIZE_MIN || size > CITIES_SIZE_MAX) {
                throw new IllegalArgumentException(
                    "Configuration error: 'problem' size " + size + " is out of range for cities. " +
                    "Allowed range: " + CITIES_SIZE_MIN + "–" + CITIES_SIZE_MAX + ". " +
                    "Example: cities10.");
            }
        } else if (FULLY_RANDOM.equals(coreType) || PARTIALLY_RANDOM.equals(coreType)) {
            if (size < RANDOM_SIZE_MIN || size > RANDOM_SIZE_MAX) {
                throw new IllegalArgumentException(
                    "Configuration error: 'problem' size " + size + " is out of range for random. " +
                    "Allowed range: " + RANDOM_SIZE_MIN + "–" + RANDOM_SIZE_MAX + ". " +
                    "Example: fully-random50.");
            }
        } else if (CIRCLE.equals(coreType) || CLUSTER.equals(coreType)) {
            if (size < STRUCTURED_SIZE_MIN || size > STRUCTURED_SIZE_MAX) {
                throw new IllegalArgumentException(
                    "Configuration error: 'problem' size " + size + " is out of range for structured problems. " +
                    "Allowed range: " + STRUCTURED_SIZE_MIN + "–" + STRUCTURED_SIZE_MAX + ". " +
                    "Example: circle-12, cluster-20.");
            }
        }
    }

    /**
     * Validates that the problem type string is valid (known type and size in range).
     * @throws IllegalArgumentException if the problem type is invalid
     */
    public static void validateProblemType(String problemType) {
        if (problemType == null || problemType.isBlank()) {
            return; // SolverConfiguration handles null/blank
        }
        String core = coreProblemType(problemType);
        int size = sizeOfProblemType(problemType);

        if (TSPLIB.equals(core)) {
            String tspName = problemType.substring(TSPLIB.length());
            if (tspName.isBlank()) {
                throw new IllegalArgumentException(
                    "Configuration error: 'problem' '" + problemType + "' requires a TSPLIB name suffix. " +
                    "Example: tsplib-berlin52.");
            }
            return; // existence is validated at load time by TspLibParser
        }

        if (CITIES.equals(core) || FULLY_RANDOM.equals(core) || PARTIALLY_RANDOM.equals(core)
                || CIRCLE.equals(core) || CLUSTER.equals(core)) {
            if (size == 0) {
                throw new IllegalArgumentException(
                    "Configuration error: 'problem' '" + problemType + "' requires a numeric suffix. " +
                    "Examples: cities10, fully-random50, partially-random5, circle-12, cluster-20.");
            }
            validateSizeForType(core, size);
        }
    }
}
