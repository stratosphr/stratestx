package langs.maths.generic.arith;

import langs.maths.generic.AGenericTypeExpr;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 03:09
 */
public abstract class ABinaryArithExpr<Left extends AGenericTypeExpr, Right extends AGenericTypeExpr> extends AArithExpr {

    private final Left left;
    private final Right right;

    public ABinaryArithExpr(Left left, Right right) {
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
