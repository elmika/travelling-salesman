package com.elmika.tsp.infrastructure;

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

    /** Valid size range for "citiesN": 1 to 15. */
    public static final int CITIES_SIZE_MIN = 1;
    public static final int CITIES_SIZE_MAX = 15;

    /** Valid size range for "fully-randomN" and "partially-randomN": 1 to 150. */
    public static final int RANDOM_SIZE_MIN = 1;
    public static final int RANDOM_SIZE_MAX = 150;

    private ProblemTypeParser() {
    }

    /**
     * Returns the core problem type with any trailing digits stripped (e.g. "cities10" -> "cities").
     */
    public static String coreProblemType(String type) {
        if (type == null) {
            return "";
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
                    "Cannot generate an Euclidean problem of size " + size + ".");
            }
        } else if (FULLY_RANDOM.equals(coreType) || PARTIALLY_RANDOM.equals(coreType)) {
            if (size < RANDOM_SIZE_MIN || size > RANDOM_SIZE_MAX) {
                throw new IllegalArgumentException(
                    "Cannot generate a Random problem of size " + size + ".");
            }
        }
    }
}
