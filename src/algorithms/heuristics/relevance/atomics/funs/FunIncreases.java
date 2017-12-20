package algorithms.heuristics.relevance.atomics.funs;

import algorithms.heuristics.relevance.IVariantComputer;
import algorithms.heuristics.relevance.atomics.AAtomicRelevancePredicate;
import algorithms.heuristics.relevance.atomics.AIncreases;
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
public final class FunIncreases extends AIncreases<Fun> {

    public FunIncreases(Fun fun) {
        super(fun);
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
    public FunIncreases clone() {
        return new FunIncreases(assignable.clone());
    }

}
