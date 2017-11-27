package langs.maths.generic.arith.operators;

import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.AUnaryArithExpr;
import visitors.interfaces.IObjectFormatter;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:09
 */
public final class UMinus extends AUnaryArithExpr<AArithExpr> {

    public UMinus(AArithExpr operand) {
        super(operand);
    }

    @Override
    public String accept(IObjectFormatter visitor) {
        return visitor.visit(this);
    }

}
