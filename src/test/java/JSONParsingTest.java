import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.elmika.tsp.infrastructure.JSONParsing;
import com.fasterxml.jackson.databind.JsonNode;

public class JSONParsingTest {

    @Test
    public void testReadJsonFile() {
        JsonNode json = JSONParsing.read("file.json");

        assertNotNull(json, "Could not read the json file.");

        String a = json.path("yes").asText();
        String b = json.path("test").asText();

        assertEquals("Sample json value", b);
        assertEquals("Why not", a);
    }
}
