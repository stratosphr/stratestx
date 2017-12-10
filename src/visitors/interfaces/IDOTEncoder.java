package visitors.interfaces;

import langs.formal.graphs.AFSM;
import langs.formal.graphs.AState;
import langs.formal.graphs.ATransition;

/**
 * Created by gvoiron on 06/12/17.
 * Time : 15:27
 */
public interface IDOTEncoder<State extends AState, Transition extends ATransition<State>> {

    String visit(AFSM<State, Transition> stateTransitionAFSM);

}
