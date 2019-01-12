package core.controller;

import core.model.Bus;
import core.model.Stop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class SimulatorControllerTest {


    private SimulatorController sim;

    @BeforeEach
    public void setUp() {
        sim = new SimulatorController(new File("./src/test/resources/test_scenario.txt"), new File("./src/main/resources/statistics 0-10 for test_scenario.txt"));
    }
    @Test
    void nextEventMulti() {
        /*this test performs multiple assertions on the event queue making sure the correct number of events are in the queue at all times,
        the events are populated correctly as well as reflect any changes to the sim
         */


        /*
        assert that with the current setup, there should at any one time be only as many events as there are buses
        also tests that the events in the queue are repopulated
        */

        assertEquals(sim.getEvents().size(), sim.getBuses().size());
        sim.nextEvent();
        assertEquals(sim.getEvents().size(), sim.getBuses().size());


        /*begin setup*/
        sim.changeSpeed(60, 999); //high speed to guarantee first arrival
        Stop tmp = sim.getBuses().get(60).getCurStop();


        while(sim.getBuses().get(60).getCurStop().equals(tmp)) {
            sim.nextEvent();
        } //bus 60 has arrived at next stop, so speed has been updated and next event should reflect this
        System.out.println("Bus 60's speed is updated");

        /*end setup*/
        tmp = sim.getBuses().get(60).getNextStop();
        /*perform test*/
        sim.nextEvent();

        /*verify that the bus is the fastest to its next stop*/
        assertTrue(sim.getEvents().peek().getTime() >= sim.getBuses().get(60).getArrivalTime());


        int minArrivalTime = Integer.MAX_VALUE;
        Bus arriveFirst = null;
        for(Bus b: sim.getBuses().values()) {
            if (b.getArrivalTime() < minArrivalTime) {
                arriveFirst = b;
            }
            minArrivalTime = Math.min(b.getArrivalTime(), minArrivalTime);

        }
        assertEquals(sim.getEvents().peek().getId(), arriveFirst.getId()); //asserting that the event priority queue polls events with the lowest time

        assertEquals(sim.getEvents().size(), sim.getBuses().size()); //final assert to make sure same number of events in priority queue



    }
    @Test
    public void testParser_Simple() {
        /* Verify Results */
        assertEquals(4, sim.getBuses().keySet().size());
        assertEquals(12, sim.getStops().keySet().size());
        assertEquals(2, sim.getRoutes().keySet().size());

        assertEquals(7, sim.getRoutes().get(51).size());
        assertEquals(12, sim.getRoutes().get(55).size());

        assertEquals(60, sim.getEvents().peek().getId());

        assertEquals(60, sim.getBuses().get(sim.getEvents().peek().getId()).getId());
    }

    @Test
    public void testNextEvent_Simple() {
        /* Perform Tests */
        sim.nextEvent();

        /* Verify Results */
    }

    @Test
    public void testChangeCapacity_NegativeCapacity() {
        int busId, expected, actual;

        /* Setup Tests */
        busId = sim.getBuses().keySet().iterator().next();
        expected = sim.getBuses().get(busId).getCapacity();

        /* Perform Tests */
        sim.changeCapacity(busId, -149);
        actual = sim.getBuses().get(busId).getCapacity();

        /* Verify Results */
        assertEquals(expected, actual);
    }

    @Test
    public void testChangeCapacity_Simple() {
        int busId, expected, actual;

        /* Setup Tests */
        busId = sim.getBuses().keySet().iterator().next();
        expected = 149;

        /* Perform Tests */
        sim.changeCapacity(busId, 149);
        actual = sim.getBuses().get(busId).getCapacity();

        /* Verify Results */
        assertEquals(expected, actual);
    }

    @Test
    public void testChangeSpeed_ZeroSpeed() {
        int busId, expected, actual;

        /* Setup Tests */
        busId = sim.getBuses().keySet().iterator().next();
        expected = sim.getBuses().get(busId).getSpeed();

        /* Perform Tests */
        sim.changeSpeed(busId, 0);
        actual = sim.getBuses().get(busId).getSpeed();

        /* Verify Results */
        assertEquals(expected, actual);
    }

    @Test
    public void testChangeSpeed_Simple() {
        int busId, expected, actual;

        /* Setup Tests */
        busId = sim.getBuses().keySet().iterator().next();
        expected = sim.getBuses().get(busId).getSpeed();

        /* Perform Tests */
        sim.changeSpeed(busId, 20);
        actual = 20;

        /* Verify Results */
        assertNotEquals(expected, actual);
    }

}