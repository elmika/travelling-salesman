import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONObject;

import com.elmika.tsp.JSONParsing;

public class JSONParsingTest {
    
    @Test
    public void testReadJsonFile() {
        String a, b = null;

        JSONObject json = JSONParsing.read("file.json");

        if (json == null) {
            throw new AssertionError("Could not read the json file.");
        }

        try {
            a = json.getString("yes");
            b = json.getString("test");
        } catch(Exception e){
            throw new AssertionError("Could not read the json configuration values.");
        }

        // Assuming solve returns a cost
        assertEquals("Sample json value", b);
        assertEquals("Why not", a);
    }
}
