package algorithms.heuristics.relevance.atomics.funs;

import algorithms.heuristics.relevance.IVariantComputer;
import algorithms.heuristics.relevance.atomics.AAtomicRelevancePredicate;
import algorithms.heuristics.relevance.atomics.ADecreases;
import langs.eventb.Machine;
import langs.formal.graphs.ConcreteState;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.literals.AValue;
import langs.maths.generic.arith.literals.Fun;

import java.util.LinkedHashMap;

/**
 * Created by gvoiron on 19/12/17.
 * Time : 22:33
 */
@SuppressWarnings("WeakerAccess")
public final class FunDecreases extends ADecreases<Fun> {

    public FunDecreases(Fun assignable) {
        super(assignable);
    }

    @Override
    public AArithExpr getVInit(IVariantComputer computer, ConcreteState c, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine) {
        return computer.getVInit(this, c, variantsMapping, machine);
    }

    @Override
    public AArithExpr getV(IVariantComputer computer, ConcreteState c, ConcreteState c_, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine) {
        return computer.getV(this, c, c_, variantsMapping, machine);
    }

    @Override
    public FunDecreases clone() {
        return new FunDecreases(assignable.clone());
    }

}
