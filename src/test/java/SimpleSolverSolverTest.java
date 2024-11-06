import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.elmika.tsp.SimpleSolver;

public class SimpleSolverSolverTest {

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

    @Test
    public void testBruteForce() {
        double[][] distanceMatrix = this.create4xProblem();
        SimpleSolver solver = new SimpleSolver(distanceMatrix);
        Integer[] result = solver.findSolution("brute-force");

        // Assuming solve returns a cost
        assertEquals(4, solver.getTotalDistance(result));
    }

    public void test3xProblem() {
        double[][] distanceMatrix = {
            {1, 1, 1},
            {1, 1, 1},
            {1, 1, 1}
        };
        SimpleSolver solver = new SimpleSolver(distanceMatrix);
        // Integer[] result = solver.findSolution("brute-force");

        // Assuming solve returns a cost
        // assertEquals(3, solver.getTotalDistance(result));
    }
}
