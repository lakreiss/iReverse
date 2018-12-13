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
    private final double[] BEST_WEIGHTS = new double[]{
            +0.601, +0.245, -0.693, +0.623, -0.932, -0.330, -0.273, -0.164, -0.749, +0.281, -1.349,
            +0.386, -0.675, -0.485, +0.355, +1.237, -0.377, +0.489, -1.031, -0.126, +1.044, +1.114,
            -0.407, +0.250, +0.313, +0.143, +0.535, -0.235, -1.832, +0.539};

    public HardComputer(boolean firstPlayer, double[] weights) {
        super(firstPlayer);
        this.weights = weights;
    }

    public HardComputer(boolean firstPlayer) {
        super(firstPlayer);
        this.weights = BEST_WEIGHTS;
    }

//    //doesn't look ahead
//    public int getMove(Board gameboard) {
//        ArrayList<Integer> validMoves = gameboard.getValidMoves(this);
//        Board potentialGameboard;
//        int bestMove = -1;
//        double bestMoveScore = Integer.MIN_VALUE;
//        for (int i = 0; i < validMoves.size(); i++) {
//            potentialGameboard = new Board(gameboard);
//            potentialGameboard.makeMove(this, validMoves.get(i));
//
//            double boardScore = getScore(potentialGameboard);
//            if (boardScore > bestMoveScore) {
//                bestMoveScore = boardScore;
//                bestMove = i;
//            }
//        }
//        return validMoves.get(bestMove);
//    }

    //looks one full move ahead
    public int getMove(Board gameboard) {
        Player opponent = gameboard.getOpponent(this);
        ArrayList<Integer> validMovesForPlayer = gameboard.getValidMoves(this);
        Board potentialBoardForOpponent;
        double[] allMoveScores = new double[validMovesForPlayer.size()];

        //MOVE THAT GETS THE HIGHEST VALUE FROM THE FOLLOWING
        for (int i = 0; i < validMovesForPlayer.size(); i++) {
            potentialBoardForOpponent = new Board(gameboard);
            potentialBoardForOpponent.makeMove(this, validMovesForPlayer.get(i));
            ArrayList<Integer> validMovesForOpponent = potentialBoardForOpponent.getValidMoves(opponent);
            Board potentialBoardForPlayer;
            double worstMoveScore = Integer.MAX_VALUE;

            //MOVE THAT GETS THE LOWEST VALUE FROM THE FOLLOWING
            for (int validMoveForOpponent : validMovesForOpponent) {
                potentialBoardForPlayer = new Board(potentialBoardForOpponent);
                potentialBoardForPlayer.makeMove(opponent, validMoveForOpponent);
                double moveScore = getScore(potentialBoardForPlayer.getGameboard());
                if (moveScore < worstMoveScore) {
                    worstMoveScore = moveScore;
                }
            }
            allMoveScores[i] = worstMoveScore;
        }

        int bestMove = -1;
        double bestMoveScore = Integer.MIN_VALUE;
        for (int i = 0; i < allMoveScores.length; i++) {
            if (allMoveScores[i] > bestMoveScore) {
                bestMove = i;
                bestMoveScore = allMoveScores[i];
            }
        }

        return validMovesForPlayer.get(bestMove);
    }

    public double getScore(Tile[][] gameboard) {
        double score = 0;
        int index;
        for (Tile[] row : gameboard) {
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
