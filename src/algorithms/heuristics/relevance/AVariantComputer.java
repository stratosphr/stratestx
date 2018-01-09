package algorithms.heuristics.relevance;

import algorithms.heuristics.relevance.atomics.AAtomicRelevancePredicate;
import algorithms.heuristics.relevance.atomics.Conditions;
import algorithms.heuristics.relevance.atomics.funs.FunChanges;
import algorithms.heuristics.relevance.atomics.funs.FunDecreases;
import algorithms.heuristics.relevance.atomics.funs.FunIncreases;
import algorithms.heuristics.relevance.atomics.vars.VarChanges;
import algorithms.heuristics.relevance.atomics.vars.VarDecreases;
import algorithms.heuristics.relevance.atomics.vars.VarIncreases;
import langs.eventb.Event;
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
import java.util.stream.Collectors;

/**
 * Created by gvoiron on 23/12/17.
 * Time : 15:49
 */
public abstract class AVariantComputer {

    final Machine machine;
    private final RelevancePredicate relevancePredicate;
    final LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping;
    private LinkedHashSet<Event> relevantEvents;
    private boolean preComputed;


    AVariantComputer(Machine machine, RelevancePredicate relevancePredicate) {
        this.machine = machine;
        this.relevancePredicate = relevancePredicate;
        this.variantsMapping = new LinkedHashMap<>();
        this.relevantEvents = null;
        this.preComputed = false;
    }

    private <Variant extends AArithExpr> Variant registerVInit(Variant variant, AAtomicRelevancePredicate atomicPredicate, ConcreteState c) {
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

    private <Variant extends AArithExpr> Variant registerV(Variant variant, AAtomicRelevancePredicate atomicPredicate, ConcreteState c, ConcreteState c_) {
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


    @SuppressWarnings({"EmptyMethod", "WeakerAccess"})
    protected void preCompute() {
    }

    public AArithExpr computeVInit(ConcreteState c) {
        if (!preComputed) {
            preCompute();
            preComputed = true;
        }
        return relevancePredicate.getVInit(this, c, machine);
    }

    public AArithExpr computeV(ConcreteState c, ConcreteState c_) {
        if (!preComputed) {
            preCompute();
            preComputed = true;
        }
        return relevancePredicate.getV(this, c, c_, machine);
    }

    public final AArithExpr computeVInit(AAtomicRelevancePredicate atomicPredicate, ConcreteState c) {
        return registerVInit(atomicPredicate.getVInit(this, c), atomicPredicate, c);
    }

    public final AArithExpr computeV(AAtomicRelevancePredicate atomicPredicate, ConcreteState c, ConcreteState c_) {
        return registerV(atomicPredicate.getV(this, c, c_), atomicPredicate, c, c_);
    }

    public abstract AArithExpr getVInit(VarChanges varChanges, ConcreteState c);

    public abstract AArithExpr getV(VarChanges varChanges, ConcreteState c, ConcreteState c_);

    public abstract AArithExpr getVInit(FunChanges funChanges, ConcreteState c);

    public abstract AArithExpr getV(FunChanges funChanges, ConcreteState c, ConcreteState c_);

    public abstract AArithExpr getVInit(VarDecreases varDecreases, ConcreteState c);

    public abstract AArithExpr getV(VarDecreases varDecreases, ConcreteState c, ConcreteState c_);

    public abstract AArithExpr getVInit(VarIncreases varIncreases, ConcreteState c);

    public abstract AArithExpr getV(VarIncreases varIncreases, ConcreteState c, ConcreteState c_);

    public abstract AArithExpr getVInit(FunDecreases funDecreases, ConcreteState c);

    public abstract AArithExpr getV(FunDecreases funDecreases, ConcreteState c, ConcreteState c_);

    public abstract AArithExpr getVInit(FunIncreases funIncreases, ConcreteState c);

    public abstract AArithExpr getV(FunIncreases funIncreases, ConcreteState c, ConcreteState c_);

    public abstract AArithExpr getVInit(Conditions conditions, ConcreteState c);

    public abstract AArithExpr getV(Conditions conditions, ConcreteState c, ConcreteState c_);

    public Machine getMachine() {
        return machine;
    }

    public RelevancePredicate getRelevancePredicate() {
        return relevancePredicate;
    }

    public LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> getVariantsMapping() {
        return variantsMapping;
    }

    public LinkedHashSet<Event> getRelevantEvents() {
        if (this.relevantEvents == null) {
            this.relevantEvents = machine.getEvents().values().stream().filter(event -> Z3.checkSAT(new And(
                    machine.getInvariant(),
                    machine.getInvariant().accept(new Primer(1)),
                    event.getSubstitution().getPrd(machine.getAssignables()),
                    relevancePredicate
            ), machine.getDefsRegister()).isSAT()).collect(Collectors.toCollection(LinkedHashSet::new));
        }
        return relevantEvents;
    }

}
