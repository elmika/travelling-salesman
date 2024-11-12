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

import com.elmika.tsp.PermutationsIterator;

/* A permutation is correct if:
    1) Iterations provide (n-1)! combinations
    2) All combinations start with 1.
    3) No two combinations are equal.
    4) All combinations contain exactly once the values 1..n
 */
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
        Integer[] iteration;
        PermutationsIterator iterator = new PermutationsIterator(n);
        if (iterator.hasNext()) {
            iteration = iterator.next();
            if (iteration[0] != 1) {
                return false;
            }
        }
        return true;
    }

    private boolean hasDuplicates(int n) {
        Integer[] newIteration;
        Set<List<Integer>> previousIterations = new HashSet<>();
        PermutationsIterator iterator = new PermutationsIterator(n);

        while (iterator.hasNext()) {
            newIteration = iterator.next();
            List<Integer> list = Arrays.stream(newIteration).collect(Collectors.toList());
            if (!previousIterations.add(list)) {
                // If add returns false, there's a duplicate
                return true;
            }
        }
        return false;
    }

    private boolean iterationsElementsValuesAreValid(int n) {
        Integer[] iteration;
        PermutationsIterator iterator = new PermutationsIterator(n);
        if (iterator.hasNext()) {
            iteration = iterator.next();
            if (!hasAllValuesFrom1ToN(iteration, n)) {
                return false;
            }
        }
        return true;
    }

    private boolean hasAllValuesFrom1ToN(Integer[] arr, int n) {
        if (arr.length != n) {
            // If the array length isn't n, it can't contain all values from 1 to n
            return false;
        }

        boolean[] seen = new boolean[n + 1];  // Create an array to track seen values

        for (Integer num : arr) {
            if (num < 1 || num > n) {
                // If a number is outside the range 1 to n, return false
                return false;
            }
            if (seen[num]) {
                // If a number is already seen, return false (duplicate)
                return false;
            }
            seen[num] = true;  // Mark the number as seen
        }

        // If all numbers from 1 to n are seen, return true
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

    public void displayIterators() {
        for (int i = 1; i < 6; i++) {
            System.out.println("Running Iterator with value: " + i);
            PermutationsIterator iterator = new PermutationsIterator(i);
            while (iterator.hasNext()) {
                Integer[] iteration = iterator.next();
                System.out.println(Arrays.toString(iteration));
            }
        }
    }
}
