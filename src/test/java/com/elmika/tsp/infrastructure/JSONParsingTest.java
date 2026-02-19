package com.elmika.tsp.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.elmika.tsp.application.ConfigLoadException;
import com.elmika.tsp.application.ProblemConfiguration;
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
    public void getConfigWithMissingFileThrowsConfigLoadException(@TempDir Path tempDir) {
        Path nonExistent = tempDir.resolve("nonexistent.json");

        ConfigLoadException e = assertThrows(ConfigLoadException.class,
            () -> JSONParsing.getConfig(nonExistent));

        assertTrue(e.getMessage().contains("not found"));
        assertTrue(e.getMessage().contains("nonexistent.json"));
    }

    @Test
    public void getConfigWithMalformedFileThrowsConfigLoadException(@TempDir Path tempDir) throws Exception {
        Path configFile = tempDir.resolve("problemConfiguration.json");
        Files.writeString(configFile, "not valid json {", StandardCharsets.UTF_8);

        ConfigLoadException e = assertThrows(ConfigLoadException.class,
            () -> JSONParsing.getConfig(configFile));

        assertTrue(e.getMessage().contains("parse"));
        assertNotNull(e.getCause());
    }
}
