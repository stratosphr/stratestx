package visitors.dot;

import visitors.interfaces.IObjectFormatter;

/**
 * Created by gvoiron on 06/12/17.
 * Time : 16:53
 */
public final class DOTNode extends ADotObject<DOTNode> {

    private String name;

    public DOTNode(String name) {
        this.name = name;
    }

    @Override
    public DOTNode getThis() {
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
    public DOTNode clone() {
        return new DOTNode(name);
    }

}
