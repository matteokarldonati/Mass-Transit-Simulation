package core.model;

/**
 * Simulated bus
 */
public class Bus {
    private final int id;
    private final Route route;
    private int curStop;
    private int passengers;
    private int capacity;
    private int speed;
    private int curTime;                // When it arrived to curStop
    private boolean didStart = false;   // Whether it arrived to its first stop (first move_bus statement)
    private String description;         // Bus String description

    /**
     * Creates a bus instance
     * @param id            Bus ID
     * @param route         Route the bus is following
     * @param curStop       Location of the bus at creation
     * @param passengers    N° of passengers in the bus at creation
     * @param capacity      Capacity of the bus at creation
     * @param speed         Bus speed when created
     */
    public Bus(int id, Route route, int curStop, int passengers, int capacity, int speed) {
        this.id = id;
        this.route = route;
        this.curStop = curStop;
        this.passengers = passengers;
        this.capacity = capacity;
        this.speed = speed;
        curTime = 0;
        setDescription();
    }


    // MARK : Getters and Setters

    public int getId() {
        return id;
    }

    public Route getRoute() {
        return route;
    }

    public Stop getCurStop() {
        return route.get(curStop);
    }
    public int getCurStopNum() {
        return curStop;
    } //For testing purposes

    public Stop getNextStop() {
        if (didStart) {
            if (curStop+1>=route.size()) {
                return route.get(0);
            } else {
                return route.get(curStop+1);
            }
        } else {
            return getCurStop();
        }
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

    /**
     * Moves the bus to its next stop
     * @param time  Time at which the bus arrives at its next stop.
     */
    public void moveBus(int time) {
        // Bus arrives
        curTime = time;

        if (didStart) {
            // At next stop
            curStop += 1;
            if (curStop >= route.size()) {
                curStop = 0;
            }
        } else {
            // At first stop
            didStart = true;
        }
        setDescription();
    }

    /**
     * Computes when the bus will reach its next stop
     * @return  Arrival time of the bus
     */
    public int getArrivalTime() {
        if (didStart) {
            // Using the formula in the Requirements pdf
            int timeToNext = 1 + ((int)getCurStop().distanceTo(getNextStop()) * 60 / getSpeed());
            return curTime + timeToNext;
        } else {
            return 0;
        }

    }

    private void setDescription() {
        if (didStart) {
            description = "Bus:" + id + " || Current Stop: " + getCurStop().getId() + " @ " + curTime + " -> Next Stop: " + getNextStop().getId() + " @ " + getArrivalTime() + " || Bus Passengers: " + getPassengers() + " || Bus Capacity: " + getCapacity() + " Bus Speed: " + getSpeed();
        } else {
            description = "Bus:" + id + " stopped with "+ getPassengers() +" passengers";
        }
    }

    /**
     * Gives a complete description of the state of the bus.
     * @return The complete description
     */
    @Override
    public String toString() {
        return description;
    }
}
