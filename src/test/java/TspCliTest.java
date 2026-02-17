import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.elmika.tsp.ConfigLoader;
import com.elmika.tsp.ProblemConfiguration;
import com.elmika.tsp.ProblemProvider;
import com.elmika.tsp.Problem;
import com.elmika.tsp.DistanceMatrixProblem;
import com.elmika.tsp.SolveTspUseCase;
import com.elmika.tsp.TspCli;

public class TspCliTest {

    @Test
    public void runCompletesWithFakeAdapters() {
        ConfigLoader configLoader = () -> new ProblemConfiguration("trivial", "brute-force");
        ProblemProvider problemProvider = type -> {
            double[][] distances = {{0, 1, 1}, {1, 0, 1}, {1, 1, 0}};
            return new DistanceMatrixProblem(distances);
        };
        TspCli cli = new TspCli(configLoader, problemProvider, new SolveTspUseCase());

        assertDoesNotThrow(() -> cli.run());
    }
}
