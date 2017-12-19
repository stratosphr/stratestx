package utilities.tuples;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 12:28
 */
public final class Tuple<Left, Right> {

    private final Left left;
    private final Right right;

    public Tuple(Left left, Right right) {
        this.left = left;
        this.right = right;
    }

    public Left getLeft() {
        return left;
    }

    public Right getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "(" + left + ", " + right + ")";
    }

}
