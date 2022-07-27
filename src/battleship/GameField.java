package battleship;

import java.util.Arrays;

public class GameField {
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
