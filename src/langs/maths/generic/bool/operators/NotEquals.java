package langs.maths.generic.bool.operators;

import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.bool.ABinaryBoolExpr;
import visitors.interfaces.IObjectFormatter;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:07
 */
public final class NotEquals extends ABinaryBoolExpr<AArithExpr, AArithExpr> {

    public NotEquals(AArithExpr left, AArithExpr right) {
        super(left, right);
    }

    @Override
    public String accept(IObjectFormatter visitor) {
        return visitor.visit(this);
    }

}
