package gameplay;

import board.Board;
import board.Tile;
import players.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by liamkreiss on 12/9/18.
 */
public class Game {
    private Player[] players;
    private Board gameboard;
    private GameType gameType;
    private boolean showText = false;

    boolean p1turn = true;
    Player activePlayer;
    boolean gameOver = false;

    public Game(GameType gameType, boolean humanFirst) {
        Scanner console = new Scanner(System.in);
        if (humanFirst) {
            this.players = new Player[]{
                    new HumanPlayer(true, console),
                    new HardComputer(false)};
        } else {
            this.players = new Player[]{
                    new HardComputer(true),
                    new HumanPlayer(false, console)};
        }
        this.gameboard = new Board(players);

        if (gameType.equals(GameType.TEXT_GAME) || gameType.equals(GameType.TEST_GAME)) {
            this.showText = true;
        }
        if (gameType.equals(GameType.VISUAL_GAME)) {
            startVisualGame();
        } else {
            startTextGame();
        }
    }

    public Game(GameType gameType, String fileName) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        this.gameType = gameType;
        this.players = new Player[]{
                new HumanPlayer(true, console),
                new HumanPlayer(false, console)};
        Scanner file = new Scanner(new File("board_states/" + fileName));
        this.gameboard = new Board(players, file);
//        System.out.println(gameboard.toString());
    }


    //always human vs. AI with pre-made weights
    public Game(GameType gameType, String fileName, HardComputer player, boolean computerIsWhite) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        this.gameType = gameType;
        if (computerIsWhite) {
            this.players = new Player[]{
                    player,
                    new HumanPlayer(false, console)};
        } else {
            this.players = new Player[]{
                    new HumanPlayer(true, console),
                    player};
        }

        Scanner file = new Scanner(new File("board_states/" + fileName));
        this.gameboard = new Board(players, file);
//        System.out.println(gameboard.toString());
    }

    public Game(GameType gameType, Player p1, Player p2) {
        this.players = new Player[]{p1, p2};
        this.gameboard = new Board(players);
        this.gameType = gameType;
        if (gameType.equals(GameType.TEXT_GAME) || gameType.equals(GameType.TEST_GAME)) {
            this.showText = true;
        }

        if (showText) {
            System.out.println(gameboard.toString());
        }
    }

    public Player startGame() {
        if (gameType.equals(GameType.VISUAL_GAME)) {
            startVisualGame();
            return null;
        } else {
            return startTextGame();
        }
    }

    public Player startTextGame() {
        p1turn = true;
        activePlayer = players[0];
        gameOver = false;
        GameState curState = new GameState();
        if (gameboard.getValidMoves(activePlayer).size() == 0) {
            if (gameboard.getValidMoves(gameboard.getOpponent(activePlayer)).size() == 0) {
                curState.gameOver(gameboard.getWinner());
            } else {
                activePlayer = gameboard.getOpponent(activePlayer);
                p1turn = !p1turn;
            }
        }
        while (!curState.isGameOver()) {
            curState = makeMove(activePlayer.getMove(gameboard));
        }
        Player winner = curState.getWinner();
        if (winner == null) {
            if (showText) {
                System.out.println("It's a tie!");
            }
            return null;
        } else {
            if (showText) {
                System.out.println("Player " + winner.getName() + " wins!");
            }
            return winner;
        }
    }

    public void startVisualGame() {
        p1turn = true;
        activePlayer = players[0];
        gameOver = false;
    }

    //returns null if not possible to make that move
    public GameState makeMove(int index) {
        if (gameboard.makeMove(activePlayer, index)) {
            GameState curState = new GameState();
            p1turn = !p1turn;
            activePlayer = p1turn ? players[0] : players[1];

            if (showText) {
                System.out.println(gameboard.toString());
                int[] pieceCounts = gameboard.getPieceCounts();
                System.out.printf("CURRENT SCORE\nBlack [%d] - [%d] White\n\n", pieceCounts[0], pieceCounts[1]);
            }

            if (gameboard.getValidMoves(activePlayer).size() == 0) {
                if (gameboard.getValidMoves(gameboard.getOpponent(activePlayer)).size() == 0) {
                    curState.gameOver(gameboard.getWinner());
                } else {
                    p1turn = !p1turn;
                    activePlayer = p1turn ? players[0] : players[1];
                }
            }
            return curState;
        }
        return null;
    }

    private boolean isValidMove(int index) {
        return (gameboard.getValidMoves(activePlayer).contains(index));
    }

    public Tile[][] getTiles() {
        return gameboard.getGameboard();
    }

    public Board getBoard() {
        return gameboard;
    }

    public int getBoardSize() {
        return gameboard.getBoardSize();
    }

    public Player[] getPlayers() {
        return this.players;
    }

    public boolean getP1Turn() {
        return this.p1turn;
    }
}
