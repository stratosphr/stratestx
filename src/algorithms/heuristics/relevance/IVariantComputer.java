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
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.literals.AValue;

import java.util.LinkedHashMap;

/**
 * Created by gvoiron on 19/12/17.
 * Time : 22:46
 */
public interface IVariantComputer {

    AArithExpr getVInit(VarChanges varChanges, ConcreteState c, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine);

    AArithExpr getVInit(FunChanges funChanges, ConcreteState c, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine);

    AArithExpr getVInit(VarDecreases varDecreases, ConcreteState c, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine);

    AArithExpr getVInit(FunDecreases funDecreases, ConcreteState c, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine);

    AArithExpr getVInit(VarIncreases varIncreases, ConcreteState c, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine);

    AArithExpr getVInit(FunIncreases funIncreases, ConcreteState c, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine);

    AArithExpr getVInit(Conditions conditions, ConcreteState c, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine);

    AArithExpr getV(VarChanges varChanges, ConcreteState c, ConcreteState c_, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine);

    AArithExpr getV(FunChanges funChanges, ConcreteState c, ConcreteState c_, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine);

    AArithExpr getV(VarDecreases varDecreases, ConcreteState c, ConcreteState c_, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine);

    AArithExpr getV(FunDecreases funDecreases, ConcreteState c, ConcreteState c_, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine);

    AArithExpr getV(VarIncreases varIncreases, ConcreteState c, ConcreteState c_, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine);

    AArithExpr getV(FunIncreases funIncreases, ConcreteState c, ConcreteState c_, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine);

    AArithExpr getV(Conditions conditions, ConcreteState c, ConcreteState c_, LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping, Machine machine);

}
