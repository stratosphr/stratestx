package algorithms.heuristics.relevance;

import langs.eventb.Machine;
import langs.formal.graphs.ConcreteState;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.AAssignable;
import langs.maths.generic.arith.literals.AValue;
import langs.maths.generic.bool.operators.LT;
import visitors.Primer;

import java.util.LinkedHashMap;

/**
 * Created by gvoiron on 19/12/17.
 * Time : 22:33
 */
@SuppressWarnings("WeakerAccess")
public final class Decreases extends AAtomicRelevancePredicate {

    private AAssignable assignable;

    public Decreases(AAssignable assignable) {
        super(new LT(assignable.accept(new Primer(1)), assignable));
        this.assignable = assignable;
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
    public AAtomicRelevancePredicate clone() {
        return new Decreases(assignable.clone());
    }

}
