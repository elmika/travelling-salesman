import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.elmika.tsp.Problem;
import com.elmika.tsp.ProblemFactory;

public class ProblemFactoryTest {

    @Test
    public void citiesTypeUsesTrailingDigitsAsSize() {
        Problem problem = ProblemFactory.createProblem("cities7");
        assertEquals(7, problem.getSize());
    }
}

