package algorithms.heuristics.relevance;

import langs.maths.generic.bool.ABoolExpr;

/**
 * Created by gvoiron on 19/12/17.
 * Time : 22:24
 */
public abstract class AAtomicRelevancePredicate extends ARelevancePredicate implements IVariantComputable {

    public AAtomicRelevancePredicate(ABoolExpr expr) {
        super(expr);
    }

    @Override
    public abstract AAtomicRelevancePredicate clone();

}
