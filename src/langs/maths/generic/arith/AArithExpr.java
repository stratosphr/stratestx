package langs.maths.generic.arith;

import com.microsoft.z3.ArithExpr;
import langs.maths.generic.AGenericTypeExpr;
import visitors.interfaces.ISMTEncoder;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:03
 */
public abstract class AArithExpr extends AGenericTypeExpr {

    @Override
    public abstract ArithExpr accept(ISMTEncoder encoder);

}
