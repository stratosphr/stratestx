package algorithms.heuristics.relevance;

import algorithms.heuristics.relevance.atomics.AAtomicRelevancePredicate;
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
import langs.maths.def.DefsRegister;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.literals.AValue;
import langs.maths.generic.arith.literals.Int;
import langs.maths.generic.arith.literals.Var;
import langs.maths.generic.arith.operators.Minus;
import langs.maths.generic.arith.operators.Times;
import langs.maths.generic.bool.operators.And;
import langs.maths.generic.bool.operators.Equals;
import langs.maths.set.AFiniteSetExpr;
import langs.maths.set.literals.Z;
import solvers.z3.Model;
import solvers.z3.Z3;
import solvers.z3.Z3Result;
import visitors.Primer;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * Created by gvoiron on 20/12/17.
 * Time : 13:53
 */
public final class DefaultVariantComputer implements IVariantComputer {

    // TODO: This variant computer should take into account the number of relevant events

    private <Variant extends AArithExpr> Variant registerVInit(Variant variant, AAtomicRelevancePredicate atomicPredicate, ConcreteState c, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine) {
        DefsRegister tmpDefsRegister = new DefsRegister(machine.getDefsRegister());
        Var variantVar = new Var("variant!");
        tmpDefsRegister.getVarsDefs().put(variantVar.getName(), new Z());
        Z3Result result = Z3.checkSAT(new And(
                machine.getInvariant(),
                c,
                new Equals(variantVar, variant)
        ), tmpDefsRegister);
        Model model = result.getModel(new LinkedHashSet<>(Collections.singleton(variantVar)));
        variantsMapping.get(c).put(atomicPredicate, model.get(variantVar));
        return variant;
    }

    private <Variant extends AArithExpr> Variant registerV(Variant variant, AAtomicRelevancePredicate atomicPredicate, ConcreteState c, ConcreteState c_, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine) {
        DefsRegister tmpDefsRegister = new DefsRegister(machine.getDefsRegister());
        Var variantVar = new Var("variant!");
        tmpDefsRegister.getVarsDefs().put(variantVar.getName(), new Z());
        Z3Result result = Z3.checkSAT(new And(
                machine.getInvariant(),
                machine.getInvariant().accept(new Primer(1)),
                c,
                c_.accept(new Primer(1)),
                new Equals(variantVar, variant)
        ), tmpDefsRegister);
        Model model = result.getModel(new LinkedHashSet<>(Collections.singleton(variantVar)));
        variantsMapping.get(c_).put(atomicPredicate, model.get(variantVar));
        return variant;
    }

    @Override
    public AArithExpr getVInit(VarChanges varChanges, ConcreteState c, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine) {
        return registerVInit(new Int(((AFiniteSetExpr) machine.getDefsRegister().getVarsDefs().get(varChanges.getAssignable().getName())).getElementsValues(machine.getDefsRegister()).size()), varChanges, c, variantsMapping, machine);
    }

    @Override
    public AArithExpr getVInit(FunChanges funChanges, ConcreteState c, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine) {
        return registerVInit(new Int(((AFiniteSetExpr) machine.getDefsRegister().getFunsDefs().get(funChanges.getAssignable().getName()).getRight()).getElementsValues(machine.getDefsRegister()).size()), funChanges, c, variantsMapping, machine);
    }

    @Override
    public AArithExpr getVInit(VarDecreases varDecreases, ConcreteState c, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine) {
        return registerVInit(new Times(new Int(2), new Int(5), new Int(
                ((AFiniteSetExpr) machine.getDefsRegister().getVarsDefs().get(varDecreases.getAssignable().getName())).getElementsValues(machine.getDefsRegister()).stream().mapToInt(AValue::getValue).max().orElse(0)
        )), varDecreases, c, variantsMapping, machine);
    }

    @Override
    public AArithExpr getVInit(FunDecreases funDecreases, ConcreteState c, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine) {
        return registerVInit(new Times(new Int(2), new Int(
                ((AFiniteSetExpr) machine.getDefsRegister().getFunsDefs().get(funDecreases.getAssignable().getName()).getRight()).getElementsValues(machine.getDefsRegister()).stream().mapToInt(AValue::getValue).max().orElse(0)
        )), funDecreases, c, variantsMapping, machine);
    }

    @Override
    public AArithExpr getVInit(VarIncreases varIncreases, ConcreteState c, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine) {
        return registerVInit(new Times(new Int(2), new Int(
                ((AFiniteSetExpr) machine.getDefsRegister().getVarsDefs().get(varIncreases.getAssignable().getName())).getElementsValues(machine.getDefsRegister()).stream().mapToInt(AValue::getValue).max().orElse(0)
        )), varIncreases, c, variantsMapping, machine);
    }

    @Override
    public AArithExpr getVInit(FunIncreases funIncreases, ConcreteState c, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine) {
        return registerVInit(new Times(new Int(2), new Int(
                ((AFiniteSetExpr) machine.getDefsRegister().getFunsDefs().get(funIncreases.getAssignable().getName()).getRight()).getElementsValues(machine.getDefsRegister()).stream().mapToInt(AValue::getValue).max().orElse(0)
        )), funIncreases, c, variantsMapping, machine);
    }

    @Override
    public AArithExpr getVInit(Conditions conditions, ConcreteState c, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine) {
        for (Condition condition : conditions.getConditions()) {
            if (Z3.checkSAT(new And(
                    machine.getInvariant(),
                    c,
                    condition.getCondition()
            ), machine.getDefsRegister()).isSAT()) {
                return registerVInit(condition.getThenPart().getVInit(this, c, variantsMapping, machine), conditions, c, variantsMapping, machine);
            }
        }
        throw new Error("At least one implication condition should be satisfiable with regards to \"" + c + "\" in the following conditional relevance predicate:\n" + conditions + "");
    }

    @Override
    public AArithExpr getV(VarChanges varChanges, ConcreteState c, ConcreteState c_, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine) {
        return registerV(new Minus(variantsMapping.get(c).get(varChanges), new Int(1)), varChanges, c, c_, variantsMapping, machine);
    }

    @Override
    public AArithExpr getV(FunChanges funChanges, ConcreteState c, ConcreteState c_, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine) {
        return registerV(new Minus(variantsMapping.get(c).get(funChanges), new Int(1)), funChanges, c, c_, variantsMapping, machine);
    }

    @Override
    public AArithExpr getV(VarDecreases varDecreases, ConcreteState c, ConcreteState c_, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine) {
        return new Minus(variantsMapping.get(c).get(varDecreases), new Int(1));
    }

    @Override
    public AArithExpr getV(FunDecreases funDecreases, ConcreteState c, ConcreteState c_, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine) {
        return new Minus(variantsMapping.get(c).get(funDecreases), new Int(1));
    }

    @Override
    public AArithExpr getV(VarIncreases varIncreases, ConcreteState c, ConcreteState c_, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine) {
        return new Minus(variantsMapping.get(c).get(varIncreases), new Int(1));
    }

    @Override
    public AArithExpr getV(FunIncreases funIncreases, ConcreteState c, ConcreteState c_, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine) {
        return new Minus(variantsMapping.get(c).get(funIncreases), new Int(1));
    }

    @Override
    public AArithExpr getV(Conditions conditions, ConcreteState c, ConcreteState c_, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine) {
        return new Minus(variantsMapping.get(c).get(conditions), new Int(1));
    }

}
