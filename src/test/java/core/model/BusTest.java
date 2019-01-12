package core.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BusTest {

    private Bus bus10;
    private Stop stop10;
    private Stop stop65;
    private Stop stop104;
    private Route route1;
    private int time;

    @BeforeEach
    public void setUp() {
        // TODO : Implement real passenger data in the tests (with stop.setPassengerData(data)
        stop10 = new Stop(10, "Arlington Street", 20, 5, 10);
        stop65 = new Stop(65, "Apple Avenue", 10, 2, 23);
        stop104 = new Stop(104, "South Street", 30, 20, 35);
        route1 = new Route(1, 1, "Route 1");
        route1.add(stop10);
        route1.add(stop65);
        route1.add(stop104);
        bus10 = new Bus(10, route1, 0, 10, 30, 20);
    }

    @Test
    public void testMoveBus_Simple() {
        /* Setup Test */
        time = 0;

        /* Perform Test */
        bus10.moveBus(time);

        /* Verify Results */
        assertEquals(bus10.getCurStop(), stop10);
        assertEquals(bus10.getNextStop(), stop65);
    }

    @Test
    public void testMoveBus_Full() {
        /* Setup Test */
        time = 0;

        /* Perform Test */
        bus10.moveBus(time);
        time = bus10.getArrivalTime();

        /* Verify Results */
        assertEquals(bus10.getCurStop(), stop10);
        assertEquals(bus10.getNextStop(), stop65);

        /* Perform Test */
        bus10.moveBus(time);
        time = bus10.getArrivalTime();

        /* Verify Results */
        assertEquals(bus10.getCurStop(), stop65);
        assertEquals(bus10.getNextStop(), stop104);

        /* Perform Test */
        bus10.moveBus(time);

        /* Verify Results */
        assertEquals(bus10.getCurStop(), stop104);
        assertEquals(bus10.getNextStop(), stop10);
    }

    @Test
    public void testGetArrivalTime_Start() {
        int expected, actual;

        /* Setup Test */
        time = 0;
        expected = 0;

        /* Perform Test */
        actual = bus10.getArrivalTime();

        /* Verify Results */
        assertEquals(expected, actual);
    }

    @Test
    public void testGetArrivalTime_Simple() {
        int expected, actual;

        /* Setup Test */
        time = 0;
        expected = 1 + ((int) stop10.distanceTo(stop65) * 60 / bus10.getSpeed());

        /* Perform Test */
        bus10.moveBus(time);
        actual = bus10.getArrivalTime();

        /* Verify Results */
        assertEquals(expected, actual);
    }

    @Test
    public void testGetArrivalTime_Full() {
        int expected, actual;

        /* Setup Test */
        time = 0;
        expected = 1 + ((int) stop10.distanceTo(stop65) * 60 / bus10.getSpeed());

        /* Perform Test */
        bus10.moveBus(time);
        actual = bus10.getArrivalTime();
        time = bus10.getArrivalTime();

        /* Verify Results */
        assertEquals(expected, actual);

        /* Perform Test */
        bus10.moveBus(time);
        expected = time + 1 + ((int) stop65.distanceTo(stop104) * 60 / bus10.getSpeed());
        actual = bus10.getArrivalTime();
        time = bus10.getArrivalTime();

        /* Verify Results */
        assertEquals(expected, actual);

        /* Perform Test */
        bus10.moveBus(time);
        expected = time + 1 + ((int) stop104.distanceTo(stop10) * 60 / bus10.getSpeed());
        actual = bus10.getArrivalTime();

        /* Verify Results */
        assertEquals(expected, actual);
    }

}
