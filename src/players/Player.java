package players;

import gameplay.Board;

import java.awt.Color;

/**
 * Created by liamkreiss on 12/8/18.
 */
public class Player {
    private String name;
    private Color color;

    public Player(boolean firstPlayer) {
        if (firstPlayer) {
            this.name = "W";
            this.color = Color.WHITE;
        } else {
            this.name = "B";
            this.color = Color.BLACK;
        }
    }

    public Player(String name, boolean firstPlayer) {
        this.name = name;
        if (firstPlayer) {
            this.color = Color.WHITE;
        } else {
            this.color = Color.BLACK;
        }
    }

    public String getName() {
        return this.name;
    }

    //returns the index of the move the player wants to make
    public int getMove(Board gameboard) {

        //TODO
        return -1;
    }
}
