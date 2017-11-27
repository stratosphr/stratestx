package langs.maths.generic.arith.operators;

import com.microsoft.z3.ArithExpr;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.AUnaryArithExpr;
import visitors.interfaces.IObjectFormatter;
import visitors.interfaces.ISMTEncoder;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:09
 */
public final class UMinus extends AUnaryArithExpr<AArithExpr> {

    public UMinus(AArithExpr operand) {
        super(operand);
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public ArithExpr accept(ISMTEncoder encoder) {
        return encoder.visit(this);
    }

}
