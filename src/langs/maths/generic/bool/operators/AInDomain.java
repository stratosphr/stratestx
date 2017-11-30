package langs.maths.generic.bool.operators;

import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.bool.ABinaryBoolExpr;
import langs.maths.set.ASetExpr;
import visitors.interfaces.IPrimer;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 01:09
 */
public abstract class AInDomain<Left extends AArithExpr> extends ABinaryBoolExpr<Left, ASetExpr> {

    AInDomain(Left left, ASetExpr domain) {
        super(left, domain);
    }

    @Override
    public abstract AInDomain<Left> accept(IPrimer primer);

    @Override
    public abstract AInDomain<Left> clone();

}
