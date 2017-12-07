package visitors.dot;

import visitors.interfaces.IObjectFormatter;

/**
 * Created by gvoiron on 06/12/17.
 * Time : 16:54
 */
public final class DOTEdge extends ADotObject<DOTEdge> {

    private final DOTNode source;
    private final DOTNode target;

    DOTEdge(DOTNode source, DOTNode target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public DOTEdge getThis() {
        return this;
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    public DOTNode getSource() {
        return source;
    }

    public DOTNode getTarget() {
        return target;
    }

    @Override
    public DOTEdge clone() {
        return new DOTEdge(getSource().clone(), getTarget().clone());
    }

}
