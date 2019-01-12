package analysis.service;

import core.model.Bus;
import core.model.Stop;
import core.model.Route;
import analysis.model.Report;


import java.util.ArrayList;

public class CalcEngine {

    private double timeWeight;
    private double busWeight;
    private static double currentEffectiveness;

    public CalcEngine() {
        this.timeWeight = 0.5;
        this.busWeight = 0.5;
    }

    public void setWeights(double timeWeight, double busWeight) {
        this.timeWeight = timeWeight;
        this.busWeight = busWeight;
    }

    public int computeWaitingTime(ArrayList<Stop> stops){ // snapshot approach
        if (stops == null || stops.isEmpty()) {
            throw new IllegalArgumentException("The list of stops cannot be null and must have at least 1 stop in it.");
        }

        int result = 0;
        for (Stop stop : stops) {
            result += stop.getPassengers();
        }
        return result;
    }

    public int computeAverageWaitingTime(ArrayList<Report> reports){
        int result = 0;
        for (Report report : reports) {
            result += report.getWaitingTime();
        }
        result /= reports.size();
        return result;
    }

    public int computeBusCost(ArrayList<Bus> buses){
        if (buses == null) {
            throw new IllegalArgumentException("The list of buses cannot be null");
        }

        int result = 0;
        for (Bus bus : buses) {
            result += bus.getSpeed() * bus.getCapacity();
        }
        return result;
    }

    private double computeCurrentEffectiveness(ArrayList<Stop> stops, ArrayList<Bus> buses) {
        currentEffectiveness =  (this.busWeight * this.computeBusCost(buses) + this.timeWeight * this.computeWaitingTime(stops));
        return currentEffectiveness;
    }

    public static double getCurrentEffectiveness(){ //this is not how a getter works lol, I will change this - Tim
        return currentEffectiveness;
    }

    private double computePeriodEffectiveness(ArrayList<Report> reports) {
        double effectiveness = 0;

        for (Report report : reports) {
            effectiveness += report.getEffectiveness();
        }

        effectiveness = effectiveness / reports.size();
        return effectiveness;
    }

    public double getPeriodEffectiveness(ArrayList<Report> reports){
        return this.computePeriodEffectiveness(reports);
    }

    public Report generateReport(ArrayList<Stop> stops, ArrayList<Bus> buses, int id, ArrayList<Route> routes) {
        return new Report(computeWaitingTime(stops), computeBusCost(buses), computeCurrentEffectiveness(stops, buses), id, stops, routes);
    }
}