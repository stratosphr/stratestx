package langs.maths.generic.arith.operators;

import com.microsoft.z3.IntExpr;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.ANaryArithExpr;
import visitors.interfaces.IObjectFormatter;
import visitors.interfaces.IPrimer;
import visitors.interfaces.ISMTEncoder;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:09
 */
public final class Times extends ANaryArithExpr<AArithExpr> {

    public Times(AArithExpr... operands) {
        super(operands);
    }

    @Override
    public Times accept(IPrimer primer) {
        return primer.visit(this);
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public IntExpr accept(ISMTEncoder encoder) {
        return encoder.visit(this);
    }

    @Override
    public Times cloned() {
        return new Times(getOperands().stream().map(AArithExpr::cloned).toArray(AArithExpr[]::new));
    }

}
