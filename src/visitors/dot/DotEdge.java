package visitors.dot;

import visitors.interfaces.IObjectFormatter;

/**
 * Created by gvoiron on 06/12/17.
 * Time : 16:54
 */
public final class DotEdge extends ADotObject<DotEdge> {

    private DotNode source;
    private DotNode target;

    public DotEdge(DotNode source, DotNode target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public DotEdge getThis() {
        return this;
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    public DotNode getSource() {
        return source;
    }

    public DotNode getTarget() {
        return target;
    }

    @Override
    public DotEdge clone() {
        return new DotEdge(getSource().clone(), getTarget().clone());
    }

}
