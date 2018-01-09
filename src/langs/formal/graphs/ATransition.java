package langs.formal.graphs;

import langs.AObject;
import langs.eventb.Event;

/**
 * Created by gvoiron on 06/12/17.
 * Time : 15:11
 */
public abstract class ATransition<State extends AState> extends AObject {

    private final State source;
    private final Event event;
    private final State target;

    ATransition(State source, Event event, State target) {
        this.source = source;
        this.event = event;
        this.target = target;
    }

    public final State getSource() {
        return source;
    }

    public final Event getEvent() {
        return event;
    }

    public final State getTarget() {
        return target;
    }

    @Override
    public abstract ATransition<State> cloned();

}
