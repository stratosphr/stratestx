package algorithms;

import algorithms.heuristics.relevance.RelevancePredicate;
import algorithms.outputs.ATS;
import langs.eventb.Event;
import langs.eventb.Machine;
import langs.formal.graphs.*;
import langs.maths.generic.arith.AAssignable;
import langs.maths.generic.arith.literals.AValue;
import langs.maths.generic.arith.literals.Int;
import langs.maths.generic.bool.operators.And;
import langs.maths.generic.bool.operators.GEQ;
import solvers.z3.Model;
import solvers.z3.Z3;
import solvers.z3.Z3Result;
import visitors.Primer;

import java.util.*;
import java.util.stream.Collectors;

import static algorithms.heuristics.EColor.GREEN;

/**
 * Created by gvoiron on 19/12/17.
 * Time : 22:08
 */
public final class RCXPComputer extends AComputer<ATS> {

    private final Machine machine;
    private final ATS ats;
    private final RelevancePredicate relevancePredicate;
    private final LinkedHashSet<AAssignable> primedAssignables;
    private final LinkedHashMap<Map<AAssignable, AValue>, ConcreteState> CMappings;

    public RCXPComputer(Machine machine, ATS ats, RelevancePredicate relevancePredicate) {
        this.machine = machine;
        this.ats = ats.clone();
        this.CMappings = new LinkedHashMap<>();
        this.relevancePredicate = relevancePredicate;
        this.primedAssignables = machine.getAssignables().stream().map(assignable -> assignable.accept(new Primer(1))).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    ATS run() {
        Z3Result result;
        ats.getCTS().getStates().forEach(c -> CMappings.put(c.getMapping(), c));
        Stack<ConcreteState> RCS = new Stack<>();
        for (ConcreteState c : ats.getCTS().getStates().stream().filter(c -> ats.getKappa().get(c) == GREEN).collect(Collectors.toList())) {
            result = Z3.checkSAT(new And(
                    machine.getInvariant(),
                    c,
                    new GEQ(relevancePredicate.getVInit(c, machine), new Int(0))
            ), machine.getDefsRegister());
            if (result.isSAT()) {
                RCS.add(c);
            } else {
                throw new Error("The green concrete state \"" + c + "\" does not satisfy the relevance predicate. This is probably not a problem and this error should be deleted.");
            }
        }
        LinkedHashSet<ConcreteState> PRCS = new LinkedHashSet<>();
        while (!RCS.isEmpty()) {
            ConcreteState c = RCS.peek();
            RCS.pop();
            PRCS.add(c);
//
            if (relevancePredicate.getVariantsMapping().get(c) == null) {
                throw new Error("No variant mapping found for state \"" + c.toString() + "\".");
            }
            AbstractState q = ats.getAlpha().get(c);
            for (Event e : machine.getEvents().values()) {
                for (AbstractState q_ : ats.getMTS().getStates()) {
                    if (ats.getMTS().getTransitions().contains(new AbstractTransition(q, e, q_))) {
                        result = Z3.checkSAT(new And(
                                machine.getInvariant(),
                                machine.getInvariant().accept(new Primer(1)),
                                c,
                                e.getSubstitution().getPrd(machine.getAssignables()),
                                q_.accept(new Primer(1)),
                                relevancePredicate
                        ), machine.getDefsRegister());
                        if (result.isSAT()) {
                            Model c_Model = result.getModel(primedAssignables);
                            ConcreteState c_ = CMappings.getOrDefault(c_Model, new ConcreteState("c" + ats.getCTS().getStates().size() + q_.getName(), c_Model));
                            if (!PRCS.contains(c_)) {
                                result = Z3.checkSAT(new And(
                                        machine.getInvariant(),
                                        machine.getInvariant().accept(new Primer(1)),
                                        c,
                                        c_.accept(new Primer(1)),
                                        new GEQ(relevancePredicate.getV(c, c_, machine), new Int(0))
                                ), machine.getDefsRegister());
                                if (result.isSAT()) {
                                    /*relevancePredicate.getVariantsMapping().put(c_, new LinkedHashMap<>());
                                    Iterator<AAtomicRelevancePredicate> apIterator = relevancePredicate.getAtomicPredicates().iterator();
                                    Iterator<AArithExpr> opIterator = ((Plus) relevancePredicate.getV(c, c_, machine)).getOperands().iterator();
                                    while (apIterator.hasNext()) {
                                        Var variant = new Var("variant!");
                                        AAtomicRelevancePredicate ap = apIterator.next();
                                        AArithExpr op = opIterator.next();
                                        DefsRegister tmpDefsRegister = new DefsRegister(machine.getDefsRegister());
                                        tmpDefsRegister.getVarsDefs().put(variant.getName(), new Z());
                                        Z3Result result1 = Z3.checkSAT(new And(machine.getInvariant(), machine.getInvariant().accept(new Primer(1)), c, c_.accept(new Primer(1)), new Equals(variant, op)), tmpDefsRegister);
                                        relevancePredicate.getVariantsMapping().get(c_).put(ap, result1.getModel(new LinkedHashSet<>(Collections.singletonList(variant))).get(variant));
                                        if (ap instanceof AtomicPredicateMultiImplies) {
                                            ((AtomicPredicateMultiImplies) ap).getImplies().forEach(imply -> {
                                                Z3.checkSAT(new And(Machine.getInvariant(), c_, new Equals(variant, imply.getThenPart().getVariantC_(c, c_, variantsMapping))));
                                                variantsMapping.get(c_).put(imply.getThenPart(), Z3.getModel(new LinkedHashSet<>(Collections.singletonList(variant))).get(variant));
                                            });
                                        }
                                    }*/
                                    RCS.push(c_);
                                }
                            }
                            ats.getCTS().getStates().add(c_);
                            CMappings.put(c_Model, c_);
                            ats.getCTS().getTransitions().add(new ConcreteTransition(c, e, c_));
                            ats.getAlpha().put(c_, q_);
                            ats.getKappa().put(c_, GREEN);
                        }
                    }
                }
            }
        }
        return new ATS(machine.clone(), ats.getMTS(), new CTS(ats.getCTS().getInitialStates(), ats.getCTS().getStates(), new ArrayList<>(new LinkedHashSet<>(ats.getCTS().getTransitions()))), ats.getAlpha(), ats.getKappa());
    }

}
