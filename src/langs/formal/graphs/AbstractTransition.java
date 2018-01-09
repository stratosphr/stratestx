package langs.formal.graphs;

import langs.eventb.Event;
import visitors.interfaces.IObjectFormatter;

/**
 * Created by gvoiron on 09/12/17.
 * Time : 20:49
 */
public final class AbstractTransition extends ATransition<AbstractState> {

    public AbstractTransition(AbstractState source, Event event, AbstractState target) {
        super(source, event, target);
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public AbstractTransition cloned() {
        return new AbstractTransition(getSource().cloned(), getEvent().cloned(), getTarget().cloned());
    }

}
