package algorithms.heuristics.relevance;

import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.AAssignable;
import langs.maths.generic.bool.operators.And;
import langs.maths.generic.bool.operators.Equals;
import visitors.Primer;

/**
 * Created by gvoiron on 19/12/17.
 * Time : 22:17
 */
@SuppressWarnings("WeakerAccess")
public abstract class AChanges<Assignable extends AAssignable> extends AAtomicRelevancePredicate {

    protected final Assignable assignable;
    protected final AArithExpr value;
    protected final AArithExpr value_;

    public AChanges(Assignable assignable, AArithExpr value, AArithExpr value_) {
        super(new And(new Equals(assignable, value), new Equals(assignable.accept(new Primer(1)), value_)));
        this.assignable = assignable;
        this.value = value;
        this.value_ = value_;
    }

    public final Assignable getAssignable() {
        return assignable;
    }

    public final AArithExpr getValue() {
        return value;
    }

    public final AArithExpr getValue_() {
        return value_;
    }

    @Override
    public abstract AChanges<Assignable> clone();

}
