package langs.eventb.substitutions;

import langs.maths.generic.bool.ABoolExpr;
import visitors.interfaces.IObjectFormatter;

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
    public ASubstitution clone() {
        return new IfThenElse(condition.clone(), thenPart.clone(), elsePart.clone());
    }

}
