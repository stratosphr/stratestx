package langs.maths.set;

import langs.maths.AExpr;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.bool.ABoolExpr;
import visitors.interfaces.IPrimer;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:18
 */
public abstract class ASetExpr extends AExpr {

    @Override
    public abstract ASetExpr accept(IPrimer primer);

    public abstract ABoolExpr getConstraint(AArithExpr expr);

    @Override
    public abstract ASetExpr clone();

}
