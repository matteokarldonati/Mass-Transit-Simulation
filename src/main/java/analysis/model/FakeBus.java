package analysis.model;

public class FakeBus {
    private final int id;
    private int curStop;
    private int passengers;
    private int capacity;
    private int speed;
    private int time;

    public FakeBus(int id, int curStop, int passengers, int capacity, int speed) {
        this.id = id;
        this.curStop = curStop;
        this.passengers = passengers;
        this.capacity = capacity;
        this.speed = speed;
    }

    public int getId() {
        return id;
    }

    public int getCurStop() {
        return curStop;
    }

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}

