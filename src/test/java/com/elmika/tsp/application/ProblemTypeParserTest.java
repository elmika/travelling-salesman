package com.elmika.tsp.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ProblemTypeParserTest {

    @Test
    public void coreProblemTypeStripsTrailingDigits() {
        assertEquals("cities", ProblemTypeParser.coreProblemType("cities10"));
        assertEquals("fully-random", ProblemTypeParser.coreProblemType("fully-random100"));
        assertEquals("partially-random", ProblemTypeParser.coreProblemType("partially-random5"));
        assertEquals("simple", ProblemTypeParser.coreProblemType("simple"));
        assertEquals("circle-", ProblemTypeParser.coreProblemType("circle-12"));
        assertEquals("cluster-", ProblemTypeParser.coreProblemType("cluster-20"));
    }

    @Test
    public void sizeOfProblemTypeExtractsTrailingDigits() {
        assertEquals(10, ProblemTypeParser.sizeOfProblemType("cities10"));
        assertEquals(100, ProblemTypeParser.sizeOfProblemType("fully-random100"));
        assertEquals(5, ProblemTypeParser.sizeOfProblemType("partially-random5"));
        assertEquals(0, ProblemTypeParser.sizeOfProblemType("simple"));
        assertEquals(0, ProblemTypeParser.sizeOfProblemType("cities"));
        assertEquals(12, ProblemTypeParser.sizeOfProblemType("circle-12"));
        assertEquals(20, ProblemTypeParser.sizeOfProblemType("cluster-20"));
    }

    @Test
    public void coreProblemTypeHandlesNull() {
        assertEquals("", ProblemTypeParser.coreProblemType(null));
    }

    @Test
    public void sizeOfProblemTypeHandlesNull() {
        assertEquals(0, ProblemTypeParser.sizeOfProblemType(null));
    }

    @Test
    public void validateSizeForTypeCitiesAcceptsValidRange() {
        ProblemTypeParser.validateSizeForType(ProblemTypeParser.CITIES, 8);
        ProblemTypeParser.validateSizeForType(ProblemTypeParser.CITIES, 30);
        ProblemTypeParser.validateSizeForType(ProblemTypeParser.CITIES, 10);
    }

    @Test
    public void validateSizeForTypeCitiesRejectsOutOfRange() {
        assertThrows(IllegalArgumentException.class,
            () -> ProblemTypeParser.validateSizeForType(ProblemTypeParser.CITIES, 0));
        assertThrows(IllegalArgumentException.class,
            () -> ProblemTypeParser.validateSizeForType(ProblemTypeParser.CITIES, 7));
        assertThrows(IllegalArgumentException.class,
            () -> ProblemTypeParser.validateSizeForType(ProblemTypeParser.CITIES, 31));
    }

    @Test
    public void cities0HasSizeZeroAndFailsValidation() {
        int size = ProblemTypeParser.sizeOfProblemType("cities0");
        assertEquals(0, size);
        assertThrows(IllegalArgumentException.class,
            () -> ProblemTypeParser.validateSizeForType(ProblemTypeParser.CITIES, size));
    }

    @Test
    public void fullyRandom1001ExceedsMaxAndFailsValidation() {
        int size = ProblemTypeParser.sizeOfProblemType("fully-random1001");
        assertEquals(1001, size);
        assertThrows(IllegalArgumentException.class,
            () -> ProblemTypeParser.validateSizeForType(ProblemTypeParser.FULLY_RANDOM, size));
    }

    @Test
    public void citiesXYZHasNoTrailingDigits() {
        assertEquals("citiesXYZ", ProblemTypeParser.coreProblemType("citiesXYZ"));
        assertEquals(0, ProblemTypeParser.sizeOfProblemType("citiesXYZ"));
    }

    @Test
    public void validateSizeForTypeRandomAcceptsValidRange() {
        ProblemTypeParser.validateSizeForType(ProblemTypeParser.FULLY_RANDOM, 1);
        ProblemTypeParser.validateSizeForType(ProblemTypeParser.FULLY_RANDOM, 1000);
        ProblemTypeParser.validateSizeForType(ProblemTypeParser.PARTIALLY_RANDOM, 50);
    }

    @Test
    public void validateSizeForTypeRandomRejectsOutOfRange() {
        assertThrows(IllegalArgumentException.class,
            () -> ProblemTypeParser.validateSizeForType(ProblemTypeParser.FULLY_RANDOM, 0));
        assertThrows(IllegalArgumentException.class,
            () -> ProblemTypeParser.validateSizeForType(ProblemTypeParser.FULLY_RANDOM, 1001));
    }

    @Test
    public void validateSizeForTypeCircleAcceptsValidRange() {
        ProblemTypeParser.validateSizeForType(ProblemTypeParser.CIRCLE, 3);
        ProblemTypeParser.validateSizeForType(ProblemTypeParser.CIRCLE, 1000);
        ProblemTypeParser.validateSizeForType(ProblemTypeParser.CIRCLE, 50);
    }

    @Test
    public void validateSizeForTypeCircleRejectsOutOfRange() {
        assertThrows(IllegalArgumentException.class,
            () -> ProblemTypeParser.validateSizeForType(ProblemTypeParser.CIRCLE, 2));
        assertThrows(IllegalArgumentException.class,
            () -> ProblemTypeParser.validateSizeForType(ProblemTypeParser.CIRCLE, 1001));
    }

    @Test
    public void validateSizeForTypeClusterAcceptsValidRange() {
        ProblemTypeParser.validateSizeForType(ProblemTypeParser.CLUSTER, 3);
        ProblemTypeParser.validateSizeForType(ProblemTypeParser.CLUSTER, 1000);
    }

    @Test
    public void validateSizeForTypeClusterRejectsOutOfRange() {
        assertThrows(IllegalArgumentException.class,
            () -> ProblemTypeParser.validateSizeForType(ProblemTypeParser.CLUSTER, 2));
        assertThrows(IllegalArgumentException.class,
            () -> ProblemTypeParser.validateSizeForType(ProblemTypeParser.CLUSTER, 1001));
    }
}
