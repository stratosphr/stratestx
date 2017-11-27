package langs.maths.generic.bool.operators;

import langs.maths.generic.arith.AArithExpr;
import langs.maths.set.ASetExpr;
import visitors.interfaces.IObjectFormatter;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:19
 */
public final class InDomain extends AInDomain<AArithExpr> {

    public InDomain(AArithExpr left, ASetExpr right) {
        super(left, right);
    }

    @Override
    public String accept(IObjectFormatter visitor) {
        return visitor.visit(this);
    }

}
