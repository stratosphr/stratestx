package algorithms;

import algorithms.heuristics.EColor;
import algorithms.outputs.ATS;
import langs.eventb.Event;
import langs.eventb.Machine;
import langs.formal.graphs.*;
import langs.maths.def.DefsRegister;
import langs.maths.generic.arith.literals.Int;
import langs.maths.generic.arith.literals.Var;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.operators.And;
import langs.maths.generic.bool.operators.Equals;
import langs.maths.generic.bool.operators.Not;
import langs.maths.generic.bool.operators.Or;
import langs.maths.set.literals.Z;
import solvers.z3.Model;
import solvers.z3.Z3;
import solvers.z3.Z3Result;
import utilities.Streams;
import visitors.Primer;

import java.util.*;
import java.util.stream.Collectors;

import static algorithms.heuristics.EColor.GREEN;

/**
 * Created by gvoiron on 06/12/17.
 * Time : 21:45
 */
public final class FullSemanticsComputer extends AComputer<ATS> {

    private final Machine machine;
    private final LinkedHashSet<AbstractState> A;
    private final LinkedHashSet<AbstractState> Q0;
    private final LinkedHashSet<AbstractState> Q;
    private final LinkedHashSet<AbstractTransition> Delta;
    private final LinkedHashMap<ConcreteState, AbstractState> alpha;
    private final LinkedHashMap<ConcreteState, EColor> kappa;
    private final LinkedHashSet<ConcreteState> initialStates;
    private final LinkedHashMap<ConcreteState, Boolean> states;
    private final ArrayList<ConcreteTransition> transitions;
    private final Var q_Index;
    private final DefsRegister defsRegisterWithIndex;
    private final ABoolExpr q_Constraint;

    public FullSemanticsComputer(Machine machine, LinkedHashSet<AbstractState> A) {
        this.machine = machine;
        this.A = A;
        this.Q0 = new LinkedHashSet<>();
        this.Q = new LinkedHashSet<>();
        this.Delta = new LinkedHashSet<>();
        this.initialStates = new LinkedHashSet<>();
        this.states = new LinkedHashMap<>();
        this.transitions = new ArrayList<>();
        this.alpha = new LinkedHashMap<>();
        this.kappa = new LinkedHashMap<>();
        this.q_Index = new Var("i!");
        this.defsRegisterWithIndex = new DefsRegister(machine.getDefsRegister());
        defsRegisterWithIndex.getVarsDefs().put(q_Index.getName(), new Z());
        this.q_Constraint = new Or(Streams.mapWithIndex(A.stream(), ((index, q) -> new And(new Equals(q_Index, new Int(index)), q.accept(new Primer(1))))).toArray(ABoolExpr[]::new));
    }

    @Override
    ATS run() {
        Z3Result result;
        while ((result = Z3.checkSAT(new And(
                machine.getInvariant(),
                machine.getInvariant().accept(new Primer(1)),
                machine.getInitialisation().getPrd(machine.getAssignables()),
                new Not(new Or(initialStates.toArray(new ABoolExpr[initialStates.size()]))).accept(new Primer(1)),
                q_Constraint
        ), defsRegisterWithIndex)).isSAT()) {
            Model q0Model = result.getModel(new LinkedHashSet<>(Collections.singletonList(q_Index)));
            Model c0Model = result.getModel(machine.getAssignables().stream().map(assignable -> assignable.accept(new Primer(1))).collect(Collectors.toCollection(LinkedHashSet::new)));
            AbstractState q0 = Streams.filterWithIndex(A.stream(), entry -> entry.getKey().equals(q0Model.get(q_Index).getValue())).findFirst().get().getValue();
            ConcreteState c0 = concreteState(initialStates, c0Model, q0);
            Q0.add(q0);
            Q.add(q0);
            initialStates.add(c0);
            alpha.put(c0, q0);
            kappa.put(c0, GREEN);
        }
        initialStates.forEach(concreteState -> states.put(concreteState, false));
        Optional<Map.Entry<ConcreteState, Boolean>> state = states.entrySet().stream().filter(entry -> entry.getValue().equals(false)).findFirst();
        while (state.isPresent()) {
            ConcreteState c = state.get().getKey();
            states.put(c, true);
            for (Event e : machine.getEvents().values()) {
                while ((result = Z3.checkSAT(new And(
                        machine.getInvariant(),
                        machine.getInvariant().accept(new Primer(1)),
                        c,
                        e.getSubstitution().getPrd(machine.getAssignables()),
                        new Not(new Or(transitions.stream().filter(transition -> transition.getSource().equals(c) && transition.getEvent().equals(e)).map(aTransition -> aTransition.getTarget().accept(new Primer(1))).toArray(ABoolExpr[]::new))),
                        q_Constraint
                ), defsRegisterWithIndex)).isSAT()) {
                    Model q_Model = result.getModel(new LinkedHashSet<>(Collections.singletonList(q_Index)));
                    Model c_Model = result.getModel(machine.getAssignables().stream().map(assignable -> assignable.accept(new Primer(1))).collect(Collectors.toCollection(LinkedHashSet::new)));
                    AbstractState q_ = Streams.filterWithIndex(A.stream(), entry -> entry.getKey().equals(q_Model.get(q_Index).getValue())).findFirst().get().getValue();
                    ConcreteState c_ = concreteState(states.keySet(), c_Model, q_);
                    Q.add(q_);
                    Delta.add(new AbstractTransition(alpha.get(c), e, q_));
                    if (!states.containsKey(c_)) {
                        states.put(c_, false);
                    }
                    transitions.add(new ConcreteTransition(c, e, c_));
                    alpha.put(c_, q_);
                    kappa.put(c_, GREEN);
                    if (transitions.size() % 100 == 0) {
                        System.out.println("transitions: " + transitions.size());
                    }
                    if (states.size() % 100 == 0) {
                        System.out.println("states: " + states.size());
                    }
                }
            }
            state = states.entrySet().stream().filter(entry -> entry.getValue().equals(false)).findFirst();
        }
        return new ATS(machine, new MTS(Q0, Q, Delta), new CTS(initialStates, new LinkedHashSet<>(states.keySet()), transitions), alpha, kappa);
    }

    private ConcreteState concreteState(Set<ConcreteState> states, Model model, AbstractState q) {
        return states.stream().filter(concreteState -> concreteState.getMapping().equals(model)).findFirst().orElse(new ConcreteState("c" + states.size() + q.getName(), model));
    }

}
