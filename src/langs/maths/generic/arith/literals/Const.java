package langs.maths.generic.arith.literals;

import langs.maths.generic.arith.AArithExpr;
import visitors.interfaces.IObjectFormatter;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:06
 */
public final class Const extends AArithExpr {

    private final String name;

    public Const(String name) {
        this.name = name;
    }

    @Override
    public String accept(IObjectFormatter visitor) {
        return visitor.visit(this);
    }

    public String getName() {
        return name;
    }

}
