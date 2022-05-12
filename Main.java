package battleship;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}

class Game {
    protected void start() {
        Player player1 = new Player("Player 1", true);
        Player player2 = new Player("Player 2", false);

        initialStage(player1, player2);
        battle(player1, player2);

    }

    protected void initialStage(Player p1, Player p2) {
        System.out.println(p1.getName() + ", place your ships on the game field\n");
        prepareShips(p1.gf, p1.sb);
        UserInteraction.switchTurn();
        System.out.println(p2.getName() + ", place your ships on the game field\n");
        System.out.println();
        prepareShips(p2.gf, p2.sb);
        UserInteraction.switchTurn();
        System.out.println("The game starts!\n");
    }

    protected void startOfTurn(Player attackingPlayer) {
        attackingPlayer.gf.drawField(attackingPlayer.gf.fogField);
        System.out.println("---------------------");
        attackingPlayer.gf.drawField(attackingPlayer.gf.field);
        System.out.println(attackingPlayer.getName() + ", it's your turn:\n");
        System.out.print("> ");
    }

    protected void battle(Player p1, Player p2) {
        while (!p1.isWin() && !p2.isWin()) {
            if (p1.isTurn()) {
                startOfTurn(p1);
                makeShot(p1, p2);
                if (p2.sb.getCountShips() == 0) {
                    p1.setWin();
                    break;
                }
            } else if (p2.isTurn()) {
                startOfTurn(p2);
                makeShot(p2, p1);
                if (p1.sb.getCountShips() == 0) {
                    p2.setWin();
                    break;
                }
            }
            UserInteraction.switchTurn();
            p1.setTurn();
            p2.setTurn();
        }
    }

    protected void prepareShips(GameField gf, ShipBuilder sb) {
        gf.createClearField(gf.field);
        gf.createClearField(gf.fogField);
        gf.drawField(gf.field);
        int i = 0;
        boolean check;

        while (i < sb.getShips().length) {
            UserInteraction.getQuery(sb.getShips()[i]);
            String[] answer;
            do {
                answer = UserInteraction.verifyCoordinates(UserInteraction.answer(), sb.getShips()[i]);
                if (answer == null) {
                    continue;
                }
                check = gf.createNextField(answer, gf.field, sb.getShips()[i]);
                if (!check) {
                    answer = null;
                    UserInteraction.callErrorPlaced();
                }
            } while (answer == null);

            gf.drawField(gf.field);
            i++;
        }
    }

    protected void makeShot(Player attacking, Player waiting) {
        String[] coordinates;
        do {
            coordinates = UserInteraction.takeShot();
        } while (coordinates == null);

        waiting.gf.checkShot(coordinates, attacking.gf, waiting.sb);
    }
}

class GameField {
    String[][] field = new String[12][12];
    String[][] fogField = new String[12][12];

    protected void createClearField(String[][] field) {
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (i == 0 && j == 0) {
                    field[0][0] = " ";
                } else if (i == 0) {
                    field[i][j] = j + "";
                } else if (j == 0) {
                    field[i][j] = ((char) (64 + i)) + "";
                } else {
                    field[i][j] = "~";
                }
            }
        }
    }

    protected void checkShot(String[] coordinates, GameField gfOfAttackingPlayer, ShipBuilder sbOfWaitingPlayer) {
        int charShot = coordinates[0].charAt(0) - 64;
        int numShot = Integer.parseInt(coordinates[1]);

        if ("O".equals(field[charShot][numShot])) {
            field[charShot][numShot] = "X";
            gfOfAttackingPlayer.fogField[charShot][numShot] = "X";
            for (Ship ship : sbOfWaitingPlayer.getShips()) {
                if (ship.checkStatusShot(numShot, charShot)) {
                    if (ship.getLives() == 0) {
                        sbOfWaitingPlayer.setCountShips();
                        if (sbOfWaitingPlayer.getCountShips() == 0) {
                            System.out.println("You sank the last ship. You won. Congratulations!");
                        } else {
                            System.out.println("You sank a ship! Specify a new target!\n");
                        }
                    } else {
                        System.out.println("You hit a ship!\n");
                    }
                    break;
                }
            }
        } else if (("X".equals(field[charShot][numShot]))) {
            System.out.println("Hit\n");
        } else if (("M".equals(field[charShot][numShot]))) {
            System.out.println("Miss\n");
        } else {
            field[charShot][numShot] = "M";
            gfOfAttackingPlayer.fogField[charShot][numShot] = "M";
            System.out.println("You missed!\n");
        }
    }

    protected void drawField(String[][] field) {
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                System.out.print(field[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    protected boolean createNextField(String[] coordinates, String[][] field, Ship ship) {
        int maxNum = Math.max(Integer.parseInt(coordinates[1]), Integer.parseInt(coordinates[3]));
        int minNum = Math.min(Integer.parseInt(coordinates[1]), Integer.parseInt(coordinates[3]));
        int maxCh = Math.max(coordinates[0].charAt(0), coordinates[2].charAt(0)) - 64;
        int minCh = Math.min(coordinates[0].charAt(0), coordinates[2].charAt(0)) - 64;

        if (minCh == maxCh) {
            if ("O".equals(field[minCh - 1][minNum - 1]) || "O".equals(field[minCh][minNum - 1]) || "O".equals(field[minCh + 1][minNum - 1])) {
                return false;
            } else if ("O".equals(field[minCh - 1][maxNum + 1]) || "O".equals(field[minCh][maxNum + 1]) || "O".equals(field[minCh + 1][maxNum + 1])) {
                return false;
            } else {
                for (int i = minNum; i <= maxNum; i++) {
                    if ("O".equals(field[minCh - 1][i]) || "O".equals(field[minCh][i]) || "O".equals(field[minCh + 1][i])) {
                        return false;
                    } else {
                        field[minCh][i] = "O";
                    }
                }
                ship.setCoordinatesX(minNum, maxNum);
                Arrays.fill(ship.getCoordinatesY(), minCh);
            }
        } else if (minNum == maxNum) {
            if ("O".equals(field[minCh - 1][minNum - 1]) || "O".equals(field[minCh - 1][minNum]) || "O".equals(field[minCh - 1][minNum + 1])) {
                return false;
            } else if ("O".equals(field[maxCh + 1][maxNum - 1]) || "O".equals(field[maxCh + 1][maxNum]) || "O".equals(field[maxCh + 1][maxNum + 1])) {
                return false;
            } else {
                for (int i = minCh; i <= maxCh; i++) {
                    if ("O".equals(field[i][minNum - 1]) || "O".equals(field[i][minNum]) || "O".equals(field[i][minNum + 1])) {
                        return false;
                    } else {
                        field[i][minNum] = "O";
                    }
                }
                Arrays.fill(ship.getCoordinatesX(), minNum);
                ship.setCoordinatesY(minCh, maxCh);
            }
        }
        return true;
    }
}

class UserInteraction {
    protected static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        } catch (IOException | InterruptedException ex) {
            System.out.println("Clear is bad");
        }
    }

    protected static void getQuery(Ship ship) {
        System.out.printf("Enter the coordinates of the %s (%d cells):\n\n", ship.getName(), ship.getCells());
    }

    protected static String[] answer() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String[] answer = takeShipCoordinates(reader.readLine().replaceAll("\\s+", "").toUpperCase());
            if (answer == null) {
                throw new IOException();
            } else {
                System.out.println();
                return answer;
            }
        } catch (IOException e) {
            callError();
            return null;
        }
    }

    protected static String[] takeShot() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String[] answer = takeShotCoordinates(reader.readLine().replaceAll("\\s+", "").toUpperCase());
            if (answer == null) {
                throw new IOException();
            } else {
                System.out.println();
                return answer;
            }
        } catch (IOException e) {
            System.out.println();
            callErrorShot();
            return null;
        }
    }

    protected static void switchTurn() {
        System.out.print("Press Enter and pass the move to another player");
        Scanner scanner = new Scanner(System.in);
        while ("\n".equals(scanner.nextLine())) {
            scanner.close();
        }
        System.out.println("...");
        clearConsole();
    }

    protected static void callError() {
        System.out.println();
        System.out.println("Error! Try again:\n");
    }

    protected static void callErrorWrongLength(Ship ship) {
        System.out.printf("Error! Wrong length of the %s! Try again:\n\n", ship.getName());
    }

    protected static void callErrorShipLocation() {
        System.out.println("Error! Wrong ship location! Try again:\n");
    }

    protected static void callErrorShot() {
        System.out.println("Error! You entered the wrong coordinates! Try again:\n");
    }

    protected static void callErrorPlaced() {
        System.out.println("Error! You placed it too close to another one. Try again:\n");
    }

    protected static String[] takeShotCoordinates(String shotCoordinates) {
        String[] coordinates = new String[2];
        Arrays.fill(coordinates, "");
        String[] data = shotCoordinates.split("");

        if (!Pattern.matches("[A-J]", data[0])) {
            return null;
        } else {
            coordinates[0] = data[0];
        }

        for (int i = 1; i < data.length; i++) {
            if (Pattern.matches("[0-9]", coordinates[i - 1])) {
                coordinates[i - 1] += data[i];
            } else {
                coordinates[i] = data[i];
            }
        }
        try {
            if (Integer.parseInt(coordinates[1]) < 1 || Integer.parseInt(coordinates[1]) > 10) {
                return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return coordinates;
    }

    protected static String[] takeShipCoordinates(String answer) {
        String[] coordinates = new String[4];
        Arrays.fill(coordinates, "");
        String[] data = answer.split("");
        int currentIndex = 0;
        int counterChars = 0;

        if (!Pattern.matches("[A-J]", data[0])) {
            return null;
        }
        for (int i = 0; i < data.length; i++) {
            if (Pattern.matches("[A-J]", data[i]) || Pattern.matches("[0-9]", data[i])) {
                if (i == 0) {
                    coordinates[currentIndex] = data[i];
                    counterChars++;
                    currentIndex++;
                } else if (Pattern.matches("[0-9]", data[i])) {
                    if (Pattern.matches("[0-9]", coordinates[currentIndex - 1])) {
                        coordinates[currentIndex - 1] += data[i];
                    } else {
                        coordinates[currentIndex] = data[i];
                        currentIndex++;
                    }
                } else if (Pattern.matches("[A-J]", data[i])) {
                    coordinates[currentIndex] = data[i];
                    counterChars++;
                    currentIndex++;
                }
                if (counterChars > 2) {
                    return null;
                }
            } else {
                return null;
            }
        }

        if (Integer.parseInt(coordinates[1]) < 1 || Integer.parseInt(coordinates[1]) > 10) {
            return null;
        }
        if (Integer.parseInt(coordinates[3]) < 1 || Integer.parseInt(coordinates[3]) > 10) {
            return null;
        }

        return coordinates;
    }

    protected static String[] verifyCoordinates(String[] coordinates, Ship ship) {
        if (coordinates == null) {
            return null;
        }
        int maxNum = Math.max(Integer.parseInt(coordinates[1]), Integer.parseInt(coordinates[3]));
        int minNum = Math.min(Integer.parseInt(coordinates[1]), Integer.parseInt(coordinates[3]));
        int maxCh = Math.max(coordinates[0].charAt(0), coordinates[2].charAt(0));
        int minCh = Math.min(coordinates[0].charAt(0), coordinates[2].charAt(0));
        if (minCh == maxCh && minNum == maxNum) {
            callErrorWrongLength(ship);
            return null;
        } else if (minCh == maxCh && (maxNum - minNum != ship.getCells() - 1)) {
            callErrorWrongLength(ship);
            return null;
        } else if (minNum == maxNum && maxCh - minCh != ship.getCells() - 1) {
            callErrorWrongLength(ship);
            return null;
        } else if (minNum != maxNum && minCh != maxCh) {
            callErrorShipLocation();
            return null;
        }
        return coordinates;
    }
}

enum ShipCollection {
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

class Ship {
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

class ShipBuilder {
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

class Player {
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