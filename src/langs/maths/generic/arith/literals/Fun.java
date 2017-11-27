package langs.maths.generic.arith.literals;

import langs.maths.generic.arith.AArithExpr;
import visitors.interfaces.IObjectFormatter;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:06
 */
public final class Fun extends AArithExpr {

    private final String name;
    private final AArithExpr parameter;

    public Fun(String name, AArithExpr parameter) {
        this.name = name;
        this.parameter = parameter;
    }

    @Override
    public String accept(IObjectFormatter visitor) {
        return visitor.visit(this);
    }

    public String getName() {
        return name;
    }

    public AArithExpr getParameter() {
        return parameter;
    }

}
