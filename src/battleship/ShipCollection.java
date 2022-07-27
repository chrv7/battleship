package battleship;

public enum ShipCollection {
    AIRCRAFT_CARRIER("Aircraft Carrier" ,5),
    BATTLESHIP("Battleship", 4),
    SUBMARINE("Submarine", 3),
    CRUISER("Cruiser", 3),
    DESTROYER("Destroyer", 2);

    private final String name;
    private final int cells;

    ShipCollection(String name, int cells) {
        this.name = name;
        this.cells = cells;
    }

    public String getName() {
        return this.name;
    }

    public int getCells() {
        return this.cells;
    }
}