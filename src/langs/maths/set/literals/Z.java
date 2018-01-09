package langs.maths.set.literals;

import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.literals.True;
import langs.maths.set.AInfiniteSetExpr;
import visitors.interfaces.IObjectFormatter;
import visitors.interfaces.IPrimer;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 13:41
 */
public final class Z extends AInfiniteSetExpr {

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public ABoolExpr getConstraint(AArithExpr expr) {
        return new True();
    }

    @Override
    public Z accept(IPrimer primer) {
        return primer.visit(this);
    }

    @Override
    public Z cloned() {
        return new Z();
    }

}
