package board;

/**
 * Created by liamkreiss on 12/9/18.
 */
public enum Type {
    A(0), B(1), C(2), D(3), E(4), F(5), G(6), H(7), I(8), J(9);

    private int numVal;

    Type(int numVal) {
        this.numVal = numVal;
    }

    public int getNumVal() {
        return numVal;
    }
}
