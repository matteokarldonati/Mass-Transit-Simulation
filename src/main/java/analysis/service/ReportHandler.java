package analysis.service;

import analysis.model.Report;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ReportHandler {
    private String simulationId;
    private File file;

    public ReportHandler(String simulationId) {
        this.simulationId = simulationId;
        file = new File("src/main/java/analysis/reports/" + simulationId + ".txt");
    }

    public Map<Integer, Report> importReports(String filepath) {
        HashMap<Integer, Report> reports = new HashMap<>();
        try(Scanner in = new Scanner(filepath)) {
            while (in.hasNextLine()) {
                String[] reportArray = in.nextLine().split(",");
                int id = Integer.parseInt(reportArray[0]);
                int waitingTime = Integer.parseInt(reportArray[1]);
                int busCost = Integer.parseInt(reportArray[2]);
                double effectiveness = Double.parseDouble(reportArray[3]);
                reports.put(id, new Report(waitingTime, busCost, effectiveness, id));
            }
        }
        return reports;
    }

    public ArrayList<Report> retrieveReports(Map<Integer, Report> reports, int first, int second) {
        ArrayList<Report> reportList = new ArrayList<>();

        for (int i = first; i <= second; i++) {
            reportList.add(reports.get(i));
        }

        return reportList;
    }

    public void exportReport(Map<Integer, Report> reports) {
        try (PrintWriter out = new PrintWriter(file)) {
            for (Integer currentKey : reports.keySet()) {
                out.println(reports.get(currentKey).getId() + "," +
                        reports.get(currentKey).getWaitingTime() + "," +
                        reports.get(currentKey).getBusCost() + "," +
                        reports.get(currentKey).getEffectiveness());
            }

        } catch (IOException exception) {
            System.out.println(exception.getStackTrace());
        }
    }

}
