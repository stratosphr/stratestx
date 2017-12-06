package algorithms;

import langs.formal.graphs.AGraph;
import langs.formal.graphs.AState;
import langs.formal.graphs.ATransition;
import utilities.Tuple;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * Created by gvoiron on 06/12/17.
 * Time : 20:59
 */
public final class RchblPartComputer<State extends AState, Transition extends ATransition<State>> extends AComputer<Tuple<LinkedHashSet<State>, ArrayList<Transition>>> {

    private final AGraph<State, Transition> fsm;
    private final LinkedHashMap<State, ArrayList<Transition>> adjacency;

    public RchblPartComputer(AGraph<State, Transition> fsm) {
        this.fsm = fsm;
        this.adjacency = new LinkedHashMap<>();
        fsm.getStates().forEach(s -> adjacency.put(s, new ArrayList<>()));
        fsm.getTransitions().forEach(transition -> adjacency.get(transition.getSource()).add(transition));
    }

    @Override
    protected Tuple<LinkedHashSet<State>, ArrayList<Transition>> run() {
        LinkedHashSet<State> reachableStates = new LinkedHashSet<>();
        ArrayList<Transition> reachableTransitions = new ArrayList<>();
        fsm.getInitialStates().forEach(initialState -> run_(initialState, reachableStates, reachableTransitions));
        return new Tuple<>(reachableStates, reachableTransitions);
    }

    private void run_(State startState, LinkedHashSet<State> reachableStates, ArrayList<Transition> reachableTransitions) {
        if (reachableStates.add(startState)) {
            adjacency.get(startState).forEach(transition -> {
                reachableTransitions.add(transition);
                run_(transition.getTarget(), reachableStates, reachableTransitions);
            });
        }
    }

}
