/*package analysis;

import core.model.Stop;
import analysis.service.CalcEngine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class testComputeWaitingTime {

    @Test
    public void testWaitingTime() {
        ArrayList<Stop> stops = new ArrayList<>();
        Stop stop1 = new Stop(1, "Stop 1", 3, 3, 2);
        Stop stop2 = new Stop(2, "Stop 2", 7, 4, 8);
        Stop stop3 = new Stop(3, "Stop 3", 9, 1, 15);
        Stop stop4 = new Stop(1, "Stop 1", 1, 14, 0);

        stops.add(stop1);
        stops.add(stop2);
        stops.add(stop3);
        stops.add(stop4);

        CalcEngine engine = new CalcEngine();
        int result = engine.computeWaitingTime(stops);

        Assertions.assertEquals(20, result);

    }

    @Test
    public void testWaitingTimeNoStops() {
        ArrayList<Stop> stops = new ArrayList<>();
        CalcEngine engine = new CalcEngine();
        Assertions.assertThrows(IllegalArgumentException.class, () -> engine.computeWaitingTime(stops));
    }

    @Test
    public void testWaitingTimeNullList() {
        CalcEngine engine = new CalcEngine();
        Assertions.assertThrows(IllegalArgumentException.class, () -> engine.computeWaitingTime(null));
    }


}*/
