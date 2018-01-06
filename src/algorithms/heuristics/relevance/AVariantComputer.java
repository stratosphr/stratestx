package algorithms.heuristics.relevance;

import algorithms.heuristics.relevance.atomics.AAtomicRelevancePredicate;
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
import langs.maths.generic.arith.literals.Var;
import langs.maths.generic.bool.operators.And;
import langs.maths.generic.bool.operators.Equals;
import langs.maths.set.literals.Z;
import solvers.z3.Model;
import solvers.z3.Z3;
import solvers.z3.Z3Result;
import visitors.Primer;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * Created by gvoiron on 23/12/17.
 * Time : 15:49
 */
public abstract class AVariantComputer implements IVariantComputer {

    private Machine machine;

    public AVariantComputer(Machine machine) {
        this.machine = machine;
    }

    <Variant extends AArithExpr> Variant registerVInit(Variant variant, AAtomicRelevancePredicate atomicPredicate, ConcreteState c, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping) {
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

    <Variant extends AArithExpr> Variant registerV(Variant variant, AAtomicRelevancePredicate atomicPredicate, ConcreteState c, ConcreteState c_, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping) {
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

    public abstract AArithExpr getVInit(VarChanges varChanges, ConcreteState c, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine);

    public abstract AArithExpr getVInit(FunChanges funChanges, ConcreteState c, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine);

    public abstract AArithExpr getVInit(VarDecreases varDecreases, ConcreteState c, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine);

    public abstract AArithExpr getVInit(FunDecreases funDecreases, ConcreteState c, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine);

    public abstract AArithExpr getVInit(VarIncreases varIncreases, ConcreteState c, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine);

    public abstract AArithExpr getVInit(FunIncreases funIncreases, ConcreteState c, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine);

    public abstract AArithExpr getVInit(Conditions conditions, ConcreteState c, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine);

    public abstract AArithExpr getV(VarChanges varChanges, ConcreteState c, ConcreteState c_, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine);

    public abstract AArithExpr getV(FunChanges funChanges, ConcreteState c, ConcreteState c_, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine);

    public abstract AArithExpr getV(VarDecreases varDecreases, ConcreteState c, ConcreteState c_, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine);

    public abstract AArithExpr getV(FunDecreases funDecreases, ConcreteState c, ConcreteState c_, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine);

    public abstract AArithExpr getV(VarIncreases varIncreases, ConcreteState c, ConcreteState c_, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine);

    public abstract AArithExpr getV(FunIncreases funIncreases, ConcreteState c, ConcreteState c_, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine);

    public abstract AArithExpr getV(Conditions conditions, ConcreteState c, ConcreteState c_, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine);

}
