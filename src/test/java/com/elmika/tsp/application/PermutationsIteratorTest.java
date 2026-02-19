package com.elmika.tsp.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PermutationsIteratorTest {

    private int countIterationsForN(int n) {
        int i = 0;
        PermutationsIterator iterator = new PermutationsIterator(n);
        while (iterator.hasNext()) {
            i++;
            iterator.next();
        }
        return i;
    }

    private boolean firstIterationValueIsAlwaysOne(int n) {
        PermutationsIterator iterator = new PermutationsIterator(n);
        if (iterator.hasNext()) {
            Integer[] iteration = iterator.next();
            if (iteration[0] != 1) {
                return false;
            }
        }
        return true;
    }

    private boolean hasDuplicates(int n) {
        Set<List<Integer>> previousIterations = new HashSet<>();
        PermutationsIterator iterator = new PermutationsIterator(n);
        while (iterator.hasNext()) {
            Integer[] newIteration = iterator.next();
            List<Integer> list = Arrays.stream(newIteration).collect(Collectors.toList());
            if (!previousIterations.add(list)) {
                return true;
            }
        }
        return false;
    }

    private boolean iterationsElementsValuesAreValid(int n) {
        PermutationsIterator iterator = new PermutationsIterator(n);
        if (iterator.hasNext()) {
            Integer[] iteration = iterator.next();
            if (!hasAllValuesFrom1ToN(iteration, n)) {
                return false;
            }
        }
        return true;
    }

    private boolean hasAllValuesFrom1ToN(Integer[] arr, int n) {
        if (arr.length != n) {
            return false;
        }
        boolean[] seen = new boolean[n + 1];
        for (Integer num : arr) {
            if (num < 1 || num > n) {
                return false;
            }
            if (seen[num]) {
                return false;
            }
            seen[num] = true;
        }
        return true;
    }

    @Test
    public void testCountIterations() {
        assertEquals(1, countIterationsForN(1));
        assertEquals(1, countIterationsForN(2));
        assertEquals(2, countIterationsForN(3));
        assertEquals(6, countIterationsForN(4));
        assertEquals(24, countIterationsForN(5));
        assertEquals(120, countIterationsForN(6));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8})
    public void testFirstIterationValue(int k) {
        assertTrue(firstIterationValueIsAlwaysOne(k));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8})
    public void testNoDuplicates(int k) {
        assertFalse(hasDuplicates(k));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8})
    public void testContainsCorrectValues(int k) {
        assertTrue(iterationsElementsValuesAreValid(k));
    }
}
