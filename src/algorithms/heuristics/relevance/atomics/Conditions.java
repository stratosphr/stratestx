package algorithms.heuristics.relevance.atomics;

import algorithms.heuristics.relevance.AVariantComputer;
import langs.formal.graphs.ConcreteState;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.bool.operators.And;

import java.util.Arrays;
import java.util.LinkedHashSet;

/**
 * Created by gvoiron on 19/12/17.
 * Time : 22:35
 */

public final class Conditions extends AAtomicRelevancePredicate {

    private final LinkedHashSet<Condition> conditions;

    public Conditions(Condition... conditions) {
        super(new And(conditions));
        this.conditions = new LinkedHashSet<>(Arrays.asList(conditions));
    }

    @Override
    public AArithExpr getVInit(AVariantComputer computer, ConcreteState c) {
        return computer.getVInit(this, c);
    }

    @Override
    public AArithExpr getV(AVariantComputer computer, ConcreteState c, ConcreteState c_) {
        return computer.getV(this, c, c_);
    }

    public LinkedHashSet<Condition> getConditions() {
        return conditions;
    }

    @Override
    public Conditions cloned() {
        return new Conditions(conditions.stream().map(Condition::cloned).toArray(Condition[]::new));
    }

}
