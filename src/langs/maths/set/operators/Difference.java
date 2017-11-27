package langs.maths.set.operators;

import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.operators.And;
import langs.maths.generic.bool.operators.InDomain;
import langs.maths.generic.bool.operators.Not;
import langs.maths.set.ANarySetExpr;
import langs.maths.set.ASetExpr;
import visitors.interfaces.IObjectFormatter;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 03:13
 */
public final class Difference extends ANarySetExpr {

    public Difference(ASetExpr... operands) {
        super(operands);
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public ABoolExpr getConstraint(AArithExpr expr) {
        return new And(new InDomain(expr, getOperands().get(0)), new And(getOperands().subList(1, getOperands().size()).stream().map(operand -> new Not(new InDomain(expr, operand))).toArray(ABoolExpr[]::new)));
    }

}
