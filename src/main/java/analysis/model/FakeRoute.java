package analysis.model;

public class FakeRoute {
    private final int id;
    private final int number;
    private final String name;

    public FakeRoute(int id, int number, String name) {
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
