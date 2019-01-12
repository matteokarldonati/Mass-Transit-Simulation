package analysis;

import analysis.model.Report;
import analysis.service.CalcEngine;
import analysis.service.GraphEngine;
import core.model.Bus;
import core.model.Route;
import core.model.Stop;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class TestGraphEngine {

    @Test
    public void testGenerateGraph() {

        ArrayList<Stop> stops = new ArrayList<>();
        Stop stop1 = new Stop(1, "Stop 1", 3, 3, 2);
        Stop stop2 = new Stop(2, "Stop 2", 7, 4, 8);
        Stop stop3 = new Stop(3, "Stop 3", 9, 1, 15);
        Stop stop4 = new Stop(1, "Stop 1", 1, 14, 0);

        stops.add(stop1);
        stops.add(stop2);
        stops.add(stop3);
        stops.add(stop4);


        Route route1 = new Route(1, 1, "Route");
        Route route2 = new Route(2, 2, "Route2");
        Route route3 = new Route(3, 3, "Route3");
        Route route4 = new Route(4, 4, "Route4");
        ArrayList<Route> routes = new ArrayList<>();
        routes.add(route1);
        routes.add(route2);
        routes.add(route3);
        routes.add(route4);


        Bus bus1 = new Bus(1, route1, 7, 15, 15, 15);
        Bus bus2 = new Bus(2, route2, 7, 20, 25, 25);
        Bus bus3 = new Bus(3, route3, 7, 35, 45, 25);
        Bus bus4 = new Bus(4, route4, 7, 50, 0, 25);

        ArrayList<Bus> buses = new ArrayList<>();
        buses.add(bus1);
        buses.add(bus2);
        buses.add(bus3);
        buses.add(bus4);

        CalcEngine calc = new CalcEngine();
        Report report = calc.generateReport(stops, buses, 4, routes);
        GraphEngine graph = new GraphEngine(report);

        graph.generateGraph();
    }
}
