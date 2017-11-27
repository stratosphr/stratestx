package langs.maths.set.operators;

import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.operators.And;
import langs.maths.generic.bool.operators.InDomain;
import langs.maths.set.ANarySetExpr;
import langs.maths.set.ASetExpr;
import visitors.interfaces.IObjectFormatter;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:22
 */
public final class Intersection extends ANarySetExpr {

    public Intersection(ASetExpr... operands) {
        super(operands);
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public ABoolExpr getConstraint(AArithExpr expr) {
        return new And(getOperands().stream().map(operand -> new InDomain(expr, operand)).toArray(ABoolExpr[]::new));
    }

}
