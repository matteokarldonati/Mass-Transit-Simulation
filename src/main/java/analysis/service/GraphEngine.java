package analysis.service;

import analysis.model.Report;
import core.model.Route;
import core.model.Stop;
// import org.apache.commons.lang3.SystemUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class GraphEngine {
    private Report report;
    private double stdDeviation;
    private final String RED = "#FF0000";
    private final String ORANGE = "#FFA500";
    private final String YELLOW = "#FFFF00";
    private final String GREEN = "#00FF00";

    public GraphEngine(Report report) {
        this.report = report;
        this.calculateDeviation();
    }

    public void generateGraph() {
        try (PrintWriter out = new PrintWriter("graph.dot")) {

            out.println("digraph G {");
            int stopCounter = 0;

            for (Route currentRoute : report.getRoutes()) {
                out.println("  subgraph cluster" + currentRoute.getNumber() + " {");
                ArrayList<Integer> stopGraphList = new ArrayList<>();

                for (Stop currentStop : currentRoute) {

                    String color = "000000";
                    double avgWait = (double) report.getWaitingTime() / report.getStops().size();
                    if (currentStop.getPassengers() <= (avgWait - stdDeviation) || currentStop.getPassengers() <= 1) {
                        color = GREEN;
                    } else if (currentStop.getPassengers() > (avgWait - stdDeviation) && currentStop.getPassengers() <= (avgWait)) {
                        color = YELLOW;
                    } else if (currentStop.getPassengers() > avgWait && currentStop.getPassengers() <= (avgWait + stdDeviation)) {
                        color = ORANGE;
                    } else {
                        color = RED;
                    }


                    stopGraphList.add(stopCounter);
                    out.println("    stop" + stopCounter++ + " [ label=\"stop#" + currentStop.getId() + " | " + currentStop.getPassengers() + " waiting\", color=\"" + color + "\"];");

                }
                for (int i = 0; i < stopGraphList.size() - 1; i++) {
                    out.println("    stop" + stopGraphList.get(i) + " -> stop" + stopGraphList.get(i + 1) + ";");
                }


                out.println("    label = \"Route #" + currentRoute.getNumber() + "\";");
                out.println(("  }"));
            }

            out.println("}");

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void calculateDeviation() {
        double avgWait = (double) report.getWaitingTime() / report.getStops().size();
        double stdDeviation = 0.0;
        for (Stop currentStop : report.getStops()) {
            stdDeviation += Math.pow((currentStop.getPassengers() - avgWait),2);
        }
        stdDeviation /= report.getStops().size();
        this.stdDeviation = Math.sqrt(stdDeviation);
    }



}
