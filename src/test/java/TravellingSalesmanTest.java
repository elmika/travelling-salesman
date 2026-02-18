import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

import com.elmika.tsp.adapter.cli.TspCli;
import com.elmika.tsp.application.ConfigLoader;
import com.elmika.tsp.application.ProblemProvider;
import com.elmika.tsp.application.ProblemConfiguration;
import com.elmika.tsp.application.SolveTspUseCase;
import com.elmika.tsp.infrastructure.InMemoryProblemFactory;
import com.elmika.tsp.infrastructure.JsonFileConfigLoader;

public class TravellingSalesmanTest {

    @Test
    public void mainFlowRunsWithoutException() {
        ConfigLoader configLoader = new JsonFileConfigLoader();
        ProblemProvider problemProvider = new InMemoryProblemFactory();
        SolveTspUseCase solver = new SolveTspUseCase();

        TspCli cli = new TspCli(configLoader, problemProvider, solver);

        assertDoesNotThrow(cli::run);
    }
}
