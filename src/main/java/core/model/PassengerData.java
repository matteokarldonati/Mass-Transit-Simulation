package core.model;

/**
 * Immutable class grouping the 8 statistical data for each stops
 * Provides getters and pseudo-setters (because immutable) for each value
 */
public final class PassengerData {
    private final int arrivalLow;
    public int getArrivalLow() {
        return arrivalLow;
    }
    public PassengerData withArrivalLow(int arrivalLow) {
        return new PassengerData(arrivalLow, arrivalHigh, departLow, departHigh, onLow, onHigh, offLow, offHigh);
    }

    private final int arrivalHigh;
    public int getArrivalHigh() {
        return arrivalHigh;
    }
    public PassengerData withArrivalHigh(int arrivalHigh) {
        return new PassengerData(arrivalLow, arrivalHigh, departLow, departHigh, onLow, onHigh, offLow, offHigh);
    }

    private final int departLow;
    public int getDepartLow() {
        return departLow;
    }
    public PassengerData withDepartLow(int departLow) {
        return new PassengerData(arrivalLow, arrivalHigh, departLow, departHigh, onLow, onHigh, offLow, offHigh);
    }

    private final int departHigh;
    public int getDepartHigh() {
        return departHigh;
    }
    public PassengerData withDepartHigh(int departHigh) {
        return new PassengerData(arrivalLow, arrivalHigh, departLow, departHigh, onLow, onHigh, offLow, offHigh);
    }

    private final int onLow;
    public int getOnLow() {
        return onLow;
    }
    public PassengerData withOnLow(int onLow) {
        return new PassengerData(arrivalLow, arrivalHigh, departLow, departHigh, onLow, onHigh, offLow, offHigh);
    }

    private final int onHigh;
    public int getOnHigh() {
        return onHigh;
    }
    public PassengerData withOnHigh(int onHigh) {
        return new PassengerData(arrivalLow, arrivalHigh, departLow, departHigh, onLow, onHigh, offLow, offHigh);
    }

    private final int offLow;
    public int getOffLow() {
        return offLow;
    }
    public PassengerData withOffLow(int offLow) {
        return new PassengerData(arrivalLow, arrivalHigh, departLow, departHigh, onLow, onHigh, offLow, offHigh);
    }

    private final int offHigh;
    public int getOffHigh() {
        return offHigh;
    }
    public PassengerData withOffHigh(int offHigh) {
        return new PassengerData(arrivalLow, arrivalHigh, departLow, departHigh, onLow, onHigh, offLow, offHigh);
    }

    /**
     * Creates a new instance with the given bounds
     *
     * @param arrivalHigh   Higher bound for passenger arrival at a stop
     * @param arrivalLow    Lower bound for passenger arrival at a stop
     * @param offHigh       Higher bound for passenger getting off a bus
     * @param offLow        Lower bound for passenger getting off a bus
     * @param onHigh        Higher bound for passenger getting on a bus
     * @param onLow         Lower bound for passenger getting on a bus
     * @param departHigh    Higher bound for passenger departure of a stop
     * @param departLow     Lower bound for passenger departure of a stop
     */
    public PassengerData(int arrivalHigh, int arrivalLow, int offHigh, int offLow, int onHigh, int onLow, int departHigh, int departLow) {
        this.arrivalLow = arrivalLow;
        this.arrivalHigh = arrivalHigh;
        this.departLow = departLow;
        this.departHigh = departHigh;
        this.onLow = onLow;
        this.onHigh = onHigh;
        this.offLow = offLow;
        this.offHigh = offHigh;
    }
}
