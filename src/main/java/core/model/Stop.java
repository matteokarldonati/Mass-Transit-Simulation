package core.model;

import java.util.Objects;

/**
 * Bus stop where passengers wait for a bus
 */
public class Stop {

    private final int id;
    public int getId() {
        return id;
    }

    private final String name;
    public String getName() {
        return name;
    }

    private final double longitude;
    public double getLongitude() {
        return longitude;
    }

    private final double latitude;
    public double getLatitude() {
        return latitude;
    }

    private int passengers;
    public int getPassengers() {
        return passengers;
    }
    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }

    private PassengerData passengerData = null;
    public PassengerData getPassengerData() {
        return passengerData;   // null if hasn't been set yet
    }
    public void setPassengerData(PassengerData passengerData) {
        this.passengerData = Objects.requireNonNull(passengerData);
        // Cannot be set back to null
    }

    /**
     * Creates a new Stop
     *
     * @param id            Stop ID
     * @param name          Stop name
     * @param passengers    N° of initial passengers
     * @param latitude      Stop location (latitude)
     * @param longitude     Stop location (longitude)
     */
    public Stop(int id, String name, int passengers, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.passengers = passengers;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Computes the distance between this stop to the other stop
     *
     * @param that The other stop
     * @return The distance bbetween both stops (in miles)
     */
    public double distanceTo(Stop that) {
        double lonDiff = longitude - that.longitude;
        double latDiff = latitude - that.latitude;
        double degreesDist = Math.sqrt((lonDiff * lonDiff) + (latDiff * latDiff));
        return 70 * degreesDist;  // To have the distance in miles (see pdf)
    }

    /**
     * Gives a description of the stop, including its id, name and current n° of passengers
     * @return  The description
     */
    @Override
    public String toString() {
        return "corelogic.models.Stop "+id+": "+name+", "+passengers+" passengers.";
    }
}
