package algorithms;

import langs.eventb.Event;
import langs.eventb.Machine;
import langs.formal.graphs.ConcreteState;
import langs.formal.graphs.ConcreteTransition;
import langs.formal.graphs.FSM;
import langs.maths.generic.arith.AAssignable;
import langs.maths.generic.arith.literals.AValue;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.operators.And;
import langs.maths.generic.bool.operators.Not;
import langs.maths.generic.bool.operators.Or;
import solvers.z3.Z3;
import solvers.z3.Z3Result;
import visitors.Primer;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Created by gvoiron on 06/12/17.
 * Time : 21:45
 */
public final class VeryOldFullSemanticsComputer extends AComputer<FSM<ConcreteState, ConcreteTransition>> {

    private final Machine machine;

    public VeryOldFullSemanticsComputer(Machine machine) {
        this.machine = machine;
    }

    @Override
    FSM<ConcreteState, ConcreteTransition> run() {
        LinkedHashSet<ConcreteState> initialStates = new LinkedHashSet<>();
        LinkedHashSet<ConcreteState> states = new LinkedHashSet<>();
        ArrayList<ConcreteTransition> transitions = new ArrayList<>();
        Z3Result result;
        while ((result = Z3.checkSAT(new And(
                machine.getInvariant(),
                machine.getInvariant().accept(new Primer(1)),
                machine.getInitialisation().getPrd(machine.getAssignables()),
                new Not(new Or(states.toArray(new ABoolExpr[states.size()]))).accept(new Primer(1))
        ), machine.getDefsRegister())).isSAT()) {
            TreeMap<AAssignable, AValue> model = result.getModel(machine.getAssignables().stream().map(assignable -> assignable.accept(new Primer(1))).collect(Collectors.toCollection(LinkedHashSet::new)));
            ConcreteState c0 = concreteState(states, model);
            initialStates.add(c0);
            states.add(c0);
        }
        LinkedHashSet<ConcreteState> lastReached = new LinkedHashSet<>(states);
        while (!lastReached.isEmpty()) {
            ConcreteState c = lastReached.iterator().next();
            lastReached.remove(c);
            //System.out.println("#Full: " + machine.getName() + " " + transitions.size());
            for (Event e : machine.getEvents().values()) {
                while ((result = Z3.checkSAT(new And(
                        machine.getInvariant(),
                        machine.getInvariant().accept(new Primer(1)),
                        c,
                        e.getSubstitution().getPrd(machine.getAssignables()),
                        new Not(new Or(transitions.stream().filter(aTransition -> aTransition.getSource().equals(c) && aTransition.getEvent().equals(e)).map(aTransition -> aTransition.getTarget().accept(new Primer(1))).toArray(ABoolExpr[]::new)))
                ), machine.getDefsRegister())).isSAT()) {
                    TreeMap<AAssignable, AValue> model = result.getModel(machine.getAssignables().stream().map(assignable -> assignable.accept(new Primer(1))).collect(Collectors.toCollection(LinkedHashSet::new)));
                    ConcreteState c_ = concreteState(states, model);
                    states.add(c_);
                    lastReached.add(c_);
                    transitions.add(new ConcreteTransition(c, e, c_));
                }
            }
        }
        return new FSM<>(initialStates, states, transitions);
    }

    /*@Override
    FSM<ConcreteState, ConcreteTransition> run() {
        Z3Result result;
        while ((result = Z3.checkSAT(new And(
                machine.getInvariant(),
                machine.getInvariant().accept(new Primer(1)),
                machine.getInitialisation().getPrd(machine.getAssignables()),
                new Not(new Or(initialStates.toArray(new ABoolExpr[initialStates.size()]))).accept(new Primer(1))
        ), machine.getDefsRegister())).isSAT()) {
            Model model = result.getModel(machine.getAssignables().stream().map(assignable -> assignable.accept(new Primer(1))).collect(Collectors.toCollection(LinkedHashSet::new)));
            ConcreteState c0 = concreteState(initialStates, model);
            initialStates.add(c0);
        }
        initialStates.forEach(concreteState -> states.put(concreteState, false));
        Optional<Map.Entry<ConcreteState, Boolean>> state = states.entrySet().stream().filter(entry -> entry.getValue().equals(false)).findFirst();
        while (state.isPresent()) {
            ConcreteState c = state.get().getKey();
            states.put(c, true);
            System.out.println(states.entrySet().stream().filter(entry -> entry.getValue().equals(false)).count() * machine.getEvents().size());
            for (Event e : machine.getEvents().values()) {
                while ((result = Z3.checkSAT(new And(
                        machine.getInvariant(),
                        machine.getInvariant().accept(new Primer(1)),
                        c,
                        e.getSubstitution().getPrd(machine.getAssignables()),
                        new Not(new Or(transitions.stream().filter(transition -> transition.getSource().equals(c) && transition.getEvent().equals(e)).map(aTransition -> aTransition.getTarget().accept(new Primer(1))).toArray(ABoolExpr[]::new)))
                ), machine.getDefsRegister())).isSAT()) {
                    Model model = result.getModel(machine.getAssignables().stream().map(assignable -> assignable.accept(new Primer(1))).collect(Collectors.toCollection(LinkedHashSet::new)));
                    ConcreteState c_ = concreteState(states.keySet(), model);
                    if (!states.containsKey(c_)) {
                        states.put(c_, false);
                    }
                    transitions.add(new ConcreteTransition(c, e, c_));
                }
            }
            state = states.entrySet().stream().filter(entry -> entry.getValue().equals(false)).findFirst();
        }
        System.out.println(transitions.size());
        return new FSM<>(initialStates, new LinkedHashSet<>(states.keySet()), transitions);
    }*/

    private ConcreteState concreteState(Set<ConcreteState> states, TreeMap<AAssignable, AValue> model) {
        return states.stream().filter(concreteState -> concreteState.getMapping().equals(model)).findFirst().orElse(new ConcreteState("c_" + states.size(), model));
    }

}
