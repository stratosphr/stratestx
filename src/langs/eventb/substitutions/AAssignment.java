package langs.eventb.substitutions;

import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.AAssignable;

/**
 * Created by gvoiron on 02/12/17.
 * Time : 00:49
 */
public abstract class AAssignment<Assignable extends AAssignable> extends ASubstitution {

    private final Assignable assignable;
    private final AArithExpr value;

    AAssignment(Assignable assignable, AArithExpr value) {
        this.assignable = assignable;
        this.value = value;
    }

    public Assignable getAssignable() {
        return assignable;
    }

    public AArithExpr getValue() {
        return value;
    }

}
