package langs.maths.generic.bool.operators;

import com.microsoft.z3.BoolExpr;
import langs.maths.generic.bool.ABinaryBoolExpr;
import langs.maths.generic.bool.ABoolExpr;
import visitors.interfaces.IObjectFormatter;
import visitors.interfaces.IPrimer;
import visitors.interfaces.ISMTEncoder;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:15
 */
public final class Implies extends ABinaryBoolExpr<ABoolExpr, ABoolExpr> {

    public Implies(ABoolExpr left, ABoolExpr right) {
        super(left, right);
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public BoolExpr accept(ISMTEncoder encoder) {
        return encoder.visit(this);
    }

    @Override
    public Implies accept(IPrimer primer) {
        return primer.visit(this);
    }

    @Override
    public Implies cloned() {
        return new Implies(getLeft().cloned(), getRight().cloned());
    }

}
