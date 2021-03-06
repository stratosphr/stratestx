package langs.maths.generic.bool.operators;

import com.microsoft.z3.BoolExpr;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.bool.ABinaryBoolExpr;
import visitors.interfaces.IObjectFormatter;
import visitors.interfaces.IPrimer;
import visitors.interfaces.ISMTEncoder;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:07
 */
public final class NotEquals extends ABinaryBoolExpr<AArithExpr, AArithExpr> {

    public NotEquals(AArithExpr left, AArithExpr right) {
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
    public NotEquals accept(IPrimer primer) {
        return primer.visit(this);
    }

    @Override
    public NotEquals cloned() {
        return new NotEquals(getLeft().cloned(), getRight().cloned());
    }

}
