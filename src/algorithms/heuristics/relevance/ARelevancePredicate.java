package algorithms.heuristics.relevance;

import com.microsoft.z3.BoolExpr;
import langs.maths.generic.arith.literals.Fun;
import langs.maths.generic.bool.ABoolExpr;
import visitors.interfaces.IObjectFormatter;
import visitors.interfaces.IPrimer;
import visitors.interfaces.ISMTEncoder;

import java.util.LinkedHashSet;

/**
 * Created by gvoiron on 19/12/17.
 * Time : 22:09
 */

public abstract class ARelevancePredicate extends ABoolExpr {

    private final ABoolExpr expr;

    protected ARelevancePredicate(ABoolExpr expr) {
        this.expr = expr;
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return expr.accept(formatter);
    }

    @Override
    public BoolExpr accept(ISMTEncoder encoder) {
        return expr.accept(encoder);
    }

    @Override
    public ABoolExpr accept(IPrimer primer) {
        return expr.accept(primer);
    }

    @Override
    public LinkedHashSet<Fun> getFuns() {
        return expr.getFuns();
    }

    @Override
    public abstract ARelevancePredicate cloned();

}
