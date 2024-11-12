import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.json.JSONObject;

import com.elmika.tsp.JSONParsing;
import com.elmika.tsp.PermutationsIterator;

public class PermutationsIteratorTest {

    private void printIterations(PermutationsIterator iterator){
        while(iterator.hasNext()) {
            int[] iteration = iterator.next();
            System.out.println(Arrays.toString(iteration));
        }
    }

    @Test
    public void testIterators() {
        
        for(int i=1; i<6; i++) {
            System.out.println("Running Iterator with value: "+i);
            PermutationsIterator iterator = new PermutationsIterator(i);
            printIterations(iterator);
        }
    }
}
