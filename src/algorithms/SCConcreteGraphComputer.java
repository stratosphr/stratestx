package algorithms;

import langs.eventb.Event;
import langs.eventb.substitutions.Skip;
import langs.formal.graphs.AGraph;
import langs.formal.graphs.CTS;
import langs.formal.graphs.ConcreteState;
import langs.formal.graphs.ConcreteTransition;
import utilities.tuples.Tuple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * Created by gvoiron on 19/12/17.
 * Time : 13:39
 */
public final class SCConcreteGraphComputer extends AComputer<AGraph<ConcreteState, ConcreteTransition>> {

    private final AGraph<ConcreteState, ConcreteTransition> graph;
    private final AGraph<ConcreteState, ConcreteTransition> scgraph;
    private final ConcreteState initialState;
    private final String initialTransitionLabel;
    private final String finalTransitionLabel;
    private final LinkedHashMap<ConcreteState, ArrayList<ConcreteTransition>> adjacency;

    public SCConcreteGraphComputer(AGraph<ConcreteState, ConcreteTransition> graph, ConcreteState initialState, String initialTransitionLabel, String finalTransitionLabel) {
        this.graph = graph;
        this.scgraph = graph.clone();
        this.initialState = initialState;
        this.initialTransitionLabel = initialTransitionLabel;
        this.finalTransitionLabel = finalTransitionLabel;
        this.adjacency = new LinkedHashMap<>();
        graph.getStates().forEach(state -> adjacency.put(state, new ArrayList<>()));
        graph.getTransitions().forEach(transition -> adjacency.get(transition.getSource()).add(transition));
    }

    @Override
    AGraph<ConcreteState, ConcreteTransition> run() {
        scgraph.getInitialStates().clear();
        scgraph.getInitialStates().add(initialState);
        scgraph.getStates().add(initialState);
        LinkedHashSet<ConcreteState> sources = new LinkedHashSet<>(graph.getInitialStates());
        LinkedHashSet<ConcreteState> sinks = new LinkedHashSet<>();
        ArrayList<LinkedHashSet<ConcreteState>> components = new SCComponentsComputer<>(graph).compute().getResult();
        for (LinkedHashSet<ConcreteState> component : components) {
            Tuple<LinkedHashSet<ConcreteState>, ArrayList<ConcreteTransition>> rchblPart = new RchblPartComputer<>(new CTS(
                    new LinkedHashSet<>(Collections.singleton(component.iterator().next())),
                    graph.getStates(),
                    new ArrayList<>(graph.getTransitions())
            )).compute().getResult();
            if (component.size() == rchblPart.getLeft().size()) {
                sinks.add(component.iterator().next());
            }
        }
        for (ConcreteState source : sources) {
            scgraph.getTransitions().add(new ConcreteTransition(initialState, new Event(initialTransitionLabel, new Skip()), source));
        }
        for (ConcreteState sink : sinks) {
            scgraph.getTransitions().add(new ConcreteTransition(sink, new Event(finalTransitionLabel, new Skip()), initialState));
        }
        return scgraph;
    }

}
