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
    }

    @Test
    public void sizeOfProblemTypeExtractsTrailingDigits() {
        assertEquals(10, ProblemTypeParser.sizeOfProblemType("cities10"));
        assertEquals(100, ProblemTypeParser.sizeOfProblemType("fully-random100"));
        assertEquals(5, ProblemTypeParser.sizeOfProblemType("partially-random5"));
        assertEquals(0, ProblemTypeParser.sizeOfProblemType("simple"));
        assertEquals(0, ProblemTypeParser.sizeOfProblemType("cities"));
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
        ProblemTypeParser.validateSizeForType(ProblemTypeParser.CITIES, 15);
        ProblemTypeParser.validateSizeForType(ProblemTypeParser.CITIES, 10);
    }

    @Test
    public void validateSizeForTypeCitiesRejectsOutOfRange() {
        assertThrows(IllegalArgumentException.class,
            () -> ProblemTypeParser.validateSizeForType(ProblemTypeParser.CITIES, 0));
        assertThrows(IllegalArgumentException.class,
            () -> ProblemTypeParser.validateSizeForType(ProblemTypeParser.CITIES, 7));
        assertThrows(IllegalArgumentException.class,
            () -> ProblemTypeParser.validateSizeForType(ProblemTypeParser.CITIES, 16));
    }

    @Test
    public void cities0HasSizeZeroAndFailsValidation() {
        int size = ProblemTypeParser.sizeOfProblemType("cities0");
        assertEquals(0, size);
        assertThrows(IllegalArgumentException.class,
            () -> ProblemTypeParser.validateSizeForType(ProblemTypeParser.CITIES, size));
    }

    @Test
    public void fullyRandom151ExceedsMaxAndFailsValidation() {
        int size = ProblemTypeParser.sizeOfProblemType("fully-random151");
        assertEquals(151, size);
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
        ProblemTypeParser.validateSizeForType(ProblemTypeParser.FULLY_RANDOM, 150);
        ProblemTypeParser.validateSizeForType(ProblemTypeParser.PARTIALLY_RANDOM, 50);
    }

    @Test
    public void validateSizeForTypeRandomRejectsOutOfRange() {
        assertThrows(IllegalArgumentException.class,
            () -> ProblemTypeParser.validateSizeForType(ProblemTypeParser.FULLY_RANDOM, 0));
        assertThrows(IllegalArgumentException.class,
            () -> ProblemTypeParser.validateSizeForType(ProblemTypeParser.FULLY_RANDOM, 151));
    }
}
