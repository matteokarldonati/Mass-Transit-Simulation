package core.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class StopTest {

    private Stop stop1;
    private Stop stop2;
    @Test
    public void testDistanceTo_1() {
        int expected, actual;

        /* Setup Test */
        stop1 = new Stop(1, "S1", 10, 5, 10);
        stop2 = new Stop(1, "S2", 10, 2, 23);
        expected = 933;
        /*check that null setup throws an exception*/
        Assertions.assertThrows(NullPointerException.class, ()->new Stop(1, "Null passenger bus", 10, 100, 4));
        /* Perform Test */

        actual = (int) stop1.distanceTo(stop2);

        /* Verify Results */
        assertEquals(expected, actual);
    }

    @Test
    public void testDistanceTo_2() {
        int expected, actual;

        /* Setup Test */


        stop1 = new Stop(1, "S1", 10, 100, 4);
        stop2 = new Stop(1, "S2", 10, 50, 2);
        expected = 3502;

        /* Perform Test */
        actual = (int) stop1.distanceTo(stop2);

        /* Verify Results */
        assertEquals(expected, actual);
    }

    @Test
    public void testDistanceTo_3() {
        int expected, actual;

        /* Setup Test */
        stop1 = new Stop(1, "S1", 10, 1330, 10);
        stop2 = new Stop(1, "S2", 10, 20, 100);
        expected = 91916;

        /* Perform Test */
        actual = (int) stop1.distanceTo(stop2);

        /* Verify Results */
        assertEquals(expected, actual);
    }

}
