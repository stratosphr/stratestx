package langs.formal.graphs;

import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.APredicate;
import visitors.interfaces.IPrimer;

import java.util.TreeMap;

/**
 * Created by gvoiron on 06/12/17.
 * Time : 14:20
 */
public abstract class AState<Key extends Comparable, Value> extends APredicate {

    private final TreeMap<Key, Value> mapping;

    AState(String name, ABoolExpr expr, TreeMap<Key, Value> mapping) {
        super(name, expr);
        this.mapping = mapping;
    }

    @Override
    public abstract AState<Key, Value> accept(IPrimer primer);

    public final TreeMap<Key, Value> getMapping() {
        return mapping;
    }

    @Override
    public abstract AState<Key, Value> clone();

}
