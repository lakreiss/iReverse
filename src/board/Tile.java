package board;

import players.Player;

/**
 * Created by liamkreiss on 12/8/18.
 */
public class Tile {
    private boolean occupied;
    private Player owner;
    private int index;
    private Type type;

    public Tile(Tile t) {
        this.occupied = t.occupied;
        this.owner = t.owner;
        this.index = t.index;
        setType();
    }

    public Tile(int index) {
        this.occupied = false;
        this.owner = null;
        this.index = index;
        setType();
    }

    public Tile(int index, Player p) {
        this.occupied = true;
        this.owner = p;
        this.index = index;
        setType();
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

    //only applies to 8x8
    private void setType() {
        if (index == 0 || index == 7 || index == 56 || index == 63) {
            this.type = Type.A;
        } else if (index == 1 || index == 6 || index == 8 || index == 15 || index == 48 ||
                index == 55 || index == 57 || index == 62) {
            this.type = Type.B;
        } else if (index == 2 || index == 5 || index == 16 || index == 23 || index == 40 ||
                index == 47 || index == 58 || index == 61) {
            this.type = Type.C;
        } else if (index == 3 || index == 4 || index == 24 || index == 31 || index == 32 ||
                index == 39 || index == 59 || index == 60) {
            this.type = Type.D;
        } else if (index == 9 || index == 14 || index == 49 || index == 54) {
            this.type = Type.E;
        } else if (index == 10 || index == 13 || index == 17 || index == 22 || index == 41 ||
                index == 46 || index == 50 || index == 53) {
            this.type = Type.F;
        } else if (index == 11 || index == 12 || index == 25 || index == 30 || index == 33 ||
                index == 38 || index == 51 || index == 52) {
            this.type = Type.G;
        } else if (index == 18 || index == 21 || index == 42 || index == 45) {
            this.type = Type.H;
        } else if (index == 19 || index == 20 || index == 26 || index == 29 || index == 34 ||
                index == 37 || index == 43 || index == 44) {
            this.type = Type.I;
        } else if (index == 27 || index == 28 || index == 35 || index == 36) {
            this.type = Type.J;
        } else {
            throw new Error("illegal size");
        }
    }

    public Type getType() {
        return this.type;
    }

    public String toString() {
        if (occupied) {
            //TODO
            return owner.getName();
        } else {
            return " ";
        }
    }
}
