package langs.formal.graphs;

import com.microsoft.z3.BoolExpr;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.literals.Predicate;
import langs.maths.generic.bool.operators.And;
import langs.maths.generic.bool.operators.Not;
import visitors.interfaces.IObjectFormatter;
import visitors.interfaces.IPrimer;
import visitors.interfaces.ISMTEncoder;

import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Created by gvoiron on 07/12/17.
 * Time : 20:57
 */
public final class AbstractState extends AState<Predicate, Boolean> {

    public AbstractState(String name, TreeMap<Predicate, Boolean> mapping) {
        super(name, new And(mapping.entrySet().stream().map(entry -> mapping.get(entry.getKey()) ? entry.getKey() : new Not(entry.getKey())).toArray(ABoolExpr[]::new)), mapping);
        System.out.println(new And(mapping.entrySet().stream().map(entry -> mapping.get(entry.getKey()) ? entry.getKey() : new Not(entry.getKey())).toArray(ABoolExpr[]::new)));
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public BoolExpr accept(ISMTEncoder encoder) {
        return encoder.visit(this);
    }

    @Override
    public AbstractState accept(IPrimer primer) {
        return primer.visit(this);
    }

    @Override
    public AbstractState clone() {
        return new AbstractState(getName(), getMapping().entrySet().stream().collect(Collectors.toMap(o -> o.getKey().clone(), Map.Entry::getValue, (value1, value2) -> value1, TreeMap::new)));
    }

}
