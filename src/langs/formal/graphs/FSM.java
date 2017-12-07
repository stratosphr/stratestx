package langs.formal.graphs;

import visitors.interfaces.IDOTEncoder;
import visitors.interfaces.IObjectFormatter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

/**
 * Created by gvoiron on 06/12/17.
 * Time : 15:23
 */
public final class FSM<State extends AState, Transition extends ATransition<State>> extends AGraph<State, Transition> {

    public FSM(LinkedHashSet<State> initialStates, LinkedHashSet<State> states, ArrayList<Transition> transitions) {
        super(initialStates, states, transitions);
    }

    @Override
    public String accept(IDOTEncoder<State, Transition> encoder) {
        return encoder.visit(this);
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public FSM<State, Transition> clone() {
        return new FSM<>(
                getInitialStates().stream().map(state -> (State) state.clone()).collect(Collectors.toCollection(LinkedHashSet::new)),
                getStates().stream().map(state -> (State) state.clone()).collect(Collectors.toCollection(LinkedHashSet::new)),
                getTransitions().stream().map(state -> (Transition) state.clone()).collect(Collectors.toCollection(ArrayList::new))
        );
    }

}
