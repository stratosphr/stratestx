package algorithms.heuristics.relevance.atomics.funs;

import algorithms.heuristics.relevance.IVariantComputer;
import algorithms.heuristics.relevance.atomics.AAtomicRelevancePredicate;
import algorithms.heuristics.relevance.atomics.AChanges;
import langs.eventb.Machine;
import langs.formal.graphs.ConcreteState;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.literals.AValue;
import langs.maths.generic.arith.literals.Fun;

import java.util.LinkedHashMap;

/**
 * Created by gvoiron on 20/12/17.
 * Time : 14:17
 */
@SuppressWarnings("WeakerAccess")
public final class FunChanges extends AChanges<Fun> {

    public FunChanges(Fun fun, AArithExpr value, AArithExpr value_) {
        super(fun, value, value_);
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
    public FunChanges clone() {
        return new FunChanges(assignable.clone(), value.clone(), value_.clone());
    }

}
