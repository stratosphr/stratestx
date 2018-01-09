package algorithms.heuristics.relevance.atomics;

import langs.maths.generic.arith.AAssignable;
import langs.maths.generic.bool.operators.GT;
import visitors.Primer;

/**
 * Created by gvoiron on 20/12/17.
 * Time : 18:11
 */
public abstract class AIncreases<Assignable extends AAssignable> extends AAtomicRelevancePredicate {

    protected final Assignable assignable;

    protected AIncreases(Assignable assignable) {
        super(new GT(assignable.accept(new Primer(1)), assignable));
        this.assignable = assignable;
    }

    public Assignable getAssignable() {
        return assignable;
    }

    @Override
    public abstract AIncreases<Assignable> cloned();

}
