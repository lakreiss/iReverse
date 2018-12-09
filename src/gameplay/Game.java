package gameplay;

import players.HumanPlayer;
import players.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by liamkreiss on 12/9/18.
 */
public class Game {
    private Player[] players;
    private Board gameboard;

    public Game() {
        Scanner console = new Scanner(System.in);
        this.players = new Player[]{new HumanPlayer(true, console), new HumanPlayer(false, console)};
        this.gameboard = new Board(players);
        System.out.println(gameboard.toString());
    }

    public Game(String fileName) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        this.players = new Player[]{new HumanPlayer(true, console), new HumanPlayer(false, console)};
        Scanner file = new Scanner(new File("board_states/" + fileName));
        this.gameboard = new Board(players, file);
        System.out.println(gameboard.toString());
    }

    public void startGame() {
        boolean p1turn = true;
        Player activePlayer = players[0];
        boolean moveSucceeded;
        boolean gameOver = gameboard.getValidMoves(activePlayer).size() == 0;
        while (!gameOver) {
            if (gameboard.makeMove(activePlayer, activePlayer.getMove(gameboard))) {
                p1turn = !p1turn;
                activePlayer = p1turn ? players[0] : players[1];
                System.out.println(gameboard.toString());
                gameOver = gameboard.getValidMoves(activePlayer).size() == 0;
            }
        }
        Player winner = gameboard.getWinner();
        if (winner == null) {
            System.out.println("It's a tie!");
        } else {
            System.out.println("Player " + winner.getName() + " wins!");
        }
    }
}
