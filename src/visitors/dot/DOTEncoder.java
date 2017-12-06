package visitors.dot;

import langs.formal.graphs.AState;
import langs.formal.graphs.ATransition;
import langs.formal.graphs.FSM;
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
    public String visit(FSM<State, Transition> fsm) {
        fsm.getInitialStates().stream().map(this::encodeInitialNode).forEach(nodes::add);
        fsm.getStates().stream().filter(state -> !fsm.getInitialStates().contains(state)).map(this::encodeReachedNode).forEach(nodes::add);
        return line("digraph g {") + line() + indentRight() + indentLine("rankdir=\"" + rankDir + "\"") + line() + nodes.stream().map(state -> indentLine(state.toString())).collect(Collectors.joining()) + line() + edges.stream().map(edge -> indentLine(edge.toString())).collect(Collectors.joining()) + line() + indentLeft() + indentLine("}");
    }

    private DOTNode encodeInitialNode(State state) {
        System.err.println(state.getName());
        DOTNode invisible = new DOTNode("__invisible__").setShape("point").setColor("forestgreen");
        DOTNode initial = encodeReachedNode(state).setPenWidth(3).setComment("Initial");
        nodes.add(invisible);
        edges.add(new DOTEdge(invisible, initial).setColor("forestgreen"));
        return initial;
    }

    private DOTNode encodeReachedNode(State state) {
        return new DOTNode(state.getName()).setLabel(useFullLabels ? state : state.getName()).setShape("box").setStyle("rounded, filled").setColor("forestgreen").setFillColor("limegreen").setColor("forestgreen");
    }

    public enum ERankDir {LR, TB}

}
