package langs.maths.generic.bool;

import com.microsoft.z3.BoolExpr;
import langs.maths.generic.AGenericTypeExpr;
import visitors.interfaces.ISMTEncoder;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 22:56
 */
public abstract class ABoolExpr extends AGenericTypeExpr {

    @Override
    public abstract BoolExpr accept(ISMTEncoder encoder);

    @Override
    public abstract ABoolExpr clone();

}
