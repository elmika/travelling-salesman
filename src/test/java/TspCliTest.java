import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.elmika.tsp.adapter.cli.TspCli;
import com.elmika.tsp.application.ConfigLoader;
import com.elmika.tsp.application.ProblemConfiguration;
import com.elmika.tsp.application.ProblemProvider;
import com.elmika.tsp.application.SolveTspUseCase;
import com.elmika.tsp.domain.DistanceMatrixProblem;
import com.elmika.tsp.domain.Problem;

public class TspCliTest {

    @Test
    public void runCompletesWithFakeAdapters() {
        ConfigLoader configLoader = () -> new ProblemConfiguration("trivial", "brute-force");
        ProblemProvider problemProvider = type -> {
            double[][] distances = {{0, 1, 1}, {1, 0, 1}, {1, 1, 0}};
            return new DistanceMatrixProblem(distances);
        };
        TspCli cli = new TspCli(configLoader, problemProvider, new SolveTspUseCase());

        assertDoesNotThrow(cli::run);
    }
}
