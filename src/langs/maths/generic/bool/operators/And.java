package langs.maths.generic.bool.operators;

import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.ANaryBoolExpr;
import visitors.interfaces.IObjectFormatter;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 22:55
 */
public final class And extends ANaryBoolExpr<ABoolExpr> {

    public And(ABoolExpr... operands) {
        super(operands);
    }

    @Override
    public String accept(IObjectFormatter visitor) {
        return visitor.visit(this);
    }

}
