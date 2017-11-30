package langs.maths.generic.arith;

import com.microsoft.z3.IntExpr;
import langs.maths.generic.AGenericTypeExpr;
import visitors.interfaces.IPrimer;
import visitors.interfaces.ISMTEncoder;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:03
 */
public abstract class AArithExpr extends AGenericTypeExpr {

    @Override
    public abstract AArithExpr accept(IPrimer primer);

    @Override
    public abstract IntExpr accept(ISMTEncoder encoder);

    @Override
    public abstract AArithExpr clone();

}
