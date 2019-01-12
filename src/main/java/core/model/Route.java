package core.model;

import java.util.ArrayList;

/**
 * Route (sequence of stops) followed by buses in the network
 */
public class Route extends ArrayList<Stop> {
    private final int id;
    private final int number;
    private final String name;

    /**
     * Creates a new instance
     *
     * @param id        Route id
     * @param number    Route number
     * @param name      Route name
     */
    public Route(int id, int number, String name) {
        super();
        this.id = id;
        this.number = number;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }
}
