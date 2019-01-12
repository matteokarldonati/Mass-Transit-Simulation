package database;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.*;


/**
 * Created on 12/1/18.
 */
public class DataParser {

    public static void main(String args[]) {
        parseRoutes();
        parseStops();
        //parseAPC();
    }

    /**
     * Parse route data
     */
    private static void parseRoutes() {
        String filePath = "./src/main/resources/routes.txt";
        BufferedReader br = null;
        PrintWriter writer = null;
        String DELIMITTER = ",";
        String inputLine = "";

        try {
            //Read file
            br = new BufferedReader(new FileReader(filePath));
            writer = new PrintWriter("add_route_commands.txt", "UTF-8");
            while ((inputLine = br.readLine()) != null) {

                String[] fields = inputLine.split(DELIMITTER);
                //route_id,route_short_name,route_long_name,route_desc,route_type,
                // route_url,route_color,route_text_color

                String id = fields[0];
                String number = fields[1];
                String name = fields[2];

                String outputLine = "add_route," + id + "," + number + "," + name;
                System.out.println(outputLine);
                // write to file
                writer.println(outputLine);
            }
            //
            //
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO exception");
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Parse stop data
     */
    private static void parseStops() {
        String filePath = "./src/main/resources/stops.txt";
        BufferedReader br = null;
        PrintWriter writer = null;
        String DELIMITTER = ",";
        String inputLine = "";

        try {
            //Read file
            br = new BufferedReader(new FileReader(filePath));
            writer = new PrintWriter("add_stop_commands.txt", "UTF-8");
            while ((inputLine = br.readLine()) != null) {

                String[] fields = inputLine.split(DELIMITTER);
                //stop_id,stop_code,stop_name,stop_lat,stop_lon

                String id = fields[0];
                String name = fields[2];
                String riders = "0";
                String lat = fields[3];
                String lon = fields[4];

                String outputLine = "add_stop," + id + "," + name + "," + riders + "," + lat + "," + lon;
                System.out.println(outputLine);
                //Write to file
                writer.println(outputLine);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO exception");
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Parse buses, events, and extend route
     * Also get passenger data
     */
    private static void parseAPC() {
        String csvFile = "./src/main/resources/apcdata_week.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        boolean isFirstLine = true;
        PrintWriter route_writer = null;
        PrintWriter bus_writer = null;
        PrintWriter event_writer = null;
        PrintWriter passenger_writer = null;

        // Track a stop's on and off highest values
        HashMap<Integer, Integer> onHighs = new HashMap<>();
        HashMap<Integer, Integer> offHighs = new HashMap<>();

        try {
            HashMap<Integer, ArrayList<Integer>> map = new HashMap<>(); // Map route to list of stops in it
            HashMap<Integer, Integer> buses = new HashMap<>();                   // Buses seen while parsing, logical time
            HashMap<Integer, ArrayList<Integer>> routeBuses = new HashMap<>(); // Map routes to buses running it
            route_writer = new PrintWriter("add_extend_commands.txt", "UTF-8");
            bus_writer = new PrintWriter("add_bus_commands.txt", "UTF-8");
            event_writer = new PrintWriter("add_event_commands.txt", "UTF-8");
            passenger_writer = new PrintWriter("add_passenger_commands.txt", "UTF-8");

            // Read file
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                // use comma as separator
                String[] fields = line.split(cvsSplitBy, -1);

                //calendar_day,route,route_name,direction,stop_id,stop_name,arrival_time,departure_time,
                // ons,offs,latitude,longitude,vehicle_number
                int route = Integer.parseInt(fields[1]);
                int stop = Integer.parseInt(fields[4]);
                int ons = 0;
                int offs = 0;
                try {
                    ons = Integer.parseInt(fields[8]);
                } catch (Exception e) {}
                try {
                    offs = Integer.parseInt(fields[9]);
                } catch (Exception e) {}
                int bus_num = Integer.parseInt(fields[12]);

                // add new bus to route list
                ArrayList<Integer> busList = routeBuses.get(route);
                if (busList == null) {
                    busList = new ArrayList<Integer>();
                }
                if (!busList.contains(bus_num)) {
                    busList.add(bus_num);
                    routeBuses.put(route, busList);
                }

                // extend route
                ArrayList<Integer> list = map.get(route);
                if (list == null) {
                    list = new ArrayList<Integer>();
                }
                // Only add stop to route if not added already and update map
                if (!list.contains(stop)) {
                    list.add(stop);
                    map.put(route, list);
                    String extend = "extend_route," + route + "," + stop;

                    // Write to file
                    route_writer.println(extend);
                }

                // add buses if not already added
                if (buses.get(bus_num) == null) {
                    // first bus on route
                    String addBus = "";
                    if (busList.size() == 1) {
                        buses.put(bus_num, 0);
                        addBus = "add_bus," + bus_num + "," + route + ",0,0,30,20";
                    } else {
                        // Other buses on route then update time and get stop number
                        buses.put(bus_num, buses.get(busList.get(0)));
                        int stopNum = map.get(route).indexOf(stop);
                        addBus = "add_bus," + bus_num + "," + route + "," + stopNum + ",0,30,20";
                    }

                    // Write to file for bus and event
                    bus_writer.println(addBus);

                    int time = buses.get(bus_num);
                    String eventString = "add_event," + time + ",move_bus," + bus_num;
                    event_writer.println(eventString);
                } else {
                    // add events and increment time
                    int time = buses.get(bus_num);
                    buses.put(bus_num, time + 1);

                    // increment time for other buses on same route
                    ArrayList<Integer> others = routeBuses.get(route);
                    for (int b : others) {
                        if (b != bus_num) {
                            buses.put(b, buses.get(b) + 1);
                        }
                    }
                }

                // update highs and lows
                try {
                    int onHigh = onHighs.get(stop);
                    if (ons > onHigh) {
                        onHighs.put(stop, ons);
                    }
                } catch (Exception e) {
                    onHighs.put(stop, 0);
                }
                try {
                    int offHigh = offHighs.get(stop);
                    if (offs > offHigh) {
                        offHighs.put(stop, offs);
                    }
                } catch (Exception e) {
                    offHighs.put(stop, 0);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO exception");
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                    route_writer.close();
                    bus_writer.close();
                    event_writer.close();

                    // Write to statistic file
                    for (int num : onHighs.keySet()) {
                        int onHigh = onHighs.get(num);
                        int offHigh = offHighs.get(num);
                        String stopStat = num + "," + onHigh + ",0," + offHigh + ",0," + onHigh + ",0," + offHigh + ",0";
                        passenger_writer.println(stopStat);
                    }
                    passenger_writer.close();

                    System.out.println("Done");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
