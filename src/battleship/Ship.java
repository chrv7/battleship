package battleship;

public class Ship {
    private final String name;
    private final int cells;
    private final int[] coordinatesX;
    private final int[] coordinatesY;
    private int lives;

    Ship(String name, int cells) {
        this.name = name;
        this.cells = cells;
        this.coordinatesX = new int[cells];
        this.coordinatesY = new int[cells];
        this.lives = cells;
    }

    public int[] getCoordinatesX() {
        return coordinatesX;
    }

    public int[] getCoordinatesY() {
        return coordinatesY;
    }

    public String getName() {
        return name;
    }

    public int getCells() {
        return cells;
    }

    public void setCoordinatesX(int beginVal, int endVal) {
        for (int i = 0; beginVal <= endVal; beginVal++, i++) {
            this.coordinatesX[i] = beginVal;
        }
    }

    public void setCoordinatesY(int beginVal, int endVal) {
        for (int i = 0; beginVal <= endVal; beginVal++, i++) {
            this.coordinatesY[i] = beginVal;
        }
    }

    public boolean checkStatusShot(int x, int y) {
        for (int i = 0; i < coordinatesX.length; i++) {
            if (x == coordinatesX[i] && y == coordinatesY[i]) {
                coordinatesX[i] = 0;
                coordinatesY[i] = 0;
                lives--;
                return true;
            }
        }
        return false;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int x :coordinatesX) {
            str.append(x).append(" ");
        }
        str.append("\n");
        for (int y :coordinatesY) {
            str.append(y).append(" ");
        }
        return str.toString();
    }

    public int getLives() {
        return this.lives;
    }
}