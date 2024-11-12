import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.json.JSONObject;

import com.elmika.tsp.JSONParsing;
import com.elmika.tsp.PermutationsIterator;

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

    private void printIterations(PermutationsIterator iterator){
        while(iterator.hasNext()) {
            int[] iteration = iterator.next();
            System.out.println(Arrays.toString(iteration));
        }
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

    // @Test
    public void displayIterators() {
        
        for(int i=1; i<6; i++) {
            System.out.println("Running Iterator with value: "+i);
            PermutationsIterator iterator = new PermutationsIterator(i);
            printIterations(iterator);
        }
    }


    // Is correct if:
    // 1) Has (n-1)! combinations
    // 2) All combinations start with 1.
    // 3) No two combinations are equal.
}
