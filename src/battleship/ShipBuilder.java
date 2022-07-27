package battleship;

public class ShipBuilder {
    final private Ship aircraft = new Ship(ShipCollection.AIRCRAFT_CARRIER.getName(), ShipCollection.AIRCRAFT_CARRIER.getCells());
    final private Ship battleship = new Ship(ShipCollection.BATTLESHIP.getName(), ShipCollection.BATTLESHIP.getCells());
    final private Ship submarine = new Ship(ShipCollection.SUBMARINE.getName(), ShipCollection.SUBMARINE.getCells());
    final private Ship cruiser = new Ship(ShipCollection.CRUISER.getName(), ShipCollection.CRUISER.getCells());
    final private Ship destroyer = new Ship(ShipCollection.DESTROYER.getName(), ShipCollection.DESTROYER.getCells());
    final private Ship[] ships = {aircraft, battleship, submarine, cruiser, destroyer};
    private int countShips = 5;

    public int getCountShips() {
        return this.countShips;
    }

    public void setCountShips() {
        this.countShips--;
    }

    public Ship[] getShips() {
        return this.ships;
    }
}