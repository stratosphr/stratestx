package langs.maths.generic.bool.operators;

import com.microsoft.z3.BoolExpr;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.AUnaryBoolExpr;
import visitors.interfaces.IObjectFormatter;
import visitors.interfaces.IPrimer;
import visitors.interfaces.ISMTEncoder;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 22:55
 */
public final class Not extends AUnaryBoolExpr<ABoolExpr> {

    public Not(ABoolExpr operand) {
        super(operand);
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
    public Not accept(IPrimer primer) {
        return primer.visit(this);
    }

    @Override
    public Not clone() {
        return new Not(getOperand().clone());
    }

}
