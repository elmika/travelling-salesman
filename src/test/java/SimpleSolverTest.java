
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.elmika.tsp.SimpleSolver;

public class SimpleSolverTest {

    // Create 3D distance matrix for a problem with all distance set to 1.
    private double[][] create4xProblem() {
        double[][] distanceMatrix = { 
            {1, 1, 1, 1},
            {1, 1, 1, 1},
            {1, 1, 1, 1},
            {1, 1, 1, 1}
        };
        return distanceMatrix;
    }

    @ParameterizedTest
    @ValueSource(strings = {"brute-force","random", "random10", "random100" })
    public void testSolvingStrategies(String testType) {
        double[][] distanceMatrix = this.create4xProblem();
        SimpleSolver solver = new SimpleSolver(distanceMatrix);
        Integer[] result = solver.findSolution(testType);

        // Assuming solve returns a cost
        assertEquals(4, solver.getTotalDistance(result));
    }

    @Test
    public void test3xProblem() {
        double[][] distanceMatrix = {
            {1, 1, 1},
            {1, 1, 1},
            {1, 1, 1}
        };
        SimpleSolver solver = new SimpleSolver(distanceMatrix);
        Integer[] result = solver.findSolution("brute-force");

        // Assuming solve returns a cost
        assertEquals(3, solver.getTotalDistance(result));
    }
}
