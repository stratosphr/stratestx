package langs.formal.graphs;

import langs.eventb.Event;
import visitors.interfaces.IObjectFormatter;

/**
 * Created by gvoiron on 06/12/17.
 * Time : 15:09
 */
public final class ConcreteTransition extends ATransition<ConcreteState> {

    public ConcreteTransition(ConcreteState source, Event event, ConcreteState target) {
        super(source, event, target);
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public ConcreteTransition cloned() {
        return new ConcreteTransition(getSource().cloned(), getEvent().cloned(), getTarget().cloned());
    }

}
