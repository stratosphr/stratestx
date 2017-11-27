package langs.maths.generic.arith.operators;

import com.microsoft.z3.ArithExpr;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.ABinaryArithExpr;
import visitors.interfaces.IObjectFormatter;
import visitors.interfaces.ISMTEncoder;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 03:08
 */
public final class Mod extends ABinaryArithExpr<AArithExpr, AArithExpr> {

    public Mod(AArithExpr left, AArithExpr right) {
        super(left, right);
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
