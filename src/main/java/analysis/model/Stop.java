package analysis.model;

public class Stop {
    private final int id;
    private final String name;
    private final double longitude;
    private final double latitude;
    private int passengers;

    public Stop(int id, String name, double longitude, double latitude, int passengers) {
        this.id = id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.passengers = passengers;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }
}
