package langs.maths.generic.arith.operators;

import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.ABinaryArithExpr;
import visitors.interfaces.IObjectFormatter;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 03:08
 */
public final class Mod extends ABinaryArithExpr<AArithExpr, AArithExpr> {

    public Mod(AArithExpr left, AArithExpr right) {
        super(left, right);
    }

    @Override
    public String accept(IObjectFormatter visitor) {
        return visitor.visit(this);
    }

}
