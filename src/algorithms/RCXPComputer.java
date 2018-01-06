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
            }
        }
        LinkedHashSet<ConcreteState> PRCS = new LinkedHashSet<>();
        while (!RCS.isEmpty()) {
            ConcreteState c = RCS.peek();
            RCS.pop();
            PRCS.add(c);
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
