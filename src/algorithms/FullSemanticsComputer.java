package algorithms;

import langs.eventb.Event;
import langs.eventb.Machine;
import langs.formal.graphs.ConcreteState;
import langs.formal.graphs.ConcreteTransition;
import langs.formal.graphs.FSM;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.operators.And;
import langs.maths.generic.bool.operators.Not;
import langs.maths.generic.bool.operators.Or;
import solvers.z3.Model;
import solvers.z3.Z3;
import solvers.z3.Z3Result;
import visitors.Primer;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by gvoiron on 06/12/17.
 * Time : 21:45
 */
public final class FullSemanticsComputer extends AComputer<FSM<ConcreteState, ConcreteTransition>> {

    private final Machine machine;
    private final LinkedHashSet<ConcreteState> initialStates;
    private final LinkedHashMap<ConcreteState, Boolean> states;
    private final ArrayList<ConcreteTransition> transitions;

    public FullSemanticsComputer(Machine machine) {
        this.machine = machine;
        this.initialStates = new LinkedHashSet<>();
        this.states = new LinkedHashMap<>();
        this.transitions = new ArrayList<>();
    }

    @Override
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
            //System.out.println(states.entrySet().stream().filter(entry -> entry.getValue().equals(false)).count() * machine.getEvents().size());
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
        return new FSM<>(initialStates, new LinkedHashSet<>(states.keySet()), transitions);
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
            //System.out.println(states.entrySet().stream().filter(entry -> entry.getValue().equals(false)).count() * machine.getEvents().size());
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
        return new FSM<>(initialStates, new LinkedHashSet<>(states.keySet()), transitions);
    }*/

    private ConcreteState concreteState(Set<ConcreteState> states, Model model) {
        return states.stream().filter(concreteState -> concreteState.getMapping().equals(model)).findFirst().orElse(new ConcreteState("c_" + states.size(), model));
    }

}
