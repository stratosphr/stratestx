package langs.eventb.substitutions;

import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.literals.Fun;
import visitors.interfaces.IObjectFormatter;

/**
 * Created by gvoiron on 02/12/17.
 * Time : 00:55
 */
public final class FunAssignment extends AAssignment<Fun> {

    public FunAssignment(Fun fun, AArithExpr value) {
        super(fun, value);
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public ASubstitution clone() {
        return new FunAssignment(getAssignable().clone(), getValue().clone());
    }

}
