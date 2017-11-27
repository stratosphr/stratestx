package langs.maths.generic.arith.literals;

import langs.maths.generic.arith.AArithExpr;
import visitors.interfaces.IObjectFormatter;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 00:20
 */
public final class Int extends AArithExpr {

    private int value;

    public Int(int value) {
        this.value = value;
    }

    @Override
    public String accept(IObjectFormatter visitor) {
        return visitor.visit(this);
    }

    public Integer getValue() {
        return value;
    }

}
