package players;

import board.Board;

import javafx.scene.paint.Color;

/**
 * Created by liamkreiss on 12/8/18.
 */
public class Player {
    private String name;
    private Color color;

    public Player(boolean firstPlayer) {
        if (firstPlayer) {
            this.name = "B";
            this.color = Color.BLACK;
        } else {
            this.name = "W";
            this.color = Color.WHITE;
        }
    }

    public Player(Player curPlayer) {
        this.name = curPlayer.getName();
        this.color = curPlayer.getColor();
    }

    public String getName() {
        return this.name;
    }

    public Color getColor() {
        return color;
    }

    //returns the index of the move the player wants to make
    public int getMove(Board gameboard) {

        //TODO
        return -1;
    }
}
