package langs.maths.generic.bool;

import langs.maths.AExpr;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:08
 */
public abstract class ABinaryBoolExpr<Left extends AExpr, Right extends AExpr> extends ABoolExpr {

    private final Left left;
    private final Right right;

    public ABinaryBoolExpr(Left left, Right right) {
        this.left = left;
        this.right = right;
    }

    public Left getLeft() {
        return left;
    }

    public Right getRight() {
        return right;
    }

}
