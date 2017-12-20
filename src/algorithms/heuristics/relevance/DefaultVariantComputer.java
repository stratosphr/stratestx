package algorithms.heuristics.relevance;

import langs.eventb.Machine;
import langs.formal.graphs.ConcreteState;
import langs.maths.def.DefsRegister;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.literals.AValue;
import langs.maths.generic.arith.literals.Int;
import langs.maths.generic.arith.literals.Var;
import langs.maths.generic.arith.operators.Minus;
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
    public AArithExpr getVInit(Decreases decreases, ConcreteState c, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine) {
        throw new Error();
    }

    @Override
    public AArithExpr getVInit(Increases increases, ConcreteState c, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine) {
        throw new Error();
    }

    @Override
    public AArithExpr getVInit(Conditions conditions, ConcreteState c, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine) {
        throw new Error();
    }

    @Override
    public AArithExpr getV(VarChanges changes, ConcreteState c, ConcreteState c_, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine) {
        throw new Error();
    }

    @Override
    public AArithExpr getV(FunChanges changes, ConcreteState c, ConcreteState c_, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine) {
        return registerV(new Minus(variantsMapping.get(c).get(changes), new Int(1)), changes, c, c_, variantsMapping, machine);
    }

    @Override
    public AArithExpr getV(Decreases decreases, ConcreteState c, ConcreteState c_, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine) {
        throw new Error();
    }

    @Override
    public AArithExpr getV(Increases increases, ConcreteState c, ConcreteState c_, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine) {
        throw new Error();
    }

    @Override
    public AArithExpr getV(Conditions conditions, ConcreteState c, ConcreteState c_, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine) {
        throw new Error();
    }

}
