package algorithms.heuristics.relevance.atomics;

import algorithms.heuristics.relevance.IVariantComputer;
import langs.eventb.Machine;
import langs.formal.graphs.ConcreteState;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.literals.AValue;
import langs.maths.generic.bool.operators.And;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * Created by gvoiron on 19/12/17.
 * Time : 22:35
 */
@SuppressWarnings("WeakerAccess")
public final class Conditions extends AAtomicRelevancePredicate {

    private final LinkedHashSet<Condition> conditions;

    public Conditions(Condition... conditions) {
        super(new And(conditions));
        this.conditions = new LinkedHashSet<>(Arrays.asList(conditions));
    }

    @Override
    public AArithExpr getVInit(IVariantComputer computer, ConcreteState c, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine) {
        return computer.getVInit(this, c, variantsMapping, machine);
    }

    @Override
    public AArithExpr getV(IVariantComputer computer, ConcreteState c, ConcreteState c_, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine) {
        return computer.getV(this, c, c_, variantsMapping, machine);
    }

    public LinkedHashSet<Condition> getConditions() {
        return conditions;
    }

    @Override
    public Conditions clone() {
        return new Conditions(conditions.stream().map(Condition::clone).toArray(Condition[]::new));
    }

}
