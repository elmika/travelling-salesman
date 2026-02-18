import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.elmika.tsp.adapter.view.RouteCoordinatesJsonExporter;
import com.elmika.tsp.adapter.view.RouteCoordinatesView;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RouteCoordinatesJsonExporterTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void exportsViewToJsonWithCoordinatesArray() throws Exception {
        RouteCoordinatesView view = new RouteCoordinatesView(List.of(
            new RouteCoordinatesView.Coordinate(0.0, 0.0),
            new RouteCoordinatesView.Coordinate(3.0, 4.0)
        ));

        String json = RouteCoordinatesJsonExporter.toJson(view);

        JsonNode root = MAPPER.readTree(json);
        assertTrue(root.has("coordinates"));
        JsonNode coords = root.get("coordinates");
        assertEquals(2, coords.size());
        assertEquals(0.0, coords.get(0).get("x").asDouble());
        assertEquals(0.0, coords.get(0).get("y").asDouble());
        assertEquals(3.0, coords.get(1).get("x").asDouble());
        assertEquals(4.0, coords.get(1).get("y").asDouble());
    }

    @Test
    public void nullViewThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> RouteCoordinatesJsonExporter.toJson(null));
    }
}
