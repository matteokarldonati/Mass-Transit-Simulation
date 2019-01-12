package ui;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;
import com.lynden.gmapsfx.service.directions.*;
import com.lynden.gmapsfx.shapes.Polyline;
import com.lynden.gmapsfx.shapes.PolylineOptions;
import core.controller.SimulatorController;
import core.model.Bus;
import core.model.Route;
import core.model.Stop;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.net.URL;
import java.util.*;
import java.util.stream.IntStream;

import static ui.EditWindow.passengerData;
import static ui.EditWindow.simTxtFile;

public class SimWindow implements Initializable, MapComponentInitializedListener, DirectionsServiceCallback {

    //private List<Marker> markerList;

    // Selections for filtering
    private Map<Integer, Route> routeMap;
    private Map<Bus, Triangle> busMap;
    private Map<Marker, Stop> markerMap;
    private Map<Stop, InfoWindow> infoMap;
    private Map<Bus, InfoWindow> busInfoMap;
    private Map<Integer, Stop> stopMap;

    // Drop down menu items
    private ObservableList<String> routeList = FXCollections.observableArrayList("All Routes");

    private String currRouteSelection;
    private int currRouteID;
    private int stringPad;

    private boolean simLoaded = false;

    /* Block for displaying choice box */
    @FXML public ChoiceBox<String> routeSelect;

    /* Block for opening edit scene when clicking on Edit button */
    @FXML public Button editButton;
    private static Scene editScene;
    public static void setEditScene(Scene scene) {
        editScene = scene;
    }
    public void openEditScene(ActionEvent actionEvent) {
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setScene(editScene);
    }

    /* Block for opening analysis scene when clicking on Analysis button */
    @FXML public Button analysisButton;
    private static Scene analysisScene;
    public static void setAnalysisScene(Scene scene) {
        analysisScene = scene;
    }
    public void openAnalysisScene(ActionEvent actionEvent) {
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setScene(analysisScene);
    }

    /* Block for resetting simulation when clicking on reset button */
    @FXML public Button resetButton;
    public void resetSimulation() {
        if (EditWindow.sim != null) {
            EditWindow.sim = new SimulatorController(simTxtFile, passengerData);
            simLoaded = false;
            for (Map.Entry<Bus, Triangle> entry: busMap.entrySet()) {
                entry.getValue().setVisible(false);
            }
            routeMap = EditWindow.sim.getRoutes();
            busMap = new HashMap<>();
            markerMap = new HashMap<>();
            infoMap = new HashMap<>();
            busInfoMap = new HashMap<>();

            simulationStep();
        }
    }

    /* Block for advancing one step in the simulation when clicking on Step button */
    @FXML public Button stepButton;
    public void simulationStep() {
        if (EditWindow.sim != null) {
            ui.EditWindow.sim.nextEvent();
            //TODO: Implement code to display the taken step
            if (!simLoaded) {
                loadSimulation();
            }
            currRouteSelection = routeSelect.getValue();
            stringPad = currRouteSelection.lastIndexOf(":") + 2;
            currRouteSelection = currRouteSelection.substring(stringPad);
            if (routeMap == null) {
                routeMap = EditWindow.sim.getRoutes();
            }
            for (Map.Entry<Integer, Route> e : routeMap.entrySet()) {
                if (e.getValue().getName().equals(currRouteSelection)) {
                    currRouteID = e.getValue().getId();
                }
            }
            System.out.println("Test: selected route is: " + currRouteSelection);
            renderBuses();
            renderStops();
            renderRoutes();
        }
    }


    private void loadSimulation() {
        if (ui.EditWindow.sim != null) {
            simLoaded = true;
            for (Stop s: ui.EditWindow.sim.getStops().values()) {
                MarkerOptions mo = new MarkerOptions()
                        .position(new LatLong(s.getLatitude(),
                                s.getLongitude()))
                        .title(Integer.toString(s.getId()))
                        .label(s.getName().substring(0,3))
                        .visible(true);
                System.out.println("Lat:" + s.getLatitude() + " Long: " + s.getLongitude());

                InfoWindowOptions infoOptions = new InfoWindowOptions();
                infoOptions.content("<h2>No info yet...</h2>")
                        .position(new LatLong(s.getLatitude(),
                                s.getLongitude()));

                InfoWindow window = new InfoWindow(infoOptions);
                Marker marker = new Marker(mo);
                markerMap.put(marker, s);
                infoMap.put(s, window);

                map.addUIEventHandler(marker, UIEventType.click, (JSObject obj) -> {
                    window.open(map);
                });
            }
            for (Bus b: ui.EditWindow.sim.getBuses().values()) {
                // Temp code. still need to set colors
                Stop cs = b.getCurStop();
                LatLong location = new LatLong(cs.getLatitude(),
                        cs.getLongitude());
                Triangle t = new Triangle(location, map);
                busMap.put(b, t);

                InfoWindowOptions infoOptions = new InfoWindowOptions();
                infoOptions.content("<h2>No info yet...</h2>")
                        .position(new LatLong(cs.getLatitude(),
                                cs.getLongitude()));
                InfoWindow window = new InfoWindow(infoOptions);
                busInfoMap.put(b, window);

                map.addUIEventHandler(t, UIEventType.click, (JSObject obj) -> {
                    window.open(map);
                });
            }
        } else {
            System.out.println("null sim instance!");
        }
        for (Marker m: markerMap.keySet()) {
            map.addMarker(m);
        }
    }

    private void renderStops() {
        if (routeSelect.getValue().equals("All Routes")) {
            for (Marker mo : markerMap.keySet()) {
                mo.setVisible(true);
            }
        } else {
            for (Marker mo: markerMap.keySet()) {
                mo.setVisible(false);
            }
            for (Marker mo: markerMap.keySet()) {
                int[] routeList = EditWindow.sim.getRoutesOfStop(Integer.parseInt(mo.getTitle()));
                IntStream.range(0, routeList.length).filter(i -> routeList[i] == currRouteID).mapToObj(i -> true).forEach(b -> mo.setVisible(true));
            }
        }
        // Render stop clickable stop information
        for (Map.Entry<Stop, InfoWindow> i: infoMap.entrySet()) {
            Stop s = i.getKey();
            ArrayList<Bus> buses = new ArrayList<>();
            for (Bus b: busMap.keySet()) {
                if (b.getNextStop().equals(s)) {
                    buses.add(b);
                }
            }
            //StringBuilder sb = new StringBuilder();
            String sb = "";
            for (Bus b : buses) {
                sb += "Bus " + b.getId() + " arrives in: " + b.getArrivalTime() + " minutes";
            }
            i.getValue().setContent("<h2>" + s.getName()
                    + "</h2><h3>" + "Passengers waiting: "
                    + s.getPassengers() + "</h3><h4>"
                    + sb + "</h4>");
        }
    }

    private void renderBuses() {
        for (Map.Entry<Bus, Triangle> e: busMap.entrySet()) {
            Stop s = e.getKey().getCurStop();
            LatLong l = new LatLong(s.getLatitude(),
                    s.getLongitude());
            e.getValue().refreshTriangle(l);
        }
        // Filter through and show/hide
        if (routeSelect.getValue().equals("All Routes")) {
            // Set all busses to visible
            for (Map.Entry<Bus, Triangle> entry: busMap.entrySet()) {
                entry.getValue().setVisible(true);
            }
        } else {
            // Loop through and check if it is the current route
            for (Map.Entry<Bus, Triangle> entry: busMap.entrySet()) {
                // Set busses in the route to visible, hide others
                if (entry.getKey().getRoute().getName().equals(currRouteSelection)) {
                    entry.getValue().setVisible(true);
                } else {
                    entry.getValue().setVisible(false);
                }
            }
        }

//        corresponding routes/stops
//        time remaining of a bus on its current route
//        number of passengers on board

        for (Map.Entry<Bus, InfoWindow> e: busInfoMap.entrySet()) {
            InfoWindow i = e.getValue();
            Bus b = e.getKey();

            i.setContent("<h2>Bus: " + b.getId() + "</h2>"
             + "<h3>On route: " + b.getRoute().getName() + ", " + b.getArrivalTime() + " minutes remaining" + "</h3>"
            + "<h4>Passengers onboard: " + b.getPassengers() + "</h4>");
        }
    }

    private void renderRoutes() {
        if (!routeSelect.getValue().equals("All Routes")) {
            ArrayList<Marker> visibleMarkerList = new ArrayList<>();
            for (Marker mo: markerMap.keySet()) {
                if (mo.getVisible()) {
                    visibleMarkerList.add(mo);
                }

            }


            LatLong[] ary = new LatLong[visibleMarkerList.size()];
            int i = 0;
            for (Marker mo: visibleMarkerList) {
                if (markerMap.containsKey(mo)) {
                    Stop s = markerMap.get(mo);
                    ary[i++] = new LatLong(s.getLatitude(), s.getLongitude());
                }
            }
            MVCArray mvc = new MVCArray(ary);
            PolylineOptions polyOpts = new PolylineOptions()
                    .path(mvc)
                    .strokeColor("red")
                    .strokeWeight(2);

            Polyline poly = new Polyline(polyOpts);
            map.addMapShape(poly);

//        map.addUIEventHandler(poly, UIEventType.click, (JSObject obj) -> {
//            LatLong ll = new LatLong((JSObject) obj.getMember("latLng"));
////            System.out.println("You clicked the line at LatLong: lat: " + ll.getLatitude() + " lng: " + ll.getLongitude());
//        });
        }
    }

    protected DirectionsService directionsService;
    protected DirectionsPane directionsPane;

    protected StringProperty from = new SimpleStringProperty();
    protected StringProperty to = new SimpleStringProperty();
    protected DirectionsRenderer directionsRenderer = null;

    protected GoogleMap map;

    @FXML
    protected GoogleMapView mapView;

    @FXML
    protected TextField fromTextField;

    @FXML
    protected TextField toTextField;

    @FXML
    private void toTextFieldAction(ActionEvent event) {
        DirectionsRequest request = new DirectionsRequest(from.get(), to.get(), TravelModes.DRIVING, false);
        directionsRenderer = new DirectionsRenderer(true, map, directionsPane);
        directionsService.getRoute(request, this, directionsRenderer);
    }

    @FXML
    private void clearDirections(ActionEvent event) {
        directionsRenderer.clearDirections();
    }

    @Override
    public void directionsReceived(DirectionsResult results, DirectionStatus status) {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mapView.setKey("AIzaSyDzVfFnVZPXJI_cMCakk6AtvYqf1C48Xc0");
        mapView.addMapInitializedListener(this);

        if (EditWindow.sim != null) {
            routeMap = ui.EditWindow.sim.getRoutes();
            for (Map.Entry<Integer, Route> entry : routeMap.entrySet()) {
                routeList.add(Integer.toString(entry.getValue().getNumber()) +
                        ": " + entry.getValue().getName());
            }
        }
        routeSelect.setItems(routeList);
        routeSelect.setValue("All Routes");
    }

    @Override
    public void mapInitialized() {
        MapOptions options = new MapOptions();
        options.center(new LatLong(33.654082, -84.414632))
                .zoomControl(true)
                .zoom(12)
                .streetViewControl(false)
                .mapType(MapTypeIdEnum.ROADMAP);

        map = mapView.createMap(options);
        directionsService = new DirectionsService();
        directionsPane = mapView.getDirec();

        //markerList = new ArrayList<>();
        busMap = new HashMap<>();
        markerMap = new HashMap<>();
        infoMap = new HashMap<>();
        busInfoMap = new HashMap<>();
    }

    @FXML
    public void updateDropDown() {
        if (EditWindow.sim != null && routeList.size() != EditWindow.sim.getRoutes().size() + 1) {
            routeMap = EditWindow.sim.getRoutes();
            for (Map.Entry<Integer, Route> entry : routeMap.entrySet()) {
                routeList.add(Integer.toString(entry.getValue().getNumber()) +
                        ": " + entry.getValue().getName());
            }
            routeSelect.setItems(routeList);
            System.out.println("Updated");
        }
    }
}