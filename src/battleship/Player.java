package battleship;

public class Player {
    final private String name;
    private boolean turn;
    private boolean win = false;
    GameField gf = new GameField();
    ShipBuilder sb = new ShipBuilder();

    Player(String name, boolean turn) {
        this.name = name;
        this.turn = turn;
    }

    public String getName() {
        return name;
    }

    public boolean isWin() {
        return win;
    }

    public boolean isTurn() {
        return turn;
    }

    public void setTurn () {
        this.turn = !turn;
    }

    public void setWin() {
        this.win = true;
    }
}