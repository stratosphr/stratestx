package langs.eventb.substitutions;

import langs.maths.generic.arith.AAssignable;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.operators.And;
import visitors.interfaces.IObjectFormatter;

import java.util.LinkedHashSet;

/**
 * Created by gvoiron on 02/12/17.
 * Time : 01:03
 */
public final class Select extends ASubstitution {

    private final ABoolExpr condition;
    private final ASubstitution substitution;

    public Select(ABoolExpr condition, ASubstitution substitution) {
        this.condition = condition;
        this.substitution = substitution;
    }

    public ABoolExpr getCondition() {
        return condition;
    }

    public ASubstitution getSubstitution() {
        return substitution;
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public ABoolExpr getPrd(LinkedHashSet<AAssignable> assignables) {
        return new And(
                condition,
                substitution.getPrd(assignables)
        );
    }

    @Override
    public ASubstitution clone() {
        return new Select(condition.clone(), substitution.clone());
    }

}
