package langs.formal.graphs;

import com.microsoft.z3.BoolExpr;
import langs.maths.generic.arith.AAssignable;
import langs.maths.generic.arith.literals.AValue;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.operators.And;
import langs.maths.generic.bool.operators.Equals;
import visitors.interfaces.IObjectFormatter;
import visitors.interfaces.IPrimer;
import visitors.interfaces.ISMTEncoder;

import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Created by gvoiron on 06/12/17.
 * Time : 14:14
 */
public final class ConcreteState extends AState<AAssignable, AValue> {

    public ConcreteState(String name, TreeMap<AAssignable, AValue> mapping) {
        super(name, new And(mapping.entrySet().stream().map(entry -> new Equals(entry.getKey(), entry.getValue())).toArray(ABoolExpr[]::new)), mapping);
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
    public ConcreteState accept(IPrimer primer) {
        return primer.visit(this);
    }

    @Override
    public ConcreteState clone() {
        return new ConcreteState(getName(), getMapping().entrySet().stream().collect(Collectors.toMap(o -> o.getKey().clone(), o -> o.getValue().clone(), (value1, value2) -> value1, TreeMap::new)));
    }

}
