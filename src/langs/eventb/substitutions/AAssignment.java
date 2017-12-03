package langs.eventb.substitutions;

import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.AAssignable;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.operators.Equals;
import visitors.Primer;

import java.util.LinkedHashSet;

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

    // TODO: The assignable should have the assignable prime level PLUS 1
    final ABoolExpr getPrdInAssignments(LinkedHashSet<AAssignable> assignables) {
        return new Equals(getAssignable().accept(new Primer(1)), getValue());
    }

    public final Assignable getAssignable() {
        return assignable;
    }

    public final AArithExpr getValue() {
        return value;
    }

}
