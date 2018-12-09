package players;

import board.Board;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by liamkreiss on 12/9/18.
 */
public class ComputerPlayer extends Player {
    private Difficulty difficulty;
    Random r;


    public ComputerPlayer(boolean firstPlayer, Difficulty d) {
        super(firstPlayer);
        this.difficulty = d;
        this.r = new Random();
    }

    public int getMove(Board gameboard) {
        if (difficulty.equals(Difficulty.EASY)) {
            return getEasyMove(gameboard);
        } else if (difficulty.equals(Difficulty.HARD)) {
            return getHardMove(gameboard);
        } else {
            throw new Error("Illegal difficulty level");
        }
    }

    private int getEasyMove(Board gameboard) {
        ArrayList<Integer> validMoves = gameboard.getValidMoves(this);
        int moveChoice = r.nextInt(validMoves.size());
        return validMoves.get(moveChoice);
    }

    private int getHardMove(Board gameboard) {
        Player opponent = gameboard.getOpponent(this);
        ArrayList<Integer> validMovesForPlayer = gameboard.getValidMoves(this);

//        Board potentialBoardForOpponent;
//        int bestMove = -1;
//        int bestMoveScore = -1;
//        for (int validMoveForPlayer : validMovesForPlayer) {
//            potentialBoardForOpponent = new Board(gameboard);
//            potentialBoardForOpponent.makeMove(this, validMoveForPlayer);
//            ArrayList<Integer> validMovesForOpponent = potentialBoardForOpponent.getValidMoves(opponent);
//            Board potentialBoardForPlayer;
//            for (int validMoveForOpponent : validMovesForOpponent) {
//                potentialBoardForPlayer = new Board(potentialBoardForOpponent);
//                potentialBoardForPlayer.makeMove(opponent, validMoveForOpponent);
//                int moveScore = potentialBoardForPlayer.getValidMoves(this).size();
//                if (moveScore > bestMoveScore) {
//                    bestMove = validMoveForPlayer;
//                    bestMoveScore = moveScore;
//                }
//            }
//        }

        


        return bestMove;
    }
}
