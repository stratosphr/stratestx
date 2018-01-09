package langs.maths.generic.bool.operators;

import com.microsoft.z3.BoolExpr;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.bool.ABinaryBoolExpr;
import visitors.interfaces.IObjectFormatter;
import visitors.interfaces.IPrimer;
import visitors.interfaces.ISMTEncoder;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:02
 */
public final class GEQ extends ABinaryBoolExpr<AArithExpr, AArithExpr> {

    public GEQ(AArithExpr left, AArithExpr right) {
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
    public GEQ accept(IPrimer primer) {
        return primer.visit(this);
    }

    @Override
    public GEQ cloned() {
        return new GEQ(getLeft().cloned(), getRight().cloned());
    }

}
