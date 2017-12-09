package langs.formal.graphs;

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
    public abstract AFSM<State, Transition> clone();

}
