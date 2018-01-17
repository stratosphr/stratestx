package algorithms.heuristics.relevance;

import algorithms.heuristics.relevance.atomics.Condition;
import algorithms.heuristics.relevance.atomics.Conditions;
import algorithms.heuristics.relevance.atomics.funs.FunChanges;
import algorithms.heuristics.relevance.atomics.funs.FunDecreases;
import algorithms.heuristics.relevance.atomics.funs.FunIncreases;
import algorithms.heuristics.relevance.atomics.vars.VarChanges;
import algorithms.heuristics.relevance.atomics.vars.VarDecreases;
import algorithms.heuristics.relevance.atomics.vars.VarIncreases;
import langs.eventb.Machine;
import langs.formal.graphs.ConcreteState;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.literals.AValue;
import langs.maths.generic.arith.literals.Int;
import langs.maths.generic.arith.operators.Minus;
import langs.maths.generic.arith.operators.Times;
import langs.maths.generic.bool.operators.And;
import langs.maths.set.AFiniteSetExpr;
import solvers.z3.Z3;

/**
 * Created by gvoiron on 20/12/17.
 * Time : 13:53
 */
public final class ReducedVariantComputer extends AVariantComputer {

    public ReducedVariantComputer(Machine machine, RelevancePredicate relevancePredicate) {
        super(machine, relevancePredicate);
    }

    @Override
    public AArithExpr getVInit(VarChanges varChanges, ConcreteState c) {
        return new Int(((AFiniteSetExpr) machine.getDefsRegister().getVarsDefs().get(varChanges.getAssignable().getName())).getElementsValues(machine.getDefsRegister()).size());
    }

    @Override
    public AArithExpr getVInit(FunChanges funChanges, ConcreteState c) {
        return new Int(((AFiniteSetExpr) machine.getDefsRegister().getFunsDefs().get(funChanges.getAssignable().getName()).getRight()).getElementsValues(machine.getDefsRegister()).size());
    }

    @Override
    public AArithExpr getVInit(VarDecreases varDecreases, ConcreteState c) {
        return new Times(new Int(2), new Int(getRelevantEvents().size()), new Int(
                ((AFiniteSetExpr) machine.getDefsRegister().getVarsDefs().get(varDecreases.getAssignable().getName())).getElementsValues(machine.getDefsRegister()).stream().mapToInt(AValue::getValue).max().orElse(0)
        ));
    }

    @Override
    public AArithExpr getVInit(FunDecreases funDecreases, ConcreteState c) {
        return new Times(new Int(2), new Int(getRelevantEvents().size()), new Int(
                ((AFiniteSetExpr) machine.getDefsRegister().getFunsDefs().get(funDecreases.getAssignable().getName()).getRight()).getElementsValues(machine.getDefsRegister()).stream().mapToInt(AValue::getValue).max().orElse(0)
        ));
    }

    @Override
    public AArithExpr getVInit(VarIncreases varIncreases, ConcreteState c) {
        return new Times(new Int(2), new Int(getRelevantEvents().size()), new Int(
                ((AFiniteSetExpr) machine.getDefsRegister().getVarsDefs().get(varIncreases.getAssignable().getName())).getElementsValues(machine.getDefsRegister()).stream().mapToInt(AValue::getValue).max().orElse(0)
        ));
    }

    @Override
    public AArithExpr getVInit(FunIncreases funIncreases, ConcreteState c) {
        return new Times(new Int(2), new Int(getRelevantEvents().size()), new Int(
                ((AFiniteSetExpr) machine.getDefsRegister().getFunsDefs().get(funIncreases.getAssignable().getName()).getRight()).getElementsValues(machine.getDefsRegister()).stream().mapToInt(AValue::getValue).max().orElse(0)
        ));
    }

    @Override
    public AArithExpr getVInit(Conditions conditions, ConcreteState c) {
        for (Condition condition : conditions.getConditions()) {
            if (Z3.checkSAT(new And(
                    machine.getInvariant(),
                    c,
                    condition.getCondition()
            ), machine.getDefsRegister()).isSAT()) {
                return condition.getThenPart().getVInit(this, c);
            }
        }
        throw new Error("At least one implication condition should be satisfiable with regards to \"" + c + "\" in the following conditional relevance predicate:\n" + conditions + "");
    }

    @Override
    public AArithExpr getV(VarChanges varChanges, ConcreteState c, ConcreteState c_) {
        return new Minus(variantsMapping.get(c).get(varChanges), new Int(1));
    }

    @Override
    public AArithExpr getV(FunChanges funChanges, ConcreteState c, ConcreteState c_) {
        return new Minus(variantsMapping.get(c).get(funChanges), new Int(1));
    }

    @Override
    public AArithExpr getV(VarDecreases varDecreases, ConcreteState c, ConcreteState c_) {
        return new Minus(variantsMapping.get(c).get(varDecreases), new Minus(new Int(c.getMapping().get(varDecreases.getAssignable()).getValue()), new Int(c_.getMapping().get(varDecreases.getAssignable()).getValue())));
    }

    @Override
    public AArithExpr getV(FunDecreases funDecreases, ConcreteState c, ConcreteState c_) {
        return new Minus(variantsMapping.get(c).get(funDecreases), new Minus(new Int(c.getMapping().get(funDecreases.getAssignable()).getValue()), new Int(c_.getMapping().get(funDecreases.getAssignable()).getValue())));
    }

    @Override
    public AArithExpr getV(VarIncreases varIncreases, ConcreteState c, ConcreteState c_) {
        return new Minus(variantsMapping.get(c).get(varIncreases), new Minus(new Int(c_.getMapping().get(varIncreases.getAssignable()).getValue()), new Int(c.getMapping().get(varIncreases.getAssignable()).getValue())));
    }

    @Override
    public AArithExpr getV(FunIncreases funIncreases, ConcreteState c, ConcreteState c_) {
        return new Minus(variantsMapping.get(c).get(funIncreases), new Minus(new Int(c_.getMapping().get(funIncreases.getAssignable()).getValue()), new Int(c.getMapping().get(funIncreases.getAssignable()).getValue())));
    }

    @Override
    public AArithExpr getV(Conditions conditions, ConcreteState c, ConcreteState c_) {
        return new Minus(variantsMapping.get(c).get(conditions), new Int(1));
    }

}
