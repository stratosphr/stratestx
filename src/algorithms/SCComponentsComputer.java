package algorithms;

import langs.formal.graphs.AGraph;
import langs.formal.graphs.AState;
import langs.formal.graphs.ATransition;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Stack;

/**
 * Created by gvoiron on 19/12/17.
 * Time : 12:25
 */
final class SCComponentsComputer<State extends AState, Transition extends ATransition<State>> extends AComputer<ArrayList<LinkedHashSet<State>>> {

    private final AGraph<State, Transition> graph;
    private int num;
    private final LinkedHashMap<State, Integer> verticesNums;
    private final LinkedHashMap<State, Integer> verticesNumReachable;
    private final LinkedHashMap<State, Boolean> verticesInStack;
    private final Stack<State> stack;
    private final ArrayList<LinkedHashSet<State>> components;
    private final LinkedHashMap<State, ArrayList<Transition>> adjacency;

    SCComponentsComputer(AGraph<State, Transition> graph) {
        this.graph = graph;
        this.verticesNums = new LinkedHashMap<>();
        this.verticesNumReachable = new LinkedHashMap<>();
        this.verticesInStack = new LinkedHashMap<>();
        this.num = 0;
        this.stack = new Stack<>();
        this.components = new ArrayList<>();
        this.adjacency = new LinkedHashMap<>();
        graph.getStates().forEach(state -> adjacency.put(state, new ArrayList<>()));
        graph.getTransitions().forEach(transition -> adjacency.get(transition.getSource()).add(transition));
    }

    @Override
    ArrayList<LinkedHashSet<State>> run() {
        for (State v : graph.getStates()) {
            if (!verticesNums.containsKey(v)) {
                search(v);
            }
        }
        return components;
    }

    private void search(State v) {
        verticesNums.put(v, num);
        verticesNumReachable.put(v, num);
        ++num;
        stack.push(v);
        verticesInStack.put(v, true);
        for (Transition transition : adjacency.get(v)) {
            State w = transition.getTarget();
            if (!verticesNums.containsKey(w)) {
                search(w);
                verticesNumReachable.put(v, Math.min(verticesNumReachable.get(v), verticesNumReachable.get(w)));
            } else if (verticesInStack.get(w)) {
                verticesNumReachable.put(v, Math.min(verticesNumReachable.get(v), verticesNums.get(w)));
            }
        }
        if (verticesNumReachable.get(v).equals(verticesNums.get(v))) {
            LinkedHashSet<State> component = new LinkedHashSet<>();
            State w;
            do {
                w = stack.pop();
                verticesInStack.put(w, false);
                component.add(w);
            } while (!w.equals(v));
            components.add(component);
        }
    }

}
