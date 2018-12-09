package board;

import players.Player;

/**
 * Created by liamkreiss on 12/8/18.
 */
public class Tile {
    private boolean occupied;
    private Player owner;
    private int index;

    public Tile(Tile t) {
        this.occupied = t.occupied;
        this.owner = t.owner;
        this.index = t.index;
    }

    public Tile(int index) {
        this.occupied = false;
        this.owner = null;
        this.index = index;
    }

    public Tile(int index, Player p) {
        this.occupied = true;
        this.owner = p;
        this.index = index;
    }

    public void setOwner(Player p) {
        this.owner = p;
        this.occupied = true;
    }

    public Player getOwner() {
        return this.owner;
    }

    public boolean getOccupied() {
        return this.occupied;
    }

    public int getIndex() {
        return this.index;
    }

    public String toString() {
        if (occupied) {
            //TODO
            return owner.getName();
        } else {
            return "x";
        }
    }
}
