package visitors.dot;

import algorithms.RchblPartComputer;
import langs.formal.graphs.AFSM;
import langs.formal.graphs.AState;
import langs.formal.graphs.ATransition;
import utilities.tuples.Tuple;
import visitors.AFormatter;
import visitors.interfaces.IDOTEncoder;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by gvoiron on 06/12/17.
 * Time : 15:51
 */
public final class DOTEncoder<State extends AState, Transition extends ATransition<State>> extends AFormatter implements IDOTEncoder<State, Transition> {

    private final boolean useFullLabels;
    private final ERankDir rankDir;
    private final LinkedHashSet<DOTNode> nodes;
    private final List<DOTEdge> edges;
    private final List<Tuple<String, LinkedHashMap<State, ?>>> statesProperties;

    public DOTEncoder(boolean useFullLabels, ERankDir rankDir) {
        this(useFullLabels, rankDir, Collections.emptyList());
    }

    public DOTEncoder(boolean useFullLabels, ERankDir rankDir, List<Tuple<String, LinkedHashMap<State, ?>>> statesProperties) {
        this.useFullLabels = useFullLabels;
        this.rankDir = rankDir;
        this.nodes = new LinkedHashSet<>();
        this.edges = new ArrayList<>();
        this.statesProperties = statesProperties;
    }

    @Override
    public String visit(AFSM<State, Transition> fsm) {
        fsm.getInitialStates().stream().map(this::encodeInitialNode).forEach(nodes::add);
        Tuple<LinkedHashSet<State>, ArrayList<Transition>> rchblPart = new RchblPartComputer<>(fsm).compute().getResult();
        rchblPart.getLeft().stream().filter(state -> !fsm.getInitialStates().contains(state)).map(this::encodeReachedNode).forEach(nodes::add);
        fsm.getStates().stream().filter(state -> !rchblPart.getLeft().contains(state)).map(this::encodeUnreachedNode).forEach(nodes::add);
        rchblPart.getRight().stream().map(this::encodeReachedTransition).forEach(edges::add);
        fsm.getTransitions().stream().filter(transition -> !rchblPart.getRight().contains(transition)).map(this::encodeUnreachedTransition).forEach(edges::add);
        return line("digraph g {") + line() + indentRight() + indentLine("rankdir=\"" + rankDir + "\"") + line() + nodes.stream().map(state -> indentLine(state.toString())).collect(Collectors.joining()) + line() + edges.stream().map(edge -> indentLine(edge.toString())).collect(Collectors.joining()) + line() + indentLeft() + indentLine("}");
    }

    private DOTNode encodeInitialNode(State state) {
        DOTNode invisible = new DOTNode("__invisible__").setShape("point").setColor("forestgreen");
        DOTNode initial = encodeReachedNode(state).setPenWidth(3).setComment("Initial");
        nodes.add(invisible);
        edges.add(new DOTEdge(invisible, initial).setPenWidth(3).setColor("forestgreen"));
        return initial;
    }

    private DOTNode encodeReachedNode(State state) {
        String properties = statesProperties.stream().filter(tuple -> tuple.getRight().containsKey(state)).map(tuple -> "<i>" + tuple.getLeft() + "=" + tuple.getRight().get(state) + "</i>").collect(Collectors.joining("<br/>"));
        return new DOTNode(state.getName()).setLabel("<b>" + (useFullLabels ? state : state.getName()).toString().replaceAll("<", "&lt;").replaceAll(">", "&gt;") + "</b>" + (properties.isEmpty() ? "" : "<br/><br/>" + properties), true).setShape("box").setStyle("rounded, filled").setFillColor("limegreen").setColor("forestgreen");
    }

    private DOTNode encodeUnreachedNode(State state) {
        return encodeReachedNode(state).setFillColor("lightblue2").setColor("deepskyblue4");
    }

    private DOTEdge encodeReachedTransition(Transition transition) {
        return new DOTEdge(new DOTNode(transition.getSource().getName()), new DOTNode(transition.getTarget().getName())).setLabel(transition.getEvent().getName(), true).setColor("forestgreen");
    }

    private DOTEdge encodeUnreachedTransition(Transition transition) {
        return encodeReachedTransition(transition).setColor("black").setStyle("dashed");
    }

    public enum ERankDir {
        LR, TB
    }

}
