package players;

import board.Board;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by liamkreiss on 12/9/18.
 */
public class ComputerPlayer extends Player {
    protected Random r;


    public ComputerPlayer(boolean firstPlayer) {
        super(firstPlayer);
        this.r = new Random();
    }

    public int getMove(Board gameboard) {
        return -1;
    }
}
