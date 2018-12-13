package players;

import board.Board;

import java.util.ArrayList;

/**
 * Created by liamkreiss on 12/12/18.
 */
public class EasyComputer extends ComputerPlayer{
    public EasyComputer(boolean firstPlayer) {
        super(firstPlayer);
    }

    public int getMove(Board gameboard) {
        ArrayList<Integer> validMoves = gameboard.getValidMoves(this);
        int moveChoice = this.r.nextInt(validMoves.size());
        return validMoves.get(moveChoice);
    }
}
