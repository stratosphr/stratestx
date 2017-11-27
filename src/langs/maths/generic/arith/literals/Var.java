package langs.maths.generic.arith.literals;

import langs.maths.generic.arith.AArithExpr;
import visitors.interfaces.IObjectFormatter;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:06
 */
public final class Var extends AArithExpr {

    private String name;

    public Var(String name) {
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
