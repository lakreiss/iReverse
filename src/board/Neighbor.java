package board;

/**
 * Created by liamkreiss on 12/9/18.
 */
public class Neighbor extends Tile {
    private Direction direction;

    public Neighbor(Direction dir, Tile tile) {
        super(tile);
        this.direction = dir;
    }

    public Direction getDirection() {
        return this.direction;
    }
}
