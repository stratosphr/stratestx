package visitors.dot;

import algorithms.RchblPartComputer;
import langs.formal.graphs.*;
import utilities.Tuple;
import visitors.AFormatter;
import visitors.interfaces.IDOTEncoder;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
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

    public DOTEncoder(boolean useFullLabels, ERankDir rankDir) {
        this.useFullLabels = useFullLabels;
        this.rankDir = rankDir;
        this.nodes = new LinkedHashSet<>();
        this.edges = new ArrayList<>();
    }

    @Override
    public String visit(MTS mts) {
        return null;
    }

    @Override
    public String visit(CTS cts) {
        cts.getInitialStates().stream().map(this::encodeInitialNode).forEach(nodes::add);
        Tuple<LinkedHashSet<ConcreteState>, ArrayList<ConcreteTransition>> rchblPart = new RchblPartComputer<>(cts).compute().getResult();
        rchblPart.getLeft().stream().filter(state -> !cts.getInitialStates().contains(state)).map(this::encodeReachedNode).forEach(nodes::add);
        cts.getStates().stream().filter(state -> !rchblPart.getLeft().contains(state)).map(this::encodeUnreachedNode).forEach(nodes::add);
        rchblPart.getRight().stream().map(this::encodeReachedTransition).forEach(edges::add);
        cts.getTransitions().stream().filter(transition -> !rchblPart.getRight().contains(transition)).map(this::encodeUnreachedTransition).forEach(edges::add);
        return line("digraph g {") + line() + indentRight() + indentLine("rankdir=\"" + rankDir + "\"") + line() + nodes.stream().map(state -> indentLine(state.toString())).collect(Collectors.joining()) + line() + edges.stream().map(edge -> indentLine(edge.toString())).collect(Collectors.joining()) + line() + indentLeft() + indentLine("}");
    }

    private DOTNode encodeInitialNode(AState state) {
        DOTNode invisible = new DOTNode("__invisible__").setShape("point").setColor("forestgreen");
        DOTNode initial = encodeReachedNode(state).setPenWidth(3).setComment("Initial");
        nodes.add(invisible);
        edges.add(new DOTEdge(invisible, initial).setPenWidth(3).setColor("forestgreen"));
        return initial;
    }

    private DOTNode encodeReachedNode(AState state) {
        return new DOTNode(state.getName()).setLabel(useFullLabels ? state : state.getName()).setShape("box").setStyle("rounded, filled").setColor("forestgreen").setFillColor("limegreen").setColor("forestgreen");
    }

    private DOTNode encodeUnreachedNode(AState state) {
        return encodeReachedNode(state).setFillColor("lightblue2").setColor("deepskyblue4");
    }

    private DOTEdge encodeReachedTransition(ATransition transition) {
        return new DOTEdge(new DOTNode(transition.getSource().getName()), new DOTNode(transition.getTarget().getName())).setLabel(transition.getEvent().getName()).setColor("forestgreen");
    }

    private DOTEdge encodeUnreachedTransition(ATransition transition) {
        return encodeReachedTransition(transition).setColor("black").setStyle("dashed");
    }

    @SuppressWarnings("unused")
    public enum ERankDir {LR, TB}

}
