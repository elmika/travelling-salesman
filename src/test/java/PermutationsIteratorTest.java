import org.junit.jupiter.api.Test;
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
    
    private int countIterationsForN(int n){
        int i = 0;
        PermutationsIterator iterator = new PermutationsIterator(n);
        while(iterator.hasNext()) {
            i++;
            iterator.next();            
        }
        return i;
    }

    private boolean firstIterationValueIsAlwaysOne(int n){
        int[] iteration;
        PermutationsIterator iterator = new PermutationsIterator(n);
        if(iterator.hasNext()) {            
            iteration = iterator.next();
            if(iteration[0] != 1) {
                return false;
            }
        }
        return true;
    }

    private boolean hasDuplicates(int n){
        int[] newIteration;
        Set<List<Integer>> previousIterations = new HashSet<>();
        PermutationsIterator iterator = new PermutationsIterator(n);

        while (iterator.hasNext()) {
            newIteration = iterator.next();
            List<Integer> list = Arrays.stream(newIteration).boxed().collect(Collectors.toList());
            if (!previousIterations.add(list)) {
                // If add returns false, there's a duplicate
                return true;
            }
        }
        return false;
    }

    private boolean iterationsElementsValuesAreValid(int n) {
        int[] iteration;
        PermutationsIterator iterator = new PermutationsIterator(n);
        if (iterator.hasNext()) {            
            iteration = iterator.next();            
            if (!hasAllValuesFrom1ToN(iteration, n)) {
                return false;
            }
        }
        return true;
    }

    private boolean hasAllValuesFrom1ToN(int[] arr, int n) {
        if (arr.length != n) {
            // If the array length isn't n, it can't contain all values from 1 to n
            return false;
        }

        boolean[] seen = new boolean[n + 1];  // Create an array to track seen values

        for (int num : arr) {
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

    @Test
    public void testFirstIterationValue() {
        assertTrue(firstIterationValueIsAlwaysOne(1));
        assertTrue(firstIterationValueIsAlwaysOne(2));
        assertTrue(firstIterationValueIsAlwaysOne(3));
        assertTrue(firstIterationValueIsAlwaysOne(4));
        assertTrue(firstIterationValueIsAlwaysOne(5));
        assertTrue(firstIterationValueIsAlwaysOne(6));
    }

    @Test
    public void testNoDuplicates() {
        assertFalse(hasDuplicates(1));
        assertFalse(hasDuplicates(2));
        assertFalse(hasDuplicates(3));
        assertFalse(hasDuplicates(4));
        assertFalse(hasDuplicates(5));
        assertFalse(hasDuplicates(6));
    }

    @Test
    public void testContainsCorrectValues() {
        assertTrue(iterationsElementsValuesAreValid(1));
        assertTrue(iterationsElementsValuesAreValid(2));
        assertTrue(iterationsElementsValuesAreValid(3));
        assertTrue(iterationsElementsValuesAreValid(4));
        assertTrue(iterationsElementsValuesAreValid(5));
        assertTrue(iterationsElementsValuesAreValid(6));
    }

    public void displayIterators() {
        
        for(int i=1; i<6; i++) {
            System.out.println("Running Iterator with value: "+i);
            PermutationsIterator iterator = new PermutationsIterator(i);
            while(iterator.hasNext()) {
                int[] iteration = iterator.next();
                System.out.println(Arrays.toString(iteration));
            }
        }
    }
}