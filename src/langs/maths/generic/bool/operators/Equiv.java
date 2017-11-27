package langs.maths.generic.bool.operators;

import langs.maths.generic.bool.ABinaryBoolExpr;
import langs.maths.generic.bool.ABoolExpr;
import visitors.interfaces.IObjectFormatter;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:15
 */
public final class Equiv extends ABinaryBoolExpr<ABoolExpr, ABoolExpr> {

    public Equiv(ABoolExpr left, ABoolExpr right) {
        super(left, right);
    }

    @Override
    public String accept(IObjectFormatter visitor) {
        return visitor.visit(this);
    }

}
