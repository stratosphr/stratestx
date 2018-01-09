package langs.maths.generic;

import langs.maths.AExpr;
import visitors.interfaces.IPrimer;
import visitors.interfaces.ISMTEncodable;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 22:57
 */
public abstract class AGenericTypeExpr extends AExpr implements ISMTEncodable {

    @Override
    public abstract AGenericTypeExpr accept(IPrimer primer);

    @Override
    public abstract AGenericTypeExpr cloned();

}
