package core.model;

/**
 * Notable event of the simulation
 * (eg: A bus arrives at a stop)
 */
public class Event {
    private final int time;
    private final int id;
    private final EventType type;

    /**
     * Creates a new event
     * @param time  When the event occurs
     * @param type  What type of event it is
     * @param id    Which object this event applies to
     */
    public Event(int time, EventType type, int id) {
        this.time = time;
        this.id = id;
        this.type = type;
    }

    /**
     * Gets when the event occurs
     * @return  The event time
     */
    public int getTime() {
        return time;
    }

    /**
     * Gets the type of the event
     * @return The event type
     */
    public EventType getType() {
        return type;
    }

    /**
     * Gets the ID of the object on which the event occurs
     * @return  The event's corresponding object's ID
     */
    public int getId() {
        return id;
    }

}
