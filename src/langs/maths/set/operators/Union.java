package langs.maths.set.operators;

import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.operators.InDomain;
import langs.maths.generic.bool.operators.Or;
import langs.maths.set.ANarySetExpr;
import langs.maths.set.ASetExpr;
import visitors.interfaces.IObjectFormatter;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:22
 */
public final class Union extends ANarySetExpr {

    public Union(ASetExpr... operands) {
        super(operands);
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public ABoolExpr getConstraint(AArithExpr expr) {
        return new Or(getOperands().stream().map(operand -> new InDomain(expr, operand)).toArray(ABoolExpr[]::new));
    }

    @Override
    public Union clone() {
        return new Union(getOperands().stream().map(ASetExpr::clone).toArray(ASetExpr[]::new));
    }

}
