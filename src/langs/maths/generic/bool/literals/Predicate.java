package langs.maths.generic.bool.literals;

import com.microsoft.z3.BoolExpr;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.APredicate;
import visitors.interfaces.IObjectFormatter;
import visitors.interfaces.IPrimer;
import visitors.interfaces.ISMTEncoder;

/**
 * Created by gvoiron on 07/12/17.
 * Time : 20:47
 */
public final class Predicate extends APredicate {

    public Predicate(String name, ABoolExpr expr) {
        super(name, expr);
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
    public Predicate accept(IPrimer primer) {
        return primer.visit(this);
    }

    @Override
    public Predicate cloned() {
        return new Predicate(getName(), getExpr().cloned());
    }

}
