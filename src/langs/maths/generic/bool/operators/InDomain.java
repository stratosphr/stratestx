package langs.maths.generic.bool.operators;

import com.microsoft.z3.BoolExpr;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.set.ASetExpr;
import visitors.interfaces.IObjectFormatter;
import visitors.interfaces.IPrimer;
import visitors.interfaces.ISMTEncoder;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:19
 */
public final class InDomain extends AInDomain<AArithExpr> {

    public InDomain(AArithExpr left, ASetExpr right) {
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
    public InDomain accept(IPrimer primer) {
        return primer.visit(this);
    }

    @Override
    public InDomain clone() {
        return new InDomain(getLeft().clone(), getRight().clone());
    }

}