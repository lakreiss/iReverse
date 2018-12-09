package players;

import board.Board;

import java.util.Scanner;

/**
 * Created by liamkreiss on 12/9/18.
 */
public class HumanPlayer extends Player {
    private Scanner console;

    public HumanPlayer(boolean firstPlayer, Scanner console) {
        super(firstPlayer);
        this.console = console;
    }

    public int getMove(Board gameboard) {
        System.out.println(getName() + "'s turn");
        System.out.print("Row? ");
        int row = console.nextInt();
        System.out.print("Column? ");
        int col = console.nextInt();
        if (row < 0 || row >= Board.BOARD_SIZE  || col < 0 || col >= Board.BOARD_SIZE) {
            System.out.println("Illegal move: invalid tile");
            return getMove(gameboard);
        }
        return Board.getIndexFromRowCol(row, col);
    }
}
