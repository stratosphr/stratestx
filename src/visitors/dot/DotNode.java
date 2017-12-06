package visitors.dot;

import visitors.interfaces.IObjectFormatter;

/**
 * Created by gvoiron on 06/12/17.
 * Time : 16:53
 */
public final class DotNode extends ADotObject<DotNode> {

    private String name;

    public DotNode(String name) {
        this.name = name;
    }

    @Override
    public DotNode getThis() {
        return this;
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    public String getName() {
        return name;
    }

    @Override
    public DotNode clone() {
        return new DotNode(name);
    }

}
