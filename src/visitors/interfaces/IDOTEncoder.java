package visitors.interfaces;

import langs.formal.graphs.AState;
import langs.formal.graphs.ATransition;
import langs.formal.graphs.CTS;
import langs.formal.graphs.MTS;

/**
 * Created by gvoiron on 06/12/17.
 * Time : 15:27
 */
public interface IDOTEncoder<State extends AState, Transition extends ATransition<State>> {

    String visit(MTS mts);

    String visit(CTS cts);

}
