package battleship;

public class Game {
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

