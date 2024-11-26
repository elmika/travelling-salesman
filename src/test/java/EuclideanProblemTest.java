import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.elmika.tsp.Problem;
import com.elmika.tsp.EuclideanProblem;

public class EuclideanProblemTest {

    private Problem create4xProblem() {
        double[][] points = { 
            {0, 0},
            {0, 1},
            {1, 0},
            {1, 1}
        };
        return new EuclideanProblem(points);
    }

    private Problem create5xDuplicatePointProblem() {
        double[][] points = { 
            {0, 0},
            {0, 1},
            {1, 0},
            {1, 1},
            {1, 0}
        };
        return new EuclideanProblem(points);
    }

    private Problem create4xInvalidPointProblem() {
        double[][] points = { 
            {0, 0},
            {0, 1, 3},
            {1, 0},
            {1, 1}
        };
        return new EuclideanProblem(points);
    }

    private Problem create4xOtherInvalidPointProblem() {
        double[][] points = { 
            {0, 0},
            null,
            {1, 0},
            {1, 1}
        };
        return new EuclideanProblem(points);
    }

    private Problem create16xProblem() {
        double[][] points = { 
            {0, 0},
            {0, 1},
            {1, 0},
            {1, 1},
            {10, 0},
            {10, 1},
            {11, 0},
            {11, 1},
            {0, 10},
            {0, 11},
            {1, 10},
            {1, 11},
            {10, 10},
            {10, 11},
            {11, 10},
            {11, 11}
        };
        return new EuclideanProblem(points);
    }

    @Test
    public void create4xProblemSizeIs4Test() {        
        Problem problem = this.create4xProblem();
        assertEquals(4, problem.getSize());
    }

    @Test
    public void create5xDuplicatePointProblemSizeIs4Test() {        
        Problem problem = this.create5xDuplicatePointProblem();
        assertEquals(4, problem.getSize());
    }

    @Test
    public void create4xInvalidPointProblemThrowsExceptionTest() {     
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.create4xInvalidPointProblem();
        });

        // assertEquals("Euclidean problems instanciation expects array of Euclidean coordinates.", exception.getMessage());
    }

    @Test
    public void create4xOtherInvalidPointProblemThrowsExceptionTest() {     
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.create4xOtherInvalidPointProblem();
        });

        // assertEquals("Euclidean problems instanciation expects array of Euclidean coordinates.", exception.getMessage());
    }

    @Test
    public void create4xProblemDistanceWithSelfIsZeroTest() {        
        Problem problem = this.create4xProblem();
        
        assertEquals(0, problem.getDistance(4,4));
        assertEquals(0, problem.getDistance(1,1));
        assertEquals(0, problem.getDistance(2,2));
        assertEquals(0, problem.getDistance(3,3));
    }

    @Test
    public void create4xProblemDistancesTest() {        
        Problem problem = this.create4xProblem();
        
        assertEquals(1, problem.getDistance(1,2));
        assertEquals(1, problem.getDistance(2,4));
        assertEquals(1, problem.getDistance(4,3));
        assertEquals(1, problem.getDistance(3,1));

        double sqrt2 = Math.sqrt(2.0);

        assertEquals(sqrt2, problem.getDistance(1,4));
        assertEquals(sqrt2, problem.getDistance(2,3));
    }

    @Test
    public void create4xProblemDistanceOutOfRangeTest() {   

        Problem problem = this.create4xProblem();
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            problem.getDistance(1,5);
        });

        assertEquals("Point index out of range for distance calculation.", exception.getMessage());

        IllegalArgumentException otherException = assertThrows(IllegalArgumentException.class, () -> {
            problem.getDistance(0,2);
        });

        assertEquals("Point index out of range for distance calculation.", otherException.getMessage());    
        
    }
  
    @Test
    public void create15xProblemDistanceAreSymetricTest() {    
        Problem problem = create16xProblem();

        for(int i=1; i<= problem.getSize(); i++) {
            for(int j=1; j<=i; j++) {
                assertEquals(problem.getDistance(i,j), problem.getDistance(j,i));
            }
        }
    }

}
