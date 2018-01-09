package langs.eventb.substitutions;

import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.AAssignable;
import langs.maths.generic.arith.literals.Var;
import langs.maths.generic.bool.ABoolExpr;
import visitors.interfaces.IObjectFormatter;

import java.util.LinkedHashSet;

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
    public ABoolExpr getPrd(LinkedHashSet<AAssignable> assignables) {
        return new Assignments(this).getPrd(assignables);
    }

    @Override
    public VarAssignment cloned() {
        return new VarAssignment(getAssignable().cloned(), getValue().cloned());
    }

}
