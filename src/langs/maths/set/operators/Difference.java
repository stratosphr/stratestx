package langs.maths.set.operators;

import langs.maths.set.ANarySetExpr;
import langs.maths.set.ASetExpr;
import visitors.interfaces.IObjectFormatter;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 03:13
 */
public final class Difference extends ANarySetExpr {

    public Difference(ASetExpr... operands) {
        super(operands);
    }

    @Override
    public String accept(IObjectFormatter visitor) {
        return visitor.visit(this);
    }

}
