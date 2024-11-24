
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.elmika.tsp.Problem;
import com.elmika.tsp.DistanceMatrixProblem;
import com.elmika.tsp.SimpleSolver;

public class SimpleSolverTest {

    private Problem create4xProblem() {
        double[][] distanceMatrix = { 
            {1, 1, 1, 1},
            {1, 1, 1, 1},
            {1, 1, 1, 1},
            {1, 1, 1, 1}
        };
        return new DistanceMatrixProblem(distanceMatrix);
    }

    @ParameterizedTest
    @ValueSource(strings = {"brute-force", "random", "random10", "random100"})
    public void testSolvingStrategies(String testType) {
        Problem problem = this.create4xProblem();
        SimpleSolver solver = new SimpleSolver(problem);
        Integer[] result = solver.findSolution(testType);

        assertEquals(4.0, solver.getTotalDistance(result));
    }

    @Test
    public void test3xProblem() {
        double[][] distanceMatrix = {
            {1, 1, 1},
            {1, 1, 1},
            {1, 1, 1}
        };
        Problem problem = new DistanceMatrixProblem(distanceMatrix);
        SimpleSolver solver = new SimpleSolver(problem);
        Integer[] result = solver.findSolution("brute-force");

        assertEquals(3.0, solver.getTotalDistance(result));
    }
}
