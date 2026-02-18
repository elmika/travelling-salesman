import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.elmika.tsp.domain.Problem;
import com.elmika.tsp.infrastructure.ProblemFactory;

public class ProblemFactoryTest {

    @Test
    public void citiesTypeUsesTrailingDigitsAsSize() {
        Problem problem = ProblemFactory.createProblem("cities10");
        assertEquals(10, problem.getSize());
    }
}
