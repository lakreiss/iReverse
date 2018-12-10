package players;

import board.Board;
import board.Tile;
import board.Type;

import java.util.ArrayList;

/**
 * Created by liamkreiss on 12/9/18.
 */
public class HardComputer extends ComputerPlayer{
    //ONLY WORKS FOR 8x8
    /*
    should contain 30 doubles:
    {A, B, C, D, E, F, G, H, I, J
    Opponent's A, B, C, D, E, F, G, H, I, J
    Nobody's A, B, C, D, E, F, G, H, I, J}

    ABCDDCBA
    BEFGGFEB
    CFHIIHFC
    DGIJJIGD
    DGIJJIGD
    CFHIIHFC
    BEFGGFEB
    ABCDDCBA
     */
    double[] weights;
    private final int TILE_TYPES = 10;
    private final double[] BEST_WEIGHTS = new double[]{1.141, -0.544, 1.932, 1.170, -0.620, 0.470, -1.112, 0.140,
            1.414, 0.509, -0.129, -0.098, 0.346, -0.587, -0.513, 0.582, -0.440, -0.393, 0.402, -0.093,
            0.637, 0.978, 0.541, 0.157, 1.508, -0.296, -0.083, 0.890, 0.477, -0.133};

    public HardComputer(boolean firstPlayer, double[] weights) {
        super(firstPlayer, Difficulty.HARD);
        this.weights = weights;
    }

    public HardComputer(boolean firstPlayer) {
        super(firstPlayer, Difficulty.HARD);
        this.weights = BEST_WEIGHTS;
    }

    public int getMove(Board gameboard) {
        ArrayList<Integer> validMoves = gameboard.getValidMoves(this);
        Board potentialGameboard;
        int bestMove = -1;
        double bestMoveScore = Integer.MIN_VALUE;
        for (int i = 0; i < validMoves.size(); i++) {
            potentialGameboard = new Board(gameboard);
            potentialGameboard.makeMove(this, validMoves.get(i));
            double boardScore = getScore(potentialGameboard);
            if (boardScore > bestMoveScore) {
                bestMoveScore = boardScore;
                bestMove = i;
            }
        }
        return validMoves.get(bestMove);
    }

    public double getScore(Board gameboard) {
        double score = 0;
        int index;
        for (Tile[] row : gameboard.getGameboard()) {
            for (Tile t : row) {
                int typeVal = t.getType().getNumVal();
                if (t.getOccupied()) {
                    if (t.getOwner().equals(this)) {
                        score += weights[typeVal];
                    } else {
                        score += weights[TILE_TYPES + typeVal];
                    }
                } else {
                    score += weights[2 * TILE_TYPES + typeVal];
                }
            }
        }
        return score;
    }
}
