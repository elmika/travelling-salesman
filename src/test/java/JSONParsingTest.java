import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.elmika.tsp.application.ProblemConfiguration;
import com.elmika.tsp.infrastructure.JSONParsing;
import com.fasterxml.jackson.databind.JsonNode;

public class JSONParsingTest {

    @Test
    public void testReadJsonFile() throws Exception {
        String path = Path.of(Objects.requireNonNull(getClass().getResource("/file.json")).toURI()).toString();
        JsonNode json = JSONParsing.read(path);

        assertNotNull(json, "Could not read the json file.");

        String a = json.path("yes").asText();
        String b = json.path("test").asText();

        assertEquals("Sample json value", b);
        assertEquals("Why not", a);
    }

    @Test
    public void getConfigWithValidFileReturnsParsedConfiguration(@TempDir Path tempDir) throws Exception {
        Path configFile = tempDir.resolve("problemConfiguration.json");
        Files.writeString(configFile, "{\"problem\":\"trivial\",\"resolutionStrategy\":\"brute-force\"}", StandardCharsets.UTF_8);

        ProblemConfiguration config = JSONParsing.getConfig(configFile);

        assertNotNull(config);
        assertEquals("trivial", config.getProblem());
        assertEquals("brute-force", config.getResolutionStrategy());
    }

    @Test
    public void getConfigWithMissingFileReturnsDefaults(@TempDir Path tempDir) {
        Path nonExistent = tempDir.resolve("nonexistent.json");

        ProblemConfiguration config = JSONParsing.getConfig(nonExistent);

        assertNotNull(config);
        assertEquals("simple", config.getProblem());
        assertEquals("random10", config.getResolutionStrategy());
    }

    @Test
    public void getConfigWithMalformedFileReturnsDefaults(@TempDir Path tempDir) throws Exception {
        Path configFile = tempDir.resolve("problemConfiguration.json");
        Files.writeString(configFile, "not valid json {", StandardCharsets.UTF_8);

        ProblemConfiguration config = JSONParsing.getConfig(configFile);

        assertNotNull(config);
        assertEquals("simple", config.getProblem());
        assertEquals("random10", config.getResolutionStrategy());
    }
}
