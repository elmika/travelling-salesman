import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.elmika.tsp.Problem;
import com.elmika.tsp.DistanceMatrixProblem;
import com.elmika.tsp.SolveTspUseCase;
import com.elmika.tsp.Solution;
import com.elmika.tsp.TspSolver;

public class SolveTspUseCaseTest {

    @Test
    public void solveDelegatesToSimpleSolver() {
        double[][] distances = {
            {0, 1, 1, 1},
            {1, 0, 1, 1},
            {1, 1, 0, 1},
            {1, 1, 1, 0}
        };
        Problem problem = new DistanceMatrixProblem(distances);
        TspSolver useCase = new SolveTspUseCase();

        Solution solution = useCase.solve(problem, "brute-force");

        assertEquals(4.0, solution.getTotalDistance());
        assertEquals(4, solution.getRoute().length);
    }
}
