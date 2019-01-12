/*package analysis;

import analysis.service.CalcEngine;
import core.model.Bus;
import core.model.Route;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class testBusCost {

    @Test
    public void testBusCost() { // snapshot approach
        Route route = new Route(2, 1, "Route");
        Bus bus1 = new Bus(1, route, 7, 15, 15, 15);
        Bus bus2 = new Bus(2, route, 7, 20, 25, 25);
        Bus bus3 = new Bus(3, route, 7, 35, 45, 25);
        Bus bus4 = new Bus(4, route, 7, 50, 0, 25);

        ArrayList<Bus> buses = new ArrayList<>();
        buses.add(bus1);
        buses.add(bus2);
        buses.add(bus3);
        buses.add(bus4);

        CalcEngine engine = new CalcEngine();
        Assertions.assertEquals(1975, engine.computeBusCost(buses));
    }

    @Test
    public void testBusCostNoBuses() {
        ArrayList<Bus> buses = new ArrayList<>();
        CalcEngine engine = new CalcEngine();
        Assertions.assertEquals(0, engine.computeBusCost(buses));
    }

    @Test
    public void testBusCostNullList() {
        CalcEngine engine = new CalcEngine();
        Assertions.assertThrows(IllegalArgumentException.class, () -> engine.computeBusCost(null));
    }
}*/
