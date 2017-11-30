package langs.maths.generic.bool.operators;

import com.microsoft.z3.BoolExpr;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.ANaryBoolExpr;
import visitors.interfaces.IObjectFormatter;
import visitors.interfaces.IPrimer;
import visitors.interfaces.ISMTEncoder;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 22:55
 */
public final class Or extends ANaryBoolExpr<ABoolExpr> {

    public Or(ABoolExpr... operands) {
        super(operands);
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
    public Or accept(IPrimer primer) {
        return primer.visit(this);
    }

    @Override
    public Or clone() {
        return new Or(getOperands().stream().map(ABoolExpr::clone).toArray(ABoolExpr[]::new));
    }

}
