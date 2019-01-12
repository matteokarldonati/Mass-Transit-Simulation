package ui;

import core.controller.SimulatorController;
import core.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.soap.Text;
import java.awt.*;
import java.io.File;
import java.io.*;
import java.net.URL;
import java.util.*;

public class EditWindow {

    public static File passengerData = null;
    public static File simTxtFile = null;
    public static core.controller.SimulatorController sim; //this is the main simulatorController that was
        // created in the core team's main method (in the commented out code section)
    public Map<Integer, Route> routeMap;
    public ObservableList<String> routeList = FXCollections.observableArrayList("All Routes");

    @FXML
    public ImageView editBusButton;
    public ImageView editStopButton;
    public ChoiceBox<String> routeSelectDropDown;

    /* Block for switching to SimulationWindow the simulation when clicking on Simulation button */
    @FXML public Button simButton;
    private static Scene simScene;
    public static void setSimScene(Scene scene) {
        simScene = scene;
    }
    public void openSimScene(ActionEvent actionEvent) {
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setScene(simScene);
    }

    /* Block for switching to AnalysisWindow the simulation when clicking on Analysis button */
    @FXML public Button analysisButton;
    private static Scene analysisScene;
    public static void setAnalysisScene(Scene scene) {
        analysisScene = scene;
    }
    public void openAnalysisScene(ActionEvent actionEvent) {
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setScene(analysisScene);
    }

    /* Block for loading the simulation when clicking on Load button */
    public void loadSimulation() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Simulation File");
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            simTxtFile = selectedFile;
            simLoadConfirmTxt(simTxtFile);
            if (passengerData != null) { //only create simulator object when both files are loaded
                sim = new SimulatorController(simTxtFile, passengerData);
                // Import into lists
                if (sim != null) {
                    routeMap = sim.getRoutes();
                    for (Map.Entry<Integer, Route> entry : routeMap.entrySet()) {
                        routeList.add(Integer.toString(entry.getValue().getNumber()) +
                                ": " + entry.getValue().getName());
                    }
                    routeSelectDropDown.setItems(routeList);
                }
            }
        }
    }

    /* Block for loading the passenger data when clicking on Load button */
    @FXML public Button loadPsgDataButton;
    public void loadPassengerData() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Passenger Data");
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            passengerData = selectedFile; //fileChooser.getSelectedFile();
            passLoadConfirmTxt(passengerData);
            if (simTxtFile != null) { //only create simulator object when both files are loaded
                sim = new SimulatorController(simTxtFile, passengerData);
                // Import into lists
                if (sim != null) {
                    routeMap = sim.getRoutes();
                    for (Map.Entry<Integer, Route> entry : routeMap.entrySet()) {
                        routeList.add(Integer.toString(entry.getValue().getNumber()) +
                                ": " + entry.getValue().getName());
                    }
                    routeSelectDropDown.setItems(routeList);
                }
            }
        }

    }

    @FXML public javafx.scene.text.Text simulationTxt;
    public void simLoadConfirmTxt(File simTxtFile) {
        String[] splitFileName = simTxtFile.getAbsolutePath().split("\\\\");
        int len = splitFileName.length;
        String filename = splitFileName[len - 1];
        String confirmation = "\u2714";// + " " + filename; took this out, doesn't work on non-windows os
        simulationTxt.setText(confirmation);
    }

    @FXML public javafx.scene.text.Text passengerTxt;
    public void passLoadConfirmTxt(File passengerData) {
        String[] splitFileName = passengerData.getAbsolutePath().split("\\\\");
        int len = splitFileName.length;
        String filename = splitFileName[len - 1];
        String confirmation = "\u2714";// + " " + filename; took this out, doesn't work on non-windows os
        passengerTxt.setText(confirmation);
    }

    /* Block for saving the running simulation when clicking on Save button */
    @FXML public Button saveButton;
    public void saveSimulation() {
        // the stops/routes the same, the bus with the stop it's at currently
        // and its time relative to the time of the first event on the queue, and events with the bus moving
        // relative to the time of the first event on the queue

        // Create new user saved file and check if it already exists
        File save;
        FileWriter fr = null;
        BufferedWriter br = null;
        System.out.println("Saving file");

        try {
            save = new File("userScenario.txt");
            int n = 2;
            while (!save.createNewFile()) {
                save = new File("userScenario" + n + ".txt");
                n++;
            }

            // Make sure don't overwrite an existing file
            //int num = 2;
            //while (save.exists()) {
            //    save = File.createTempFile("userScenario" + num, ".txt");
            //    System.out.println("file exists");
           // }
            System.out.println("File created");

            fr = new FileWriter(save);
            br = new BufferedWriter(fr);

            // Write stops
            Map<Integer, core.model.Stop> m = sim.getStops();
            System.out.println("Saving stops");
            for (int x : m.keySet()) {
                core.model.Stop stop = m.get(x);
                br.write("add_stop," + stop.getId() + "," + stop.getName() + "," + stop.getPassengers() + "," + stop.getLatitude() + "," + stop.getLongitude());
                br.newLine();
            }

            // Write routes
            Map<Integer, core.model.Route> m2 = sim.getRoutes();
            System.out.println("Saving routes");
            for (int x : m2.keySet()) {
                core.model.Route route = m2.get(x);
                br.write("add_route," + route.getId() + "," + route.getNumber() + "," + route.getName());
                br.newLine();
            }

            // Write extends for each route
            System.out.println("Saving extends");
            for (int x : m2.keySet()) {
                core.model.Route route = m2.get(x);
                for (core.model.Stop stop : route) {
                    br.write("extend_route," + route.getId() + "," + stop.getId());
                    br.newLine();
                }
            }

            // Write buses
            Map<Integer, core.model.Bus> m3 = sim.getBuses();
            System.out.println("Saving buses");
            for (int x : m3.keySet()) {
                core.model.Bus bus = m3.get(x);
                int id = bus.getRoute().getId();
                int loc = bus.getRoute().indexOf(bus.getCurStop());
                br.write("add_bus," + bus.getId() + "," + id + "," + loc + "," + bus.getPassengers() + "," + bus.getCapacity() + bus.getSpeed());
                br.newLine();
            }

            // Write events
            PriorityQueue<core.model.Event> m4 = sim.getEvents();
            int time = m4.peek().getTime();
            while (!m4.isEmpty()) {
                core.model.Event event = m4.poll();
                int adjTime = event.getTime() - time;
                br.write("add_event," + adjTime + ",move_bus," + event.getId());
                br.newLine();
            }

            //Done
            System.out.println("Done saving");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                fr.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* Block for creating a new route */
    @FXML public Button newRouteButton;
    public void editRouteSelected() {
        editBusButton.setVisible(true);
        editStopButton.setVisible(true);
    }
}
