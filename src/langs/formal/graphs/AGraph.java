package langs.formal.graphs;

import langs.AObject;
import visitors.interfaces.IDOTEncodable;
import visitors.interfaces.IDOTEncoder;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by gvoiron on 06/12/17.
 * Time : 15:24
 */
public abstract class AGraph<State extends AState, Transition extends ATransition<State>> extends AObject implements IDOTEncodable<State, Transition> {

    private final LinkedHashSet<State> initialStates;
    private final LinkedHashSet<State> states;
    private final List<Transition> transitions;

    AGraph(LinkedHashSet<State> initialStates, LinkedHashSet<State> states, List<Transition> transitions) {
        this.initialStates = initialStates;
        this.states = states;
        this.transitions = transitions;
    }

    @Override
    public abstract String accept(IDOTEncoder<State, Transition> encoder);

    public LinkedHashSet<State> getInitialStates() {
        return initialStates;
    }

    public LinkedHashSet<State> getStates() {
        return states;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    @Override
    public abstract AGraph<State, Transition> clone();

}
