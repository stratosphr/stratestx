package algorithms.heuristics.relevance.atomics;

import langs.maths.generic.arith.AAssignable;
import langs.maths.generic.bool.operators.LT;
import visitors.Primer;

/**
 * Created by gvoiron on 20/12/17.
 * Time : 18:11
 */
public abstract class ADecreases<Assignable extends AAssignable> extends AAtomicRelevancePredicate {

    protected final Assignable assignable;

    protected ADecreases(Assignable assignable) {
        super(new LT(assignable.accept(new Primer(1)), assignable));
        this.assignable = assignable;
    }

    public Assignable getAssignable() {
        return assignable;
    }

    @Override
    public abstract ADecreases<Assignable> clone();

}
