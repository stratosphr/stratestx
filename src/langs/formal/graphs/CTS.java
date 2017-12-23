package langs.formal.graphs;

import visitors.interfaces.IDOTEncoder;
import visitors.interfaces.IObjectFormatter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by gvoiron on 06/12/17.
 * Time : 15:23
 */
public final class CTS extends AFSM<ConcreteState, ConcreteTransition> {

    public CTS(LinkedHashSet<ConcreteState> initialStates, LinkedHashSet<ConcreteState> states, List<ConcreteTransition> transitions) {
        super(initialStates, states, transitions);
    }

    @Override
    public String accept(IDOTEncoder<ConcreteState, ConcreteTransition> encoder) {
        return encoder.visit(this);
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public CTS clone() {
        return new CTS(getInitialStates().stream().map(ConcreteState::clone).collect(Collectors.toCollection(LinkedHashSet::new)), getStates().stream().map(ConcreteState::clone).collect(Collectors.toCollection(LinkedHashSet::new)), getTransitions().stream().map(ConcreteTransition::clone).collect(Collectors.toCollection(ArrayList::new)));
    }

}
