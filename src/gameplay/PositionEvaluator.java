package gameplay;

import board.Board;
import players.HardComputer;
import players.Player;

import java.io.FileNotFoundException;

/**
 * Created by liamkreiss on 12/9/18.
 */
public class PositionEvaluator {

    public PositionEvaluator(String file, boolean computerGoesFirst) throws FileNotFoundException {
        HardComputer computer = new HardComputer(computerGoesFirst);
        Game game = new Game(file, computer, computerGoesFirst);
        double score = computer.getScore(game.getGameboard());
        String playerName = computerGoesFirst ? "White" : "Black";

        System.out.printf("Position at %s has value %#.3f for %s\n", file, score, playerName);
    }

    public PositionEvaluator(String file) throws FileNotFoundException {
        HardComputer whiteComputer = new HardComputer(true);
        HardComputer blackComputer = new HardComputer(false);
        Game whiteGame = new Game(file, whiteComputer, true);
        Game blackGame = new Game(file, blackComputer, false);
        double whiteScore = whiteComputer.getScore(whiteGame.getGameboard());
        double blackScore = blackComputer.getScore(blackGame.getGameboard());
        String winningPlayerName = whiteScore > blackScore ? "White" : "Black";

        System.out.printf("Position at %s\nValue for White: %#.3f\nValue for Black: %#.3f\n%s is currently winning\n",
                file, whiteScore, blackScore, winningPlayerName);
    }
}
