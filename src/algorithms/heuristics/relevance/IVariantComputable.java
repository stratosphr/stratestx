package algorithms.heuristics.relevance;

import algorithms.heuristics.relevance.atomics.AAtomicRelevancePredicate;
import langs.eventb.Machine;
import langs.formal.graphs.ConcreteState;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.literals.AValue;

import java.util.LinkedHashMap;

/**
 * Created by gvoiron on 20/12/17.
 * Time : 13:40
 */
public interface IVariantComputable {

    AArithExpr getVInit(IVariantComputer computer, ConcreteState c, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine);

    AArithExpr getV(IVariantComputer computer, ConcreteState c, ConcreteState c_, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine);

}
