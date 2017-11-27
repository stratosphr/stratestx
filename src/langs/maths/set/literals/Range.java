package langs.maths.set.literals;

import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.operators.And;
import langs.maths.generic.bool.operators.GEQ;
import langs.maths.generic.bool.operators.LEQ;
import langs.maths.set.ASetExpr;
import visitors.interfaces.IObjectFormatter;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:18
 */
public final class Range extends ASetExpr {

    private final AArithExpr lowerBound;
    private final AArithExpr upperBound;

    public Range(AArithExpr lowerBound, AArithExpr upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    public AArithExpr getLowerBound() {
        return lowerBound;
    }

    public AArithExpr getUpperBound() {
        return upperBound;
    }

    @Override
    public ABoolExpr getConstraint(AArithExpr expr) {
        return new And(new GEQ(expr, lowerBound), new LEQ(expr, upperBound));
    }

}
