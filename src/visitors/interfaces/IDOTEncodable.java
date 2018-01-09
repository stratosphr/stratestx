package visitors.interfaces;

import langs.formal.graphs.AState;
import langs.formal.graphs.ATransition;

/**
 * Created by gvoiron on 06/12/17.
 * Time : 15:26
 */
public interface IDOTEncodable<State extends AState, Transition extends ATransition<State>> {

    String accept(IDOTEncoder<State, Transition> encoder);

}
