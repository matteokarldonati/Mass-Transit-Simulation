package analysis.model;

import java.util.ArrayList;
import core.model.Stop;
import core.model.Route;

public class Report {
    private final int id;
    private final int waitingTime;
    private final int busCost;
    private final double effectiveness;
    private final ArrayList<Stop> stops;
    private final ArrayList<Route> routes;

    public Report(int waitingTime, int busCost, double effectiveness, int simulationID, ArrayList<Stop> stops, ArrayList<Route> routes) {
        this.id = simulationID;
        this.waitingTime = waitingTime;
        this.busCost = busCost;
        this.effectiveness = effectiveness;
        this.stops = stops;
        this.routes = routes;
    }

    public Report(int waitingTime, int busCost, double effectiveness, int id) {
        this.id = id;
        this.busCost = busCost;
        this.effectiveness = effectiveness;
        this.waitingTime = waitingTime;
        this.stops = null;
        this.routes = null;
    }

    public int getId() { return id; }

    public int getWaitingTime() {
        return waitingTime;
    }

    public int getBusCost() {
        return busCost;
    }

    public double getEffectiveness() {
        return effectiveness;
    }

    public ArrayList<Stop> getStops() {
        return stops;
    }

    public ArrayList<Route>  getRoutes() {
        return routes;
    }
}