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
        } else if (difficulty.equals(Difficulty.MEDIUM)) {
            return getMediumMove(gameboard);
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

    private int getMediumMove(Board gameboard) {
        Player opponent = gameboard.getOpponent(this);

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

        ArrayList<Integer> validMovesForPlayer = gameboard.getValidMoves(this);
        Board potentialBoardForOpponent;
        int[] allMoves = new int[validMovesForPlayer.size()];

        //MOVE THAT GETS THE HIGHEST VALUE FROM THE FOLLOWING
        for (int i = 0; i < validMovesForPlayer.size(); i++) {
            int validMoveForPlayer = validMovesForPlayer.get(i);
            potentialBoardForOpponent = new Board(gameboard);
            potentialBoardForOpponent.makeMove(this, validMoveForPlayer);
            ArrayList<Integer> validMovesForOpponent = potentialBoardForOpponent.getValidMoves(opponent);
            Board potentialBoardForPlayer;
            int worstMoveScore = Integer.MAX_VALUE;

            //MOVE THAT GETS THE LOWEST VALUE FROM THE FOLLOWING
            for (int validMoveForOpponent : validMovesForOpponent) {
                potentialBoardForPlayer = new Board(potentialBoardForOpponent);
                potentialBoardForPlayer.makeMove(opponent, validMoveForOpponent);
                int moveScore = potentialBoardForPlayer.getValidMoves(this).size();
                if (moveScore < worstMoveScore) {
                    worstMoveScore = moveScore;
                }
            }
            allMoves[i] = worstMoveScore;
        }

        int bestMove = -1;
        int bestMoveScore = -1;
        for (int i = 0; i < allMoves.length; i++) {
            if (allMoves[i] > bestMoveScore) {
                bestMove = i;
                bestMoveScore = allMoves[i];
            }
        }

        return validMovesForPlayer.get(bestMove);
    }

    private int getHardMove(Board gameboard) {
        return getMediumMove(gameboard);
    }
}
