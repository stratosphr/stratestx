package langs.formal.graphs;

import visitors.interfaces.IDOTEncoder;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by gvoiron on 06/12/17.
 * Time : 15:23
 */
public abstract class AFSM<State extends AState, Transition extends ATransition<State>> extends AGraph<State, Transition> {

    AFSM(LinkedHashSet<State> initialStates, LinkedHashSet<State> states, List<Transition> transitions) {
        super(initialStates, states, transitions);
    }

    @Override
    public String accept(IDOTEncoder<State, Transition> encoder) {
        return encoder.visit(this);
    }

    @Override
    public abstract AFSM<State, Transition> cloned();

}
