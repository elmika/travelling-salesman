import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.elmika.tsp.domain.Point;

public class PointTest {

    @Test
    public void pointsWithSameCoordinatesAreEqual() {
        Point p1 = new Point(1.0, 2.0);
        Point p2 = new Point(1.0, 2.0);

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    public void pointsWithDifferentCoordinatesAreNotEqual() {
        Point p1 = new Point(1.0, 2.0);
        Point p2 = new Point(2.0, 1.0);

        assertNotEquals(p1, p2);
    }

    @Test
    public void pointRejectsNaNOrInfiniteCoordinates() {
        assertThrows(IllegalArgumentException.class, () -> new Point(Double.NaN, 0.0));
        assertThrows(IllegalArgumentException.class, () -> new Point(0.0, Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> new Point(Double.POSITIVE_INFINITY, 0.0));
        assertThrows(IllegalArgumentException.class, () -> new Point(0.0, Double.NEGATIVE_INFINITY));
    }
}

