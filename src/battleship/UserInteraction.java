package battleship;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

public class UserInteraction {
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