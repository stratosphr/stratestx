package langs.eventb.substitutions;

import langs.maths.generic.arith.AAssignable;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.operators.And;
import langs.maths.generic.bool.operators.Implies;
import langs.maths.generic.bool.operators.Not;
import visitors.interfaces.IObjectFormatter;

import java.util.LinkedHashSet;

/**
 * Created by gvoiron on 02/12/17.
 * Time : 01:27
 */
public final class IfThenElse extends ASubstitution {

    private final ABoolExpr condition;
    private final ASubstitution thenPart;
    private final ASubstitution elsePart;

    public IfThenElse(ABoolExpr condition, ASubstitution thenPart, ASubstitution elsePart) {
        this.condition = condition;
        this.thenPart = thenPart;
        this.elsePart = elsePart;
    }

    public ABoolExpr getCondition() {
        return condition;
    }

    public ASubstitution getThenPart() {
        return thenPart;
    }

    public ASubstitution getElsePart() {
        return elsePart;
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public ABoolExpr getPrd(LinkedHashSet<AAssignable> assignables) {
        return new And(
                new Implies(condition, thenPart.getPrd(assignables)),
                new Implies(new Not(condition), elsePart.getPrd(assignables))
        );
    }

    @Override
    public IfThenElse clone() {
        return new IfThenElse(condition.clone(), thenPart.clone(), elsePart.clone());
    }

}
