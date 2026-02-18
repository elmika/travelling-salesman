import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.elmika.tsp.application.ProblemConfiguration;
import com.elmika.tsp.application.SolverConfiguration;

public class SolverConfigurationTest {

    @Test
    public void createFromValidConfig() {
        SolverConfiguration config = SolverConfiguration.createFrom(
            new ProblemConfiguration("trivial", "brute-force"));
        assertEquals("trivial", config.getProblem());
        assertEquals("brute-force", config.getResolutionStrategy());
    }

    @Test
    public void createFromTrimsWhitespace() {
        SolverConfiguration config = SolverConfiguration.createFrom(
            new ProblemConfiguration("  cities10  ", "  random10  "));
        assertEquals("cities10", config.getProblem());
        assertEquals("random10", config.getResolutionStrategy());
    }

    @Test
    public void createFromNullRawThrows() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
            () -> SolverConfiguration.createFrom(null));
        assertTrue(e.getMessage().contains("null"));
    }

    @Test
    public void createFromNullProblemThrows() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
            () -> SolverConfiguration.createFrom(new ProblemConfiguration(null, "random")));
        assertTrue(e.getMessage().contains("problem"));
    }

    @Test
    public void createFromBlankProblemThrows() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
            () -> SolverConfiguration.createFrom(new ProblemConfiguration("  ", "random")));
        assertTrue(e.getMessage().contains("problem"));
    }

    @Test
    public void createFromNullStrategyThrows() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
            () -> SolverConfiguration.createFrom(new ProblemConfiguration("simple", null)));
        assertTrue(e.getMessage().contains("resolutionStrategy"));
    }

    @Test
    public void createFromUnknownStrategyThrows() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
            () -> SolverConfiguration.createFrom(new ProblemConfiguration("simple", "greedy")));
        assertTrue(e.getMessage().contains("greedy"));
        assertTrue(e.getMessage().contains("Allowed"));
    }

    @Test
    public void createFromInvalidProblemSizeCitiesThrows() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
            () -> SolverConfiguration.createFrom(new ProblemConfiguration("cities7", "brute-force")));
        assertTrue(e.getMessage().contains("cities"));
        assertTrue(e.getMessage().contains("out of range"));
    }

    @Test
    public void createFromInvalidProblemSizeRandomThrows() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
            () -> SolverConfiguration.createFrom(new ProblemConfiguration("fully-random151", "random10")));
        assertTrue(e.getMessage().contains("random"));
        assertTrue(e.getMessage().contains("out of range"));
    }

    @Test
    public void equalsAndHashCode() {
        SolverConfiguration a = SolverConfiguration.createFrom(
            new ProblemConfiguration("trivial", "brute-force"));
        SolverConfiguration b = SolverConfiguration.createFrom(
            new ProblemConfiguration("trivial", "brute-force"));
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
