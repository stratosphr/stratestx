package langs.maths.generic.arith.operators;

import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.ANaryArithExpr;
import visitors.interfaces.IObjectFormatter;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:09
 */
public final class Minus extends ANaryArithExpr<AArithExpr> {

    public Minus(AArithExpr... operands) {
        super(operands);
    }

    @Override
    public String accept(IObjectFormatter visitor) {
        return visitor.visit(this);
    }

}
