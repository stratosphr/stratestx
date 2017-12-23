package langs.formal.graphs;

import visitors.interfaces.IDOTEncoder;
import visitors.interfaces.IObjectFormatter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

/**
 * Created by gvoiron on 09/12/17.
 * Time : 20:45
 */
public final class MTS extends AFSM<AbstractState, AbstractTransition> {

    public MTS(LinkedHashSet<AbstractState> initialStates, LinkedHashSet<AbstractState> abstractStates, LinkedHashSet<AbstractTransition> abstractTransitions) {
        super(initialStates, abstractStates, new ArrayList<>(abstractTransitions));
    }

    @Override
    public String accept(IDOTEncoder<AbstractState, AbstractTransition> encoder) {
        return encoder.visit(this);
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public MTS clone() {
        return new MTS(getInitialStates().stream().map(AbstractState::clone).collect(Collectors.toCollection(LinkedHashSet::new)), getStates().stream().map(AbstractState::clone).collect(Collectors.toCollection(LinkedHashSet::new)), getTransitions().stream().map(AbstractTransition::clone).collect(Collectors.toCollection(LinkedHashSet::new)));
    }

}
