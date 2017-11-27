package langs.maths.generic.bool.literals;

import langs.maths.generic.bool.ABoolExpr;
import visitors.interfaces.IObjectFormatter;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:01
 */
public final class False extends ABoolExpr {

    @Override
    public String accept(IObjectFormatter visitor) {
        return visitor.visit(this);
    }

}
