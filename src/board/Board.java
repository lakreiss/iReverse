package board;

import players.Player;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by liamkreiss on 12/8/18.
 */
public class Board {
    public static final int BOARD_SIZE = 8;
    private static final Direction[] ALL_DIRECTIONS = new Direction[]
            {Direction.NORTHWEST, Direction.NORTH, Direction.NORTHEAST, Direction.WEST,
                    Direction.EAST, Direction.SOUTHEAST, Direction.SOUTH, Direction.SOUTHWEST};
    private Tile[][] board;
    private Player p1, p2;

    public Board(Player[] players) {
        this.board = new Tile[BOARD_SIZE][BOARD_SIZE];
        this.p1 = players[0];
        this.p2 = players[1];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (i == (BOARD_SIZE / 2) - 1) {
                    if (j == (BOARD_SIZE / 2) - 1) {
                        board[i][j] = new Tile(getIndexFromRowCol(i, j), p1);
                    } else if (j == (BOARD_SIZE / 2)) {
                        board[i][j] = new Tile(getIndexFromRowCol(i, j), p2);
                    } else {
                        board[i][j] = new Tile(getIndexFromRowCol(i, j));
                    }
                } else if (i == (BOARD_SIZE / 2)) {
                    if (j == (BOARD_SIZE / 2) - 1) {
                        board[i][j] = new Tile(getIndexFromRowCol(i, j), p2);
                    } else if (j == (BOARD_SIZE / 2)) {
                        board[i][j] = new Tile(getIndexFromRowCol(i, j), p1);
                    } else {
                        board[i][j] = new Tile(getIndexFromRowCol(i, j));
                    }
                } else {
                    board[i][j] = new Tile(getIndexFromRowCol(i, j));
                }
            }
        }
    }

    public Board(Board curBoard) {
        this.board = new Tile[BOARD_SIZE][BOARD_SIZE];
        this.p1 = new Player(curBoard.getP1());
        this.p2 = new Player(curBoard.getP2());
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                this.board[row][col] = new Tile(curBoard.getTile(getIndexFromRowCol(row, col)));
            }
        }
    }

    public Board(Player[] players, Scanner file) {
        this.board = new Tile[BOARD_SIZE][BOARD_SIZE];
        this.p1 = players[0];
        this.p2 = players[1];
        String line;
        for (int row = 0; row < BOARD_SIZE; row++) {
            line = file.nextLine();
            for (int col = 0; col < BOARD_SIZE; col++) {
                char tileContents = line.charAt(col);
                if (tileContents == 'x') {
                    board[row][col] = new Tile(getIndexFromRowCol(row, col));
                } else if (tileContents == 'W') {
                    board[row][col] = new Tile(getIndexFromRowCol(row, col), p1);
                } else if (tileContents == 'B') {
                    board[row][col] = new Tile(getIndexFromRowCol(row, col), p2);
                } else {
                    throw new Error("Illegal char in file: " + tileContents);
                }
            }
        }
    }

    //true if possible move, false if impossible move
    public boolean makeMove(Player p, int index) {
        int row = getRowFromIndex(index);
        int col = getColFromIndex(index);
        ArrayList<Integer> validMoves = getValidMoves(p);
        if (validMoves.contains(index)) {
            board[row][col].setOwner(p);
            ArrayList<Neighbor> neighbors = getNeighbors(index);
            for (Neighbor n : neighbors) {
                if (n.getOccupied() && !n.getOwner().equals(p)) {
                    Direction d = n.getDirection();
                    Tile t = n;
                    while (t.getOccupied() && !t.getOwner().equals(p) && checkDirection(t.getIndex(), d)) {
                        t = getTile(t.getIndex(), d);
                    }
                    if (t.getOccupied() && t.getOwner().equals(p)) {
                        t = n;
                        board[getRowFromIndex(t.getIndex())][getColFromIndex(t.getIndex())].setOwner(p);
                        while (t.getOccupied() && !t.getOwner().equals(p) && checkDirection(t.getIndex(), d)) {
                            t.setOwner(p);
                            t = getTile(t.getIndex(), d);
                        }
                    }
                }
            }
            System.out.println("legal move");
            return true;
        } else {
            System.out.println("Illegal move " + p.getName() + ": (" + row + ", " + col + ")");
            return false;
        }
    }

    public ArrayList<Integer> getValidMoves(Player p) {
        ArrayList<Integer> validMoves = new ArrayList<Integer>();
        ArrayList<Neighbor> neighbors;
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col].getOccupied() && board[row][col].getOwner().equals(p)) {
                    neighbors = getNeighbors(getIndexFromRowCol(row, col));
                    for (Neighbor n : neighbors) {
                        if (n.getOccupied() && !n.getOwner().equals(p)) {
                            Direction d = n.getDirection();
                            Tile t = n;
                            while (t.getOccupied() && !t.getOwner().equals(p) && checkDirection(t.getIndex(), d)) {
                                t = getTile(t.getIndex(), d);
                            }
                            if (!t.getOccupied()) {
                                validMoves.add(t.getIndex());
                            }
                        }
                    }
                }
            }
        }
        return validMoves;
    }

    private ArrayList<Neighbor> getNeighbors(int index) {
        ArrayList<Neighbor> neighbors = new ArrayList<Neighbor>();
        for (Direction d : ALL_DIRECTIONS) {
            if (checkDirection(index, d)) {
                neighbors.add(new Neighbor(d, getTile(index, d)));
            }
        }
        return neighbors;
    }

    private Tile getTile(int index) {
        int[] rowCol = getRowColFromIndex(index);
        return board[rowCol[0]][rowCol[1]];
    }

    //assumes that the direction is valid for the given index
    private Tile getTile(int index, Direction d) {
        int neighborIndex;
        if (d.equals(Direction.NORTHWEST)) {
            neighborIndex = index - BOARD_SIZE - 1;
            return board[neighborIndex / 8][neighborIndex % 8];
        } else if (d.equals(Direction.NORTH)) {
            neighborIndex = index - BOARD_SIZE;
            return board[neighborIndex / 8][neighborIndex % 8];
        } else if (d.equals(Direction.NORTHEAST)) {
            neighborIndex = index - BOARD_SIZE + 1;
            return board[neighborIndex / 8][neighborIndex % 8];
        } else if (d.equals(Direction.WEST)) {
            neighborIndex = index - 1;
            return board[neighborIndex / 8][neighborIndex % 8];
        } else if (d.equals(Direction.EAST)) {
            neighborIndex = index + 1;
            return board[neighborIndex / 8][neighborIndex % 8];
        } else if (d.equals(Direction.SOUTHWEST)) {
            neighborIndex = index + BOARD_SIZE - 1;
            return board[neighborIndex / 8][neighborIndex % 8];
        } else if (d.equals(Direction.SOUTH)) {
            neighborIndex = index + BOARD_SIZE;
            return board[neighborIndex / 8][neighborIndex % 8];
        } else if (d.equals(Direction.SOUTHEAST)) {
            neighborIndex = index + BOARD_SIZE + 1;
            return board[neighborIndex / 8][neighborIndex % 8];
        } else {
            throw new Error("invalid direction 1");
        }
    }

    private boolean checkDirection (int index, Direction d) {
        int neighborIndex;
        if (d.equals(Direction.NORTHWEST)) {
            neighborIndex = index - BOARD_SIZE - 1; //NORTHWEST
            return (neighborIndex > 0 && (neighborIndex + BOARD_SIZE) % BOARD_SIZE != BOARD_SIZE - 1);
        } else if (d.equals(Direction.NORTH)) {
            neighborIndex = index - BOARD_SIZE; //NORTH
            return (neighborIndex > 0);
        } else if (d.equals(Direction.NORTHEAST)) {
            neighborIndex = index - BOARD_SIZE + 1; //NORTHEAST
            return (neighborIndex > 0 && neighborIndex % BOARD_SIZE != 0);
        } else if (d.equals(Direction.WEST)) {
            neighborIndex = index - 1; //WEST
            return ((neighborIndex + BOARD_SIZE) % BOARD_SIZE != BOARD_SIZE - 1);
        } else if (d.equals(Direction.EAST)) {
            neighborIndex = index + 1; //EAST
            return (neighborIndex % BOARD_SIZE != 0);
        } else if (d.equals(Direction.SOUTHWEST)) {
            neighborIndex = index + BOARD_SIZE - 1; //SOUTHWEST
            return (neighborIndex < BOARD_SIZE * BOARD_SIZE && (neighborIndex + BOARD_SIZE) % BOARD_SIZE != BOARD_SIZE - 1);
        } else if (d.equals(Direction.SOUTH)) {
            neighborIndex = index + BOARD_SIZE; //SOUTH
            return (neighborIndex < BOARD_SIZE * BOARD_SIZE);
        } else if (d.equals(Direction.SOUTHEAST)) {
            neighborIndex = index + BOARD_SIZE + 1; //SOUTHEAST
            return (neighborIndex < BOARD_SIZE * BOARD_SIZE && neighborIndex % BOARD_SIZE != 0);
        } else {
            throw new Error("invalid direction 2");
        }
    }

    //TODO make sure these work
    public static int getIndexFromRowCol(int row, int col) {
        return BOARD_SIZE * row + col;
    }

    //TODO make sure these work
    private static int[] getRowColFromIndex(int index) {
        return new int[]{index / BOARD_SIZE, index % BOARD_SIZE};
    }

    public static int getRowFromIndex(int index) {
        return index / BOARD_SIZE;
    }

    public static int getColFromIndex(int index) {
        return index % BOARD_SIZE;
    }

    public Player getOpponent(Player p) {
        return p1.equals(p) ? p2 : p1;
    }

    public Player getP1() {
        return p1;
    }

    public Player getP2() {
        return p2;
    }

    public Player getWinner() {
        int p1Pieces = 0, p2Pieces = 0;
        for (Tile[] row : board) {
            for (Tile t : row) {
                if (t.getOccupied()) {
                    if (t.getOwner().equals(this.p1)) {
                        p1Pieces += 1;
                    } else {
                        p2Pieces += 1;
                    }
                }
            }
        }
        if (p1Pieces > p2Pieces) {
            return this.p1;
        } else if (p1Pieces < p2Pieces) {
            return this.p2;
        } else {
            return null;
        }
    }

    public String toString() {
        String output = " |";
        for (int i = 0; i < BOARD_SIZE; i++) {
            output += i + "|";
        }
        output += "\n";
        for (int i = 0; i < (BOARD_SIZE + 1) * 2; i++) {
            output += "-";
        }
        output += "\n";
        for (int row = 0; row < BOARD_SIZE; row++) {
            output += row + "|";
            for (int col = 0; col < BOARD_SIZE; col++) {
                output += (board[row][col].toString() + "|");
            }
            output += "\n";
        }
        for (int i = 0; i < (BOARD_SIZE + 1) * 2; i++) {
            output += "-";
        }
        return output;
    }

}
