package langs.maths.set.operators;

import langs.maths.set.ANarySetExpr;
import langs.maths.set.ASetExpr;
import visitors.interfaces.IObjectFormatter;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:22
 */
public final class Intersection extends ANarySetExpr {

    public Intersection(ASetExpr... operands) {
        super(operands);
    }

    @Override
    public String accept(IObjectFormatter visitor) {
        return visitor.visit(this);
    }

}
