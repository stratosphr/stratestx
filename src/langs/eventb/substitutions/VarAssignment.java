package langs.eventb.substitutions;

import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.literals.Var;
import visitors.interfaces.IObjectFormatter;

/**
 * Created by gvoiron on 02/12/17.
 * Time : 00:52
 */
public final class VarAssignment extends AAssignment<Var> {

    public VarAssignment(Var var, AArithExpr value) {
        super(var, value);
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public ASubstitution clone() {
        return new VarAssignment(getAssignable().clone(), getValue().clone());
    }

}
