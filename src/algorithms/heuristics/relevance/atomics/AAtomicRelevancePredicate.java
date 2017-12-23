package algorithms.heuristics.relevance.atomics;

import algorithms.heuristics.relevance.ARelevancePredicate;
import algorithms.heuristics.relevance.IVariantComputable;
import langs.maths.generic.bool.ABoolExpr;

/**
 * Created by gvoiron on 19/12/17.
 * Time : 22:24
 */
public abstract class AAtomicRelevancePredicate extends ARelevancePredicate implements IVariantComputable {

    AAtomicRelevancePredicate(ABoolExpr expr) {
        super(expr);
    }

    @Override
    public abstract AAtomicRelevancePredicate clone();

}
