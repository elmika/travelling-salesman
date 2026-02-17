import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONObject;

import com.elmika.tsp.infrastructure.JSONParsing;

public class JSONParsingTest {

    @Test
    public void testReadJsonFile() {
        JSONObject json = JSONParsing.read("file.json");

        if (json == null) {
            throw new AssertionError("Could not read the json file.");
        }

        try {
            String a = json.getString("yes");
            String b = json.getString("test");
            assertEquals("Sample json value", b);
            assertEquals("Why not", a);
        } catch (Exception e) {
            throw new AssertionError("Could not read the json configuration values.");
        }
    }
}
