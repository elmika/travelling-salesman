import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.elmika.tsp.application.SolveTspUseCase;
import com.elmika.tsp.application.TspSolver;
import com.elmika.tsp.domain.DistanceMatrixProblem;
import com.elmika.tsp.domain.Problem;
import com.elmika.tsp.domain.Solution;

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
