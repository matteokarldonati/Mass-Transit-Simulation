package core.controller;

import analysis.model.Report;
import analysis.service.GraphEngine;
import analysis.service.ReportHandler;
import core.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import analysis.service.CalcEngine;

/**
 * Controls the logic Simulation.
 * Each instance corresponds to a different simulation.
 * Resetting the simulation can be done by replacing the simulator by a new one.
 */
public class SimulatorController {
    private final String id = LocalDateTime.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("uuuuMMdd HHmmss"));

    // Simulation data structures
    private final Map<Integer, Stop> stops = new HashMap<>();
    private final Map<Integer, Route> routes = new HashMap<>();
    private final Map<Integer, Bus> buses = new HashMap<>();
    private final PriorityQueue<Event> events = new PriorityQueue<>(1, Comparator.comparingInt(Event::getTime));

    private final Map<Integer, Report> reports = new HashMap<>();
    private final CalcEngine calcEngine = new CalcEngine();
    private final ReportHandler handler = new ReportHandler(id);

    private final Random random = new Random();

    /**
     * Creates a Controller, the required models, and starts the simulation
     * @param scenarioFile      File describing the bus network
     * @param statisticsFile    File describing the passenger behaviour
     */
    public SimulatorController(File scenarioFile, File statisticsFile) {
        parseScenario(scenarioFile);
        parsePassengerData(statisticsFile);
    }

    private int randomBetween(int low, int high) {
        return low + random.nextInt(high-low+1);
    }

    private int getPassengerArrival(PassengerData values) {
        return randomBetween(values.getArrivalLow(), values.getArrivalHigh());
    }

    private int getPassengerDepart(PassengerData values) {
        return randomBetween(values.getDepartLow(), values.getDepartHigh());
    }

    private int getPassengerOn(PassengerData values) {
        return randomBetween(values.getOnLow(), values.getOnHigh());
    }

    private int getPassengerOff(PassengerData values) {
        return randomBetween(values.getOffLow(), values.getOffHigh());
    }

    /**
     * Gets the next event (or next set of events if happening simultaneously) and executes them.
     * Prints to standard output the state of the buses at each move
     * @return Time of the executed event, or -1 if no events are left (error)
     */
    public int nextEvent() {
        // Error if no events left
        if (events.size() == 0) {
            throw new Error("No events left");
        }

        //
        final Event currentEvent = events.poll();
        final int time = currentEvent.getTime();
        switch(currentEvent.getType()) {
            case move_bus:
                final Bus bus = buses.get(currentEvent.getId());
                bus.moveBus(time);
                movePassengers(bus);

                // Update View
                buses.values().forEach((Bus b)->{
                    if(bus.getId()==b.getId()) {
                        System.out.println("->\t"+b);
                    } else {
                        System.out.println("\t"+b);
                    }
                });
                System.out.println();
                events.add(new Event(bus.getArrivalTime(), EventType.move_bus, bus.getId()));
                break;
            default:
                System.out.println("Event problem");
                break;
        }
        int id = currentEvent.getId();
        ArrayList<Stop> stopList = new ArrayList<>(stops.values());
        ArrayList<Bus> busesList = new ArrayList<>(buses.values());
        ArrayList<Route> routesList = new ArrayList<>(routes.values());

        Report report = calcEngine.generateReport(stopList, busesList, id, routesList);
        reports.put(currentEvent.getId(), report);

        ArrayList<Report> reportsList = new ArrayList<Report>(reports.values());
        double currentEffectiveness = report.getEffectiveness();
        int averageWaitingTime = calcEngine.computeAverageWaitingTime(reportsList);

        GraphEngine graphEngine = new GraphEngine(report);
        graphEngine.generateGraph();
        return time;
    }

    private void movePassengers(Bus bus) {
        // Get the passenger values
        final Stop stop = bus.getCurStop();
        int busPassengers = bus.getPassengers();
        final int busCapacity = bus.getCapacity();
        int stopPassengers = stop.getPassengers();
        final PassengerData statistics = stop.getPassengerData();


        // Move the passengers
        // Passengers arriving to the stop
        stopPassengers += getPassengerArrival(statistics);

        // Bus passengers getting off
        final int passengersOff = Math.max(   Math.min(busPassengers, getPassengerOff(statistics)), //Only passengers in the bus can get off
                                        busPassengers-busCapacity);     // But there shouldn't be more passengers than the capacity (in case of change)

        // New passengers getting on
        final int passengersOn = Math.min(    Math.min(getPassengerOn(statistics), stopPassengers), // Only passengers at the stop can get on
                                        busCapacity-busPassengers);     // But not too much (still for the capacity)

        busPassengers += passengersOn - passengersOff;
        stopPassengers += passengersOff - passengersOn;

        // Passengers leaving the system
        stopPassengers -= Math.min(getPassengerDepart(statistics), stopPassengers);   // Only passengers at the stop can leave

        bus.setPassengers(busPassengers);
        stop.setPassengers(stopPassengers);
    }

    private void parseScenario(File file) {
        final String DELIMITER = ",";
        try {
            final Scanner takeCommand = new Scanner(file);
            while (takeCommand.hasNextLine()) {
                final String userCommandLine = takeCommand.nextLine();
                final String[] tokens = userCommandLine.split(DELIMITER);
                switch (tokens[0]) {
                    case "add_event": {
                        /*the following assumes that events are of the form: add_event,int,move_bus,int*/
                        events.add(new Event(Integer.parseInt(tokens[1]),EventType.valueOf(tokens[2]),Integer.parseInt(tokens[3])));
                        System.out.print(" new event - rank: " + Integer.parseInt(tokens[1]));
                        System.out.println(" type: " + tokens[2] + " ID: " + Integer.parseInt(tokens[3]) + " created");
                        break;
                    }
                    case "add_stop": {
                        stops.put(Integer.parseInt(tokens[1]),
                                new Stop(Integer.parseInt(tokens[1]), tokens[2], Integer.parseInt(tokens[3]), Double.parseDouble(tokens[4]), Double.parseDouble(tokens[5])));
                        System.out.println(" new stop: " + tokens[1] + " created");
                        break;
                    }
                    case "add_route": {
                        routes.put(Integer.parseInt(tokens[1]), new Route(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), tokens[3]));
                        System.out.println(" new route: " + tokens[1] + " created");
                        break;
                    }
                    case "add_bus": {
                        buses.put(Integer.parseInt(tokens[1]), new Bus(Integer.parseInt(tokens[1]), routes.get(Integer.parseInt(tokens[2])), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]), Integer.parseInt(tokens[5]), Integer.parseInt(tokens[6])));
                        System.out.println(" new bus: " + tokens[1] + " created");
                        break;
                    }
                    case "extend_route": {
                        routes.get(Integer.parseInt(tokens[1])).add(stops.get(Integer.parseInt(tokens[2])));
                        System.out.println(" stop: " + Integer.parseInt(tokens[2]) + " appended to route " + Integer.parseInt(tokens[1]));
                        break;
                    }
                    default: {
                        System.out.println(" command not recognized");
                    }
                }
            }
            takeCommand.close();
        }
        catch (IOException e) {
            System.err.println("IO Exception when trying to parse Scenario file : "+e.getMessage());
            e.printStackTrace();
            throw new Error();  // Stop the program
        }
    }

    private void parsePassengerData(File file) {
        final String regex = ",";
        try {
            final Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                final int[] commands = Arrays.stream(reader.nextLine().split(regex)).mapToInt(Integer::parseInt).toArray();
                if (stops.get(commands[0]) != null) {
                    stops.get(commands[0]).setPassengerData(new PassengerData(commands[1], commands[2], commands[3], commands[4], commands[5], commands[6], commands[7], commands[8]));
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.err.println("Statistical file not found. Error : "+e.getMessage());
            e.printStackTrace();
            throw new Error();  // Stop the program
        }
    }

    /**
     * Changes the speed of a bus as soon as it reaches its next stop (waits even if the bus is currently at a stop)
     * @param busId     ID of the bus
     * @param newSpeed  New speed. Must be strictly positive
     */
    public void changeSpeed(int busId, int newSpeed) {
        final Bus target = buses.get(busId);

        if (target == null) {
            System.out.println("Error: invalid bus id");
            return;
        }

        if (newSpeed <= 0) {
            System.out.println("Error: invalid new speed");
            return;
        }

        target.setSpeed(newSpeed);
        System.out.println("New speed will be applied as bus arrives to its next stop");
    }

    /**
     * Changes the capacity of a bus as soon as it reaches its next stop (waits even if the bus is currently at a stop)
     * @param busId         ID of the bus
     * @param newCapacity   New capacity. Must be positive.
     */
    public void changeCapacity(int busId, int newCapacity) {
        final Bus target = buses.get(busId);

        if (target == null) {
            System.out.println("Error: invalid bus id");
            return;
        }

        if (newCapacity < 0) {
            System.out.println("Error: invalid new speed");
            return;
        }

        target.setCapacity(newCapacity);
        System.out.println("New capacity will be applied as bus arrives to its next stop");
    }

    /**
     * Gives the buses
     * @return The buses map
     */
    public Map<Integer, Bus> getBuses() {
        return Collections.unmodifiableMap(buses);
    }

    /**
     * Gives the routes
     * @return The routes map
     */
    public Map<Integer, Route> getRoutes() {
        return Collections.unmodifiableMap(routes);
    }

    /**
     * Gives the stops
     * @return The stops map
     */
    public Map<Integer, Stop> getStops() {
        return Collections.unmodifiableMap(stops);
    }

    // For testing purposes
    public PriorityQueue<Event> getEvents() {
        return new PriorityQueue<>(events);
    }

    /**
     * Returns the ids of the routes that contains the stop whose id is given as parameter
     * WARNING : Highly inefficient
     * @param id id of the stop
     * @return id of the routes containing this stop
     */
    public int[] getRoutesOfStop(int id) {
        return getRoutes().values().parallelStream().filter(r->r.stream().anyMatch(s->s.getId()==id)).mapToInt(Route::getId).toArray();
    }

}